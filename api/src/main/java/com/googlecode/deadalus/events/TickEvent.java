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
 * Tick or HeartBeat event. Objects that want to run some logic periodically can listen to this event and execute
 * their logic in the onEvent handler;
 *
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class TickEvent implements Event<TickEvent.TickInterval> {
    private final Long timeStamp;
    private final TickInterval interval;

    public TickEvent(long interval) {
        this.timeStamp = System.currentTimeMillis();
        this.interval = new TickInterval(interval);
    }

    @Override
    public final String getType() {
        return "tick";
    }

    @Override
    public final Long getTimestamp() {
        return timeStamp;
    }

    @Override
    public final Coordinate getOriginatingLocation() {
        return null;
    }

    @Override
    public final UUID getOriginationObjectId() {
        return null;
    }

    @Override
    public final TickInterval getPayload() {
        return this.interval;
    }

    public final class TickInterval {
        private final long interval;

        private TickInterval(long interval) {
            this.interval = interval;
        }

        /**
         *
         * @return  The time to the next tick, can be negative! 
         */
        public final long getTimeToNextTick() {
            return (timeStamp + interval) - System.currentTimeMillis();
        }
    }
}
