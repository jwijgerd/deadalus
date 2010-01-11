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

import com.googlecode.deadalus.geoutils.GeoHash;

import java.util.UUID;

/**
 * Interface to a registry to locate RegionServer instances for specific Coordinate, GeoHash or UUID instances
 *
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public interface RegionServerRegistry {
    /**
     * Find the RegionServer that manages the object with the given UUID
     *
     * @param objectId
     * @return
     */
    RegionServer findByObjectId(UUID objectId);

    /**
     * Find the region that has the coordinate (each coordinate is ultimately managed by only one region server)
     *
     * @param coordinate
     * @return
     */
    RegionServer findByCoordinate(Coordinate coordinate);

    /**
     * Find the RegionServer that manages this GeoHash. The type of Region depends on the precision of the GeoHash (i.e.
     * the number of characters)
     *
     * @param geoHash
     * @return
     */
    RegionServer findByGeoHash(GeoHash geoHash);

    /**
     * Register a server
     * 
     * @param server
     */
    void register(RegionServer server);
}
