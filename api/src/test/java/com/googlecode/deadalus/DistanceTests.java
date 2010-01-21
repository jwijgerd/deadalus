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

import org.testng.annotations.Test;
import com.googlecode.deadalus.geoutils.LengthUnit;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class DistanceTests {
    @Test
    public void testDistance() {
        Coordinate one = new Coordinate(52.3777634,4.869634);
        Coordinate two = new Coordinate(52.3777634,4.969634);
        System.out.println(one.distance(two, LengthUnit.KILOMETRES));
    }
}
