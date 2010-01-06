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
    private static final double EPSILON = 1e-12d;
	private static final double equatorRadius = 6378137d;
    private static final double poleRadius = 6356752.3142d;
	private static final double f = 1 / 298.257223563d;
	private static final double degToRad = 0.0174532925199433d;
	private static final double equatorRadiusSquared = equatorRadius * equatorRadius;
	private static final double	poleRadiusSquared = poleRadius * poleRadius;
    private final double latitude;
    private final double longitude;
    private final GeoHash geoHash;

    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.geoHash = GeoHash.withCharacterPrecision(this.latitude, this.longitude,12);
    }

    public final double getLatitude() {
        return latitude;
    }

    public final double getLongitude() {
        return longitude;
    }

    public final GeoHash getGeoHash() {
        return geoHash;
    }

    public final double distance(Coordinate to, LengthUnit unit) {
        double L = (to.longitude - this.longitude) * degToRad;
		double U1 = Math.atan((1 - f) * Math.tan(this.latitude * degToRad));
		double U2 = Math.atan((1 - f) * Math.tan(to.latitude * degToRad));
		double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
		double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

		double cosSqAlpha, sinSigma, cos2SigmaM, cosSigma, sigma;

		double lambda = L, lambdaP, iterLimit = 20;
		do {
			double sinLambda = Math.sin(lambda), cosLambda = Math.cos(lambda);
			sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda)
					+ (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda)
					* (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
			if (sinSigma == 0)
				return 0; // co-incident points
			cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
			sigma = Math.atan2(sinSigma, cosSigma);
			double sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
			cosSqAlpha = 1 - sinAlpha * sinAlpha;
			cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
			if (cos2SigmaM == Double.NaN)
				cos2SigmaM = 0; // equatorial line: cosSqAlpha=0
			double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
			lambdaP = lambda;
			lambda = L
					+ (1 - C)
					* f
					* sinAlpha
					* (sigma + C
							* sinSigma
							* (cos2SigmaM + C * cosSigma
									* (-1 + 2 * cos2SigmaM * cos2SigmaM)));
		} while (Math.abs(lambda - lambdaP) > EPSILON && --iterLimit > 0);

		if (iterLimit == 0)
			return Double.NaN;
		double uSquared = cosSqAlpha * (equatorRadius * equatorRadius - poleRadius * poleRadius) / poleRadiusSquared;
		double A = 1
				+ uSquared
				/ 16384
				* (4096 + uSquared * (-768 + uSquared * (320 - 175 * uSquared)));
		double B = uSquared / 1024
				* (256 + uSquared * (-128 + uSquared * (74 - 47 * uSquared)));
		double deltaSigma = B
				* sinSigma
				* (cos2SigmaM + B
						/ 4
						* (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B
								/ 6 * cos2SigmaM
								* (-3 + 4 * sinSigma * sinSigma)
								* (-3 + 4 * cos2SigmaM * cos2SigmaM)));
        // KM
		double dist =  (poleRadius * A * (sigma - deltaSigma)) / 1000d;
        return unit.convert(dist,LengthUnit.KILOMETRES);
    }



    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (!geoHash.equals(that.geoHash)) return false;
        return latitude == that.latitude && longitude == that.longitude;

    }

    @Override
    public final int hashCode() {
        return geoHash.hashCode();
    }
}
