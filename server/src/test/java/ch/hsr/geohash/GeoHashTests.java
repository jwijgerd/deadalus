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
import com.googlecode.deadalus.Coordinate;
import com.googlecode.deadalus.geoutils.LengthUnit;
import com.googlecode.deadalus.geoutils.GeoHash;

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
        GeoHash.Point[] bbox = b.getBoundingBoxPoints();
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
        GeoHash world = GeoHash.fromGeohashString("");
        bbox = world.getBoundingBoxPoints();
        System.out.print("world left-top");
        System.out.println(bbox[0]);
        System.out.print("world right-bottom");
        System.out.println(bbox[1]);
    }

    @Test
    public void testBase32() {
        GeoHash b = GeoHash.fromGeohashString("b1c");
        Assert.assertEquals("b1c",b.getHash());
        Assert.assertEquals(b.getPrecision(),3);
        GeoHash c = GeoHash.fromGeohashString("b1c2f");
        Assert.assertEquals("b1c2f",c.getHash());
        Assert.assertEquals(c.getPrecision(),5);
        Assert.assertEquals(GeoHash.fromGeohashString("").getPrecision(),0);
    }

    @Test
    public void testBoxSize() {
        GeoHash gh = GeoHash.withCharacterPrecision(52.376817d,4.896619d,12);
        String gh12 = gh.getHash();
        int i = 12;
        while(i >= 0) {
            String gh11 = gh12.substring(0,i);
            Assert.assertEquals(GeoHash.fromGeohashString(gh11).getPrecision(),i);
            System.out.println(gh11);
            GeoHash.Point[] bbox = GeoHash.fromGeohashString(gh11).getBoundingBoxPoints();
            Coordinate from = new Coordinate(bbox[0].getLatitude(),bbox[0].getLongitude());
            Coordinate to = new Coordinate(bbox[1].getLatitude(),bbox[1].getLongitude());
            System.out.println("dist = "+from.distance(to,LengthUnit.METRES));
            i-=1;
        }
    }

    @Test
    public void testDistance() {
        GeoHash gh = GeoHash.withCharacterPrecision(52.376817d,4.896619d,12);
        GeoHash gh2 = GeoHash.withCharacterPrecision(52.373936d,4.908464d,12);
        Coordinate from = new Coordinate(gh.getPoint().getLatitude(),gh.getPoint().getLongitude());
        Coordinate to = new Coordinate(gh2.getPoint().getLatitude(),gh2.getPoint().getLongitude());
        long start = System.nanoTime();
        for(int i=0; i<1000; i++) {
            from.distance(to,LengthUnit.KILOMETRES);
        }
        long total = System.nanoTime() - start;
        System.out.println("1000 calculations in "+total+" nanoSecs");
    }

    @Test
    public void testPrecision() {
        GeoHash gh = GeoHash.fromGeohashString("bm1");
        Assert.assertEquals(gh.getPrecision(),3);

    }
}
