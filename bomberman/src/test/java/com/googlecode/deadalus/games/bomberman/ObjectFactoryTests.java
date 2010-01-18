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

package com.googlecode.deadalus.games.bomberman;

import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.util.UUID;
import java.util.Map;
import java.nio.charset.Charset;

import com.googlecode.deadalus.games.bomberman.objects.Bomb;
import com.googlecode.deadalus.games.bomberman.objects.Player;
import com.googlecode.deadalus.ObjectFactory;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
@ContextConfiguration(locations = "/META-INF/factories.xml")
public class ObjectFactoryTests extends AbstractTestNGSpringContextTests {
    @Test
    public void testFindFactories() {
        // we should have one factory
        Map<String,ObjectFactory> factories = applicationContext.getBeansOfType(ObjectFactory.class);
        Assert.assertFalse(factories.isEmpty());
    }

    @Test
    public void testGenerateUUID() {
        System.out.println(UUID.nameUUIDFromBytes(Player.class.getName().getBytes(Charset.forName("UTF-8"))));
        System.out.println(UUID.randomUUID());
        System.out.println(Bomb.class.getName());
        System.out.println(UUID.nameUUIDFromBytes("com.googlecode.deadalus.games.bomberman.objects.Bomb".getBytes(Charset.forName("UTF-8"))));
        System.out.println(UUID.nameUUIDFromBytes("com.googlecode.deadalus.games.bomberman.objects.Bomb".getBytes(Charset.forName("UTF-8"))));
    }
}
