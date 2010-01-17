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

import com.googlecode.deadalus.RegionServerRegistry;
import com.googlecode.deadalus.RegionServer;
import com.googlecode.deadalus.Coordinate;
import com.googlecode.deadalus.geoutils.GeoHash;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class LocalRegionServerRegistry implements RegionServerRegistry {
    private final ConcurrentHashMap<GeoHash,RegionServer> registeredServers = new ConcurrentHashMap<GeoHash,RegionServer>();

    public final void init() {
        // nothing to do
    }

    public final void destroy() {
        // stop all registered servers
        for (Map.Entry<GeoHash, RegionServer> regionServerEntry : registeredServers.entrySet()) {
            if(regionServerEntry.getValue() instanceof LocalRegionServer) {
                ((LocalRegionServer)regionServerEntry.getValue()).stop();
            }
        }
    }

    @Override
    public final RegionServer findByObjectId(UUID objectId) {
        // @todo: need some way to query the cluster
        return null;
    }

    @Override
    public final RegionServer findByCoordinate(Coordinate coordinate) {
        return findByGeoHash(coordinate.getGeoHash());
    }

    @Override
    public final RegionServer findByGeoHash(GeoHash geoHash) {
        for (Map.Entry<GeoHash, RegionServer> entry : registeredServers.entrySet()) {
            if(geoHash.within(entry.getKey())) return entry.getValue();
        }
        // @todo: this is not possible
        return null;
    }

    @Override
    public final void register(RegionServer server) {
        // @todo: notify cluster of new member
        registeredServers.put(server.getGeoHash(),server);
    }
}
