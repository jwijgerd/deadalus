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

import com.googlecode.deadalus.geoutils.LengthUnit;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import ch.hsr.geohash.GeoHash;

/**
 * Describes a Coordinate somewhere on earth. Immutable object
 */
public final class Coordinate {
    private static final MathContext LATLON_CONTEXT = new MathContext(8, RoundingMode.HALF_UP);
    private static final BigDecimal PI = new BigDecimal(Math.PI);
    private static final BigDecimal ONEHUNDREDANDEIGHTY = new BigDecimal(180.0d);
    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final GeoHash geoHash;

    public Coordinate(double latitude, double longitude) {
        this.latitude = new BigDecimal(latitude, LATLON_CONTEXT);
        this.longitude = new BigDecimal(longitude, LATLON_CONTEXT);
        this.geoHash = GeoHash.withCharacterPrecision(this.latitude.doubleValue(), this.longitude.doubleValue(),12);
    }

    public final double getLatitude() {
        return latitude.doubleValue();
    }

    public final double getLongitude() {
        return longitude.doubleValue();
    }

    public final String getGeoHash() {
        return geoHash.toBase32();
    }

    public final double distance(Coordinate to, LengthUnit unit) {
        final BigDecimal theta = this.longitude.subtract(this.latitude);
        double dist = Math.sin(deg2rad(this.latitude)) * Math.sin(deg2rad(to.latitude)) + Math.cos(deg2rad(this.latitude)) * Math.cos(deg2rad(to.latitude)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        // nautical miles
        dist = dist * 60;
        return unit.convert(dist, LengthUnit.NAUTICAL_MILES);
    }

    private static double deg2rad(BigDecimal deg) {
        return deg.multiply(PI).divide(ONEHUNDREDANDEIGHTY).doubleValue();
    }

    private static double rad2deg(BigDecimal rad) {
        return rad.multiply(ONEHUNDREDANDEIGHTY).divide(PI).doubleValue();
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0d) / Math.PI;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (!geoHash.equals(that.geoHash)) return false;
        if (!latitude.equals(that.latitude)) return false;
        if (!longitude.equals(that.longitude)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return geoHash.hashCode();
    }
}
