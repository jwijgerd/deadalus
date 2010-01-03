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

import java.util.UUID;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Implememtation of a RegionServer that is running in the local JVM.
 *
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class LocalRegionServer implements RegionServer {
    /** The queue that holds the events that still need to be processed */
    private final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>();

    @Override
    public void broadCast(Event event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void send(Event event, UUID recipientId, EventCallback eventCallback) {

    }

    @Override
    public Collection<SpatialObject> getAllObjects() {
        return null;
    }

    @Override
    public Collection<RegionServer> getRegions() {
        return null;
    }

    @Override
    public SpatialObject createObject(UUID clsId, Coordinate initialLocation, Object... arguments) {
        return null;
    }
}
