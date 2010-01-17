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

package com.googlecode.deadalus.integrations.foursquare;

import com.googlecode.deadalus.DeadalusUser;
import com.googlecode.deadalus.Coordinate;

import java.util.Date;

/**
 * Desribes an update on the Location coming from an external system
 *
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class LocationUpdate {
    private final DeadalusUser user;
    private final Coordinate newLocation;
    private final Date timeOfUpdate;

    public LocationUpdate(DeadalusUser user, Coordinate newLocation, Date timeOfUpdate) {
        this.user = user;
        this.newLocation = newLocation;
        this.timeOfUpdate = timeOfUpdate;
    }

    public final DeadalusUser getUser() {
        return user;
    }

    public final Coordinate getNewLocation() {
        return newLocation;
    }

    public final Date getTimeOfUpdate() {
        return timeOfUpdate;
    }
}
