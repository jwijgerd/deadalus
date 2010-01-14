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

import com.googlecode.deadalus.ObjectFactoryRegistry;
import com.googlecode.deadalus.ObjectFactory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.net.URLClassLoader;
import java.net.URL;
import java.io.File;
import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class LocalObjectFactoryRegistry implements ObjectFactoryRegistry {
    private final ConcurrentHashMap<UUID,ObjectFactory> registry = new ConcurrentHashMap<UUID,ObjectFactory>();
    private final JarFileLoader classLoader = new JarFileLoader(new URL[] {},Thread.currentThread().getContextClassLoader());

    @Override
    public ObjectFactory getObjectFactory(UUID clsid) {
        return registry.get(clsid);
    }

    public void addJarFile(File jarFile) throws IOException {
        // add the file to the classloader
        classLoader.addJarFile(jarFile);
        // get the resources
        Resource springCtxResource = new ClassPathResource("META-INF/factories.xml",classLoader);
        if(springCtxResource.exists()) {
            // create the spring context for the application
        }
    }

    private final class JarFileLoader extends URLClassLoader {

        public JarFileLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        void addJarFile(File jarFile) throws IOException {
            String urlPath = "jar:file://" + jarFile.getAbsoluteFile() + "!/";
            addURL (new URL (urlPath));
        }
    }
}
