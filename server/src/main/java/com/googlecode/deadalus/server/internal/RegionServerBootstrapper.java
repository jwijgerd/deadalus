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

import com.googlecode.deadalus.geoutils.GeoHash;

import java.io.File;

/**
 * Helper class to bootstrap RegionServer instances on startup
 *
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class RegionServerBootstrapper {
    private GeoHash localRoot;
    private File snapshotDirectory;

    public void initilize() {

    }

    public void destoy() {

    }

    public void setLocalRoot(String geoHash) {
        this.localRoot = GeoHash.fromGeohashString(geoHash);
    }

    public void setSnapshotDirectory(File snapshotDirectory) {
        this.snapshotDirectory = snapshotDirectory;
    }
}
