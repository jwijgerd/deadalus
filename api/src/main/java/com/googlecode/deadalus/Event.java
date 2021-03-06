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

import com.googlecode.deadalus.Coordinate;

import java.util.UUID;
import java.io.Serializable;

/**
 * Describes an Event within the Deadalus application
 */
public interface Event<PayloadType extends Serializable> extends Serializable {
    /**
     *
     * @return  the type of this event
     */
    String getType();

    /**
     * Time this event was generated.
     * Timestamp is in milliseconds (UTC timezone) since 1970
     *
     * @return
     */
    Long getTimestamp();

    /**
     * The Coordinate this event was generated at. In the Deadalus system all events originate from somewhere
     *
     * @return
     */
    Coordinate getOriginatingLocation();

    /**
     * The UUID of the SpatialObject the generated this event
     *
     * @return
     */
    UUID getOriginationObjectId();

    /**
     *
     * @return  the event payload, this is application specific
     */
    PayloadType getPayload();
}
