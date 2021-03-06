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

import java.util.UUID;

/**
 * Leave a location (coordinate)
 *
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class LeaveEvent extends MoveEvent {

    public LeaveEvent(UUID subject, Coordinate from, Coordinate to) {
        super(from, to, subject);
    }

    @Override
    public final String getType() {
        return "leave";
    }

}
