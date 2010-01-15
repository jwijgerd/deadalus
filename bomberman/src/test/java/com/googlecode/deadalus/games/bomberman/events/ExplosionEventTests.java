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

package com.googlecode.deadalus.games.bomberman.events;

import org.testng.annotations.Test;
import org.testng.Assert;

import java.util.UUID;

import com.googlecode.deadalus.Coordinate;
import com.googlecode.deadalus.geoutils.LengthUnit;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class ExplosionEventTests {
    @Test
    public void testDamageFunction() {
        int damage = ExplosionEvent.MEDIUMBLAST.calculateDamage(10d, LengthUnit.METRES);
        Assert.assertEquals(damage,80);
        damage = ExplosionEvent.MEDIUMBLAST.calculateDamage(20d, LengthUnit.METRES);
        Assert.assertEquals(damage,60);
        damage = ExplosionEvent.MEDIUMBLAST.calculateDamage(45d, LengthUnit.METRES);
        Assert.assertEquals(damage,10);
        damage = ExplosionEvent.MEDIUMBLAST.calculateDamage(55d, LengthUnit.METRES);
        Assert.assertEquals(damage,0);
    }
}
