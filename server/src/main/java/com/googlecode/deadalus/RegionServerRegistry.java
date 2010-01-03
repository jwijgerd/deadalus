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

import ch.hsr.geohash.GeoHash;

import java.util.UUID;

/**
 * Interface to a registry to locate RegionServer instances for specific Coordinate, GeoHash or UUID instances
 *
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public interface RegionServerRegistry {
    RegionServer findByObjectId(UUID objectId);
    RegionServer findByCoordinate(Coordinate coord);
    RegionServer findByGeoHash(GeoHash geoHash);
}
