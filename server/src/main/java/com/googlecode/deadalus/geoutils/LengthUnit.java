/*
 * Copyright 2009 Joost van de Wijgerd
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

package com.googlecode.deadalus.geoutils;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public enum LengthUnit {
    MILES {
        public double toMiles(double l) {
            return l;
        }
        public double toKilometres(double l) {
            return l * 1.609344d;
        }
        public double toNauticalMiles(double l) {
            return l * 0.868976242d;
        }
        public double convert(double l, LengthUnit u) {
            return u.toMiles(l);
        }
    },

    KILOMETRES {
        public double toMiles(double l) {
            return l * 0.621371192d;
        }
        public double toKilometres(double l) {
            return l;
        }
        public double toNauticalMiles(double l) {
            return l * 0.539956803d;
        }
        public double convert(double l, LengthUnit u) {
            return u.toKilometres(l);
        }
    },

    NAUTICAL_MILES {
        public double toMiles(double l) {
            return l * 1.15077945d;
        }
        public double toKilometres(double l) {
            return l * 1.85200d;
        }
        public double toNauticalMiles(double l) {
            return l;
        }
        public double convert(double l, LengthUnit u) {
            return u.toNauticalMiles(l);
        }
    };

    public double convert(double originalLength, LengthUnit sourceUnit) {
        throw new AbstractMethodError();
    }

    public double toMiles(double length) {
        throw new AbstractMethodError();
    }

    public double toKilometres(double length) {
        throw new AbstractMethodError();
    }

    public double toNauticalMiles(double length) {
        throw new AbstractMethodError();
    }


}
