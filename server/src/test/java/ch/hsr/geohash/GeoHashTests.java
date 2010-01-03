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

package ch.hsr.geohash;

import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class GeoHashTests {
    @Test
    public void testBoundingBoxes() {
        GeoHash b = GeoHash.fromGeohashString("b");
        GeoHash b1 = GeoHash.fromGeohashString("b1");
        Assert.assertTrue(b1.within(b));
        Assert.assertFalse(b.within(b1));
        WGS84Point[] bbox = b.getBoundingBoxPoints();
        Assert.assertEquals(bbox.length,2);
        System.out.print("b left-top");
        System.out.println(bbox[0]);
        System.out.print("b right-bottom");
        System.out.println(bbox[1]);

        bbox = b1.getBoundingBoxPoints();
        System.out.print("b1 left-top");
        System.out.println(bbox[0]);
        System.out.print("b1 right-bottom");
        System.out.println(bbox[1]);

    }
}
