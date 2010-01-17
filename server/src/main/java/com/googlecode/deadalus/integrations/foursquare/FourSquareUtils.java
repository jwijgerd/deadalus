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

import com.googlecode.deadalus.Coordinate;

import java.util.Map;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class FourSquareUtils {
    public static Coordinate venueToCoordinate(Map<String,Object> venue) {
        if(venue != null) {
            // got the venue, now get the lat / lon info
            double lat = (Double)venue.get("geolat");
            double lon = (Double)venue.get("geolong");
            return new Coordinate(lat,lon);
        } else {
            // this was only a shout, try the next one
            return null;
        }
    }
}
