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

import com.googlecode.deadalus.*;
import com.googlecode.deadalus.geoutils.LengthUnit;

import java.util.UUID;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class LocalObject implements DeadalusObject, Context {
    private transient final LocalRegionServer regionServer;
    private final DeadalusObject owner = null; // @todo: set the owner 
    private final DeadalusObject delegate;
    private final AtomicReference<Coordinate> currentLocation = new AtomicReference<Coordinate>(null);
    private final AtomicReference<Date> lastUpdated = new AtomicReference<Date>(null);

    public LocalObject(LocalRegionServer regionServer, DeadalusObject delegate) {
        this.regionServer = regionServer;
        this.delegate = delegate;
        this.delegate.setContext(this);
    }

    @Override
    public final UUID getId() {
        return delegate.getId();
    }

    @Override
    public final UUID getClsId() {
        return delegate.getClsId();
    }

    @Override
    public final void setContext(Context context) {
        // do nothing
    }

    @Override
    public final Coordinate getCurrentLocation() {
        return currentLocation.get();
    }

    @Override
    public final Date getLastUpdated() {
        return lastUpdated.get();
    }

    @Override
    public final void onEvent(Event event) {
        // @todo: add some management code here: check invocations, time spend etc
        delegate.onEvent(event);
    }

    @Override
    public final void broadCast(Event event, double radius, LengthUnit unit) {
        // @todo: maybe add some statistics here
        regionServer.broadCast(event,radius,unit);
    }

    @Override
    public final void send(Event event, UUID recipientId, EventCallback eventCallback) {
        // @todo: maybe add some statistics here
        regionServer.send(event,recipientId,eventCallback);
    }

    @Override
    public final DeadalusUser getOwner() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    // manipulate the internal properties
    protected final Coordinate setCurrentLocation(Coordinate newLocation) {
        lastUpdated.set(new Date());
        return currentLocation.getAndSet(newLocation);
    }
}
