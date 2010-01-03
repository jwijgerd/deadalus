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

import com.googlecode.deadalus.events.Event;
import com.googlecode.deadalus.events.EventCallback;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class LocalEvent implements Serializable {
    private static final AtomicLong sequence = new AtomicLong(0);
    private final Event event;
    private final UUID recipient;
    private final EventCallback callback;
    private final boolean broadcast;
    private final long seqNum = sequence.incrementAndGet();

    public LocalEvent(Event event, UUID recipient, EventCallback callback) {
        this(event,recipient,callback,false);
    }

    public LocalEvent(Event event, UUID recipient, EventCallback callback, boolean broadcast) {
        this.event = event;
        this.recipient = recipient;
        this.callback = callback;
        this.broadcast = broadcast;
    }
}
