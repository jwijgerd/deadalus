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

import com.googlecode.deadalus.SpatialObject;
import com.googlecode.deadalus.Coordinate;
import com.googlecode.deadalus.Event;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class LocalObject implements SpatialObject {
    private final SpatialObject delegate;
    private final AtomicReference<Coordinate> currentLocation = new AtomicReference<Coordinate>(null);

    public LocalObject(SpatialObject delegate) {
        this.delegate = delegate;
    }

    @Override
    public UUID getId() {
        return delegate.getId();
    }

    @Override
    public UUID getClsId() {
        return delegate.getClsId();
    }

    @Override
    public Coordinate getCurrentLocation() {
        return currentLocation.get();
    }

    @Override
    public void onEvent(Event event) {
        // @todo: add some management code here: check invocations, time spend etc
        delegate.onEvent(event);
    }

    // manipulate the internal properties
    Coordinate setCurrentLocation(Coordinate newLocation) {
        return currentLocation.getAndSet(newLocation);
    }
}
