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

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class LocalRegionServerRegistry implements RegionServerRegistry {
    @Override
    public RegionServer findByObjectId(UUID objectId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public RegionServer findByCoordinate(Coordinate coordinate) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public RegionServer findByGeoHash(GeoHash geoHash) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
