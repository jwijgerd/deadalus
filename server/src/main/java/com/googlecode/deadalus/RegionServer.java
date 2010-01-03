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

package com.googlecode.deadalus;

import com.googlecode.deadalus.events.Event;
import com.googlecode.deadalus.events.EventCallback;
import com.googlecode.deadalus.Coordinate;

import java.util.Collection;
import java.util.UUID;

/**
 * A Region server contains SpatialObject instances but can also contain other RegionServer instances. These instances
 * are always within the boundary of the parent RegionServer.
 *
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public interface RegionServer {
    /**
     * Broadcast an event to all objects within this RegionServer
     *
     * @param event
     */
    void broadCast(Event event);

    /**
     * Send an event directly to a recipient identified by the UUID
     *
     * @param event
     * @param recipientId
     */
    void send(Event event, UUID recipientId, EventCallback eventCallback);

    /**
     * @return All objects directly managed by this RegionServer
     */
    Collection<SpatialObject> getAllObjects();

    /**
     * @return All child RegionServer
     */
    Collection<RegionServer> getRegions();

    /**
     * Create a new object withing this
     *
     * @param clsId
     * @param initialLocation
     * @param arguments
     * @return
     */
    SpatialObject createObject(UUID clsId, Coordinate initialLocation, Object... arguments);
}