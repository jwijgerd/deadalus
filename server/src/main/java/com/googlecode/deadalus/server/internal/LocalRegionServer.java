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
import com.googlecode.deadalus.geoutils.GeoHash;
import com.googlecode.deadalus.events.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Configurable;

/**
 * Implememtation of a RegionServer that is running in the local JVM.
 *
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
@Configurable()
public class LocalRegionServer implements RegionServer {
    /** default maximum number of workers to run, set to the number of processors (cores) + 1 */
    private static final int MAX_WORKERS = Runtime.getRuntime().availableProcessors()+1;
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
    /** ExecutorService to execute the workers in */
    private ExecutorService executorService;
    /** The list of EventWorker instances that handle the events, this contains both running and stopped instances */
    private final List<EventWorker> workers = new ArrayList<EventWorker>();


    public LocalRegionServer(GeoHash geoHash) {
        this.geoHash = geoHash;
    }

    public final void start() {
        // create the workers
        for(int i=0; i<MAX_WORKERS; i++) {
            workers.add(new EventWorker());
        }
        // @todo: register with RegionServerRegistry?

        // we're ready for action
    }

    public final void stop() {
        // stop all workers
        for (EventWorker worker : workers) {
            worker.stop();
        }

        // we're done here
    }

    public void setRegionServerRegistry(RegionServerRegistry regionServerRegistry) {
        this.regionServerRegistry = regionServerRegistry;
    }

    public void setObjectFactoryRegistry(ObjectFactoryRegistry objectFactoryRegistry) {
        this.objectFactoryRegistry = objectFactoryRegistry;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
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
        ensureWorkers();
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

    protected final void handleLocalEvent(final LocalEvent localEvent) {
        // first find the object
        LocalObject localObject = managedObjects.get(localEvent.getRecipient());
        // can be null if this object was recently removed from this server!
        if(localObject != null) {
            // @todo: check if local object still has quota, for now just call the onEvent method
            localObject.onEvent(localEvent.getEvent());
        } else {
            // @todo: handle events for objects that are not here anymore
        }
    }

    /**
     * Ensures there are enough running workers to handle
     */
    private void ensureWorkers() {
        // check the eventQueue
        if(eventQueue.size() > 0) {
            // start a worker if possible
            for (EventWorker worker : workers) {
                if(!worker.isRunning()) {
                    executorService.submit(worker);
                }
            }
        }
    }

    private final class EventWorker implements Callable<LocalEvent> {
        private final AtomicReference<Thread> runningThread = new AtomicReference<Thread>(null);
        private volatile boolean running = true;

        /**
         * Handles LocalEvent instances from the eventQueue. A LocalEvent is always intended for an object that is
         * managed by this instance of the server.
         *
         * @return
         * @throws Exception
         */
        @Override
        public LocalEvent call() throws Exception {
            // store the calling thread to be able to interrupt later.
            if(!runningThread.compareAndSet(null,Thread.currentThread())) {
                // somehow this worker was executed before it was finished
                // @todo: do we throw an exception or ?
            }
            while(running) {
                LocalEvent localEvent = null;
                try {
                    localEvent = eventQueue.poll(60, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    // this will probably mean we interrupted the thread so running will become false
                    localEvent = null;
                }
                if(localEvent == null) { // there were no events for 60 seconds, die
                    running = false;
                } else {
                    // handle the event...
                    handleLocalEvent(localEvent);
                }
            }
            runningThread.compareAndSet(Thread.currentThread(),null);
            return null;
        }

        private boolean isRunning() {
            return this.runningThread.get() != null;
        }

        private void stop() {
            if(this.runningThread.get() != null) this.runningThread.get().interrupt();
        }
    }
}
