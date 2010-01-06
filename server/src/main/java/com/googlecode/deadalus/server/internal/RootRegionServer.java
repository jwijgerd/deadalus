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

import com.googlecode.deadalus.RegionServer;
import com.googlecode.deadalus.SpatialObject;
import com.googlecode.deadalus.Coordinate;
import com.googlecode.deadalus.geoutils.LengthUnit;
import com.googlecode.deadalus.events.Event;
import com.googlecode.deadalus.events.EventCallback;

import java.util.UUID;
import java.util.Collection;
import java.util.Collections;

import ch.hsr.geohash.GeoHash;

/**
 * The Root Region Server a.k.a. The World. This is a special RegionServer that provides access to all other
 * Region Servers. It basically keeps an index of all available servers
 *
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class RootRegionServer implements RegionServer {
    private static final GeoHash WORLD = GeoHash.fromGeohashString("");

    @Override
    public GeoHash getGeoHash() {
        return WORLD;
    }

    @Override
    public void broadCast(Event event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void broadCast(Event event, double radius, LengthUnit unit) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void send(Event event, UUID recipientId, EventCallback eventCallback) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<RegionServer> getRegions() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SpatialObject createObject(UUID clsId, Coordinate initialLocation, Object... arguments) {
        // find the correct regionserver and create the object
        return null;
    }

    @Override
    public void moveObject(UUID objectId, Coordinate toLocation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
