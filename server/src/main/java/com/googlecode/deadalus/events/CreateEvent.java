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

package com.googlecode.deadalus.events;

import com.googlecode.deadalus.Coordinate;
import com.googlecode.deadalus.Event;

import java.util.UUID;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class CreateEvent implements Event<UUID> {
    private final Long timeStamp;
    private final Coordinate location;
    private final UUID createdObject;
    private final UUID creator;

    public CreateEvent(UUID createdObject, Coordinate location, UUID creator) {
        this.createdObject = createdObject;
        this.location = location;
        this.creator = creator;
        this.timeStamp = System.currentTimeMillis();
    }

    @Override
    public String getType() {
        return "create";
    }

    @Override
    public Long getTimestamp() {
        return timeStamp;
    }

    @Override
    public Coordinate getOriginatingLocation() {
        return location;
    }

    @Override
    public UUID getOriginationObjectId() {
        return createdObject;
    }

    @Override
    public UUID getPayload() {
        return creator;
    }
}
