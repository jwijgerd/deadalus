/*
 * Copyright 2010 Joost van de Wijgerd <joost@vdwbv.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.googlecode.deadalus.server.internal;

import com.googlecode.deadalus.*;
import com.googlecode.deadalus.geoutils.LengthUnit;
import com.googlecode.deadalus.events.*;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import ch.hsr.geohash.GeoHash;

/**
 * Implememtation of a RegionServer that is running in the local JVM.
 *
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class LocalRegionServer implements RegionServer {
    /** The hash that defines this server */
    private final GeoHash geoHash;
    /** The queue that holds the events that still need to be processed */
    private final BlockingQueue<LocalEvent> eventQueue = new LinkedBlockingQueue<LocalEvent>();
    /** The map of LocalObject instances, can be empty if all regions are handled by other RegionServers */
    private final Map<UUID,LocalObject> managedObjects = new ConcurrentHashMap<UUID,LocalObject>();
    /** List of all RegionServer instances that are the children of this RegionServer (i.e. the precision is > this.GeoHash.precision)*/
    private final Map<GeoHash,RegionServer> managedServers = new ConcurrentHashMap<GeoHash,RegionServer>();
    /** The registry to be used to find other RegionServers that are not directly children of this RegionServer */
    private RegionServerRegistry regionServerRegistry;
    /** The registry of ObjectFactory instances, this maps ClassId UUID's to ObjectFactories that can be used to create object of that class */
    private ObjectFactoryRegistry objectFactoryRegistry;


    public LocalRegionServer(GeoHash geoHash) {
        this.geoHash = geoHash;
    }

    public void setRegionServerRegistry(RegionServerRegistry regionServerRegistry) {
        this.regionServerRegistry = regionServerRegistry;
    }

    public void setObjectFactoryRegistry(ObjectFactoryRegistry objectFactoryRegistry) {
        this.objectFactoryRegistry = objectFactoryRegistry;
    }

    public final GeoHash getGeoHash() {
        return geoHash;
    }

    @Override
    public void broadCast(Event event) {
        // add an event for each UUID that is locally managed and broadcast the event to other RegionServer instances
        for (UUID uuid : managedObjects.keySet()) {
            sendLocal(event,uuid,null,true);
        }
        for (RegionServer managedServer : managedServers.values()) {
            managedServer.broadCast(event);
        }
    }

    @Override
    public void broadCast(Event event, double radius, LengthUnit unit) {
        // first check to see if this is at all for us
        if(event.getOriginatingLocation().getGeoHash().within(this.geoHash)) {
            // ok we have the hash now we need to find out if it is managed by one of the Region Server below us
            for (RegionServer managedServer : managedServers.values()) {
                if(event.getOriginatingLocation().getGeoHash().within(managedServer.getGeoHash())) {
                    // pass it to the other server
                    managedServer.broadCast(event,radius,unit);
                    return;
                }
            }
            // ok so we should handle the event ourselves
            // now go through all the SpatialObject instances
            for (SpatialObject object : managedObjects.values()) {
                // now use the distance function
                if(object.getCurrentLocation().distance(event.getOriginatingLocation(),unit) < radius) {
                    // bingo!
                    sendLocal(event,object.getId(),null,false);
                }
            }
        }
    }

    @Override
    public void send(Event event, UUID recipientId, EventCallback eventCallback) {
        // first check if we manage UUID ourselves
        if(managedObjects.containsKey(recipientId)) {
            sendLocal(event,recipientId,eventCallback,false);
        } else {
            // now we need to find it, we just send the event to the other servers and have them figure it out
            for (RegionServer managedServer : managedServers.values()) {
                managedServer.send(event,recipientId,eventCallback);
            }
        }

    }

    private void sendLocal(Event event, UUID recipientId, EventCallback eventCallback,boolean broadcast) {
        eventQueue.offer(new LocalEvent(event,recipientId,eventCallback,broadcast));
    }

    @Override
    public Collection<RegionServer> getRegions() {
        return Collections.unmodifiableCollection(managedServers.values());
    }

    @Override
    public SpatialObject createObject(UUID clsId, Coordinate initialLocation, Object... arguments) {
        ObjectFactory factory = objectFactoryRegistry.getObjectFactory(clsId);
        SpatialObject object = factory.createObject(arguments);
        LocalObject localObject = new LocalObject(object);
        localObject.setCurrentLocation(initialLocation);
        // @todo: need to know the UUID of the creator here. Get this from some kind of security context
        broadCast(new CreateEvent(object.getId(),initialLocation,null));
        return object;
    }

    @Override
    public void moveObject(UUID objectId,Coordinate toLocation) {
        if(managedObjects.containsKey(objectId)) {
            // update the object and create events
            LocalObject localObject = managedObjects.get(objectId);
            Coordinate fromLocation = localObject.getCurrentLocation();
            broadCast(new LeaveEvent(objectId,localObject.getCurrentLocation(),toLocation));
            localObject.setCurrentLocation(toLocation);
            broadCast(new EnterEvent(objectId,fromLocation,toLocation));
        } else if(toLocation.getGeoHash().within(this.geoHash)) {
            // try to find the child region server that manages the object
            // @todo: does this do us any good or shall we go to the RegionServerRegistry immediately?

        } else { // we were asked to move an object that was somewhere else entirely
            // @todo: do we want to handle this or do we consider this to be an error?
        }
    }
}
