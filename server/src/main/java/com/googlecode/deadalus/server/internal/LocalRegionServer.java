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

import com.googlecode.deadalus.RegionServer;
import com.googlecode.deadalus.SpatialObject;
import com.googlecode.deadalus.Coordinate;
import com.googlecode.deadalus.events.Event;
import com.googlecode.deadalus.events.EventCallback;

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
    /** The queue that holds the events that still need to be processed */
    private final BlockingQueue<LocalEvent> eventQueue = new LinkedBlockingQueue<LocalEvent>();
    /** The map of SpatialObject instances, can be empty if all regions are handled by other RegionServers */
    private final Map<UUID,SpatialObject> managedObjects = new ConcurrentHashMap<UUID,SpatialObject>();

    private final List<RegionServer> managedServers = new CopyOnWriteArrayList<RegionServer>();

    @Override
    public void broadCast(Event event) {
        // add an event for each UUID that is locally managed and broadcast the event to other RegionServer instances
        for (UUID uuid : managedObjects.keySet()) {
            sendLocal(event,uuid,null,true);
        }
        for (RegionServer managedServer : managedServers) {
            managedServer.broadCast(event);
        }
    }

    @Override
    public void send(Event event, UUID recipientId, EventCallback eventCallback) {
        // first check if we manage UUID ourselves
        if(managedObjects.containsKey(recipientId)) {
            sendLocal(event,recipientId,eventCallback,false);
        } else {
            // now we need to find it, we just send the event to the other servers and have them figure it out
            for (RegionServer managedServer : managedServers) {
                managedServer.send(event,recipientId,eventCallback);
            }
        }

    }

    private void sendLocal(Event event, UUID recipientId, EventCallback eventCallback,boolean broadcast) {
        eventQueue.offer(new LocalEvent(event,recipientId,eventCallback,broadcast));
    }

    @Override
    public Collection<SpatialObject> getAllObjects() {
        return Collections.unmodifiableCollection(managedObjects.values());
    }

    @Override
    public Collection<RegionServer> getRegions() {
        return Collections.unmodifiableCollection(managedServers);
    }

    @Override
    public SpatialObject createObject(UUID clsId, Coordinate initialLocation, Object... arguments) {
        return null;
    }
}
