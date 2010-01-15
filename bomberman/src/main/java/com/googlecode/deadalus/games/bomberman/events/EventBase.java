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

package com.googlecode.deadalus.games.bomberman.events;

import com.googlecode.deadalus.Coordinate;

import java.util.UUID;

/**
 * Abstract base class for all events
 *
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public abstract class EventBase {
    protected final Long timeStamp;
    protected final Coordinate location;
    protected final UUID creator;

    public EventBase(Coordinate location, UUID creator) {
        this.location = location;
        this.creator = creator;
        timeStamp = System.currentTimeMillis();
    }

    public final Long getTimestamp() {
        return timeStamp;
    }

    public final Coordinate getOriginatingLocation() {
        return location;
    }

    public final UUID getOriginationObjectId() {
        return creator;
    }
}
