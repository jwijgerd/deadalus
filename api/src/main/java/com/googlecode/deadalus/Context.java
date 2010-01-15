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

import com.googlecode.deadalus.geoutils.LengthUnit;

import java.util.Date;
import java.util.UUID;

/**
 * The Context class should be used by SpatialObject implementations to interact with the Runtime environment. When a
 * SpatialObject is created (either from scratch or after deserailisation) the Context will be set via the setContext
 * method.
 *
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public interface Context {
    /**
     * @return The current location (i.e. last known) of this object
     */
    Coordinate getCurrentLocation();
    /**
     * The last time the coordinate was updated, this can be used to determine the chance that the object is stale (i.e.
     * if an object it tied to the current position of a Mobile Phone and this position has not been updated in a
     * while then chances are high this user is not currently at the position)
     *
     * @return  the date and time when the last update to the location was made
     */
    Date getLastUpdated();
    /**
     * Broadcast an event to all objects within the specified radius. The radius is calculated from the originatingLocation
     * of the event
     *
     * @param event
     * @param radius
     * @param unit
     */
    void broadCast(Event event,double radius, LengthUnit unit);
    /**
     * Send an event directly to a recipient identified by the UUID
     *
     * @param event
     * @param recipientId
     */
    void send(Event event, UUID recipientId, EventCallback eventCallback);
}
