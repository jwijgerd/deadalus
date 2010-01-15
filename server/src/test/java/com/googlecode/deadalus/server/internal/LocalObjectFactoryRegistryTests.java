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

package com.googlecode.deadalus.server.internal;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.springframework.context.ApplicationContext;
import org.easymock.EasyMock;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.googlecode.deadalus.ObjectFactory;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class LocalObjectFactoryRegistryTests {
    @Test
    public void testAddJar() throws IOException, ClassNotFoundException {
        // create a new registry
        LocalObjectFactoryRegistry registry = new LocalObjectFactoryRegistry();
        // mock the application context
        /*ApplicationContext parentContext = EasyMock.createMock(ApplicationContext.class);
        EasyMock.expect(parentContext.getDisplayName()).andReturn("Test Context");
        EasyMock.replay(parentContext);*/
        registry.setApplicationContext(null);
        // should all be fine.. now add a jar that is not on the classpath!
        registry.addJarFile(new File("/Joost/Development/googlecode.com/deadalus/bomberman/target/bomberman-0.1.0-SNAPSHOT.jar"));
        // now we should get the bomberman factory
        ObjectFactory bombFactory = registry.getObjectFactory(UUID.fromString("3d82f2e1-d5c1-376e-8047-a98a395fcf4a"));
        Assert.assertNotNull(bombFactory);
        Assert.assertNotNull(bombFactory.getApplication());
        //EasyMock.verify(parentContext);

    }
}
