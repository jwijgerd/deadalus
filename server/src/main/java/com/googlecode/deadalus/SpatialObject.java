/*
 * Copyright 2009 Joost van de Wijgerd
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
import com.googlecode.deadalus.geoutils.Coordinate;

import java.util.UUID;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public interface SpatialObject {
    /**
     * @return The globally unique identifier of this object instance
     */
    UUID getId();

    /**
     * @return The class id of this object
     */
    UUID getClsId();

    /**
     * @return The current location (i.e. last known) of this object
     */
    Coordinate getCurrentLocation();

    /**
     * Event handling method called by the system
     *
     * @param event
     */
    void onEvent(Event event);
}
