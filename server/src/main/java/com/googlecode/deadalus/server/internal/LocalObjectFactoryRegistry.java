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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.net.URLClassLoader;
import java.net.URL;
import java.io.File;
import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.BeansException;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class LocalObjectFactoryRegistry implements ObjectFactoryRegistry, ApplicationContextAware {
    private final ConcurrentHashMap<UUID,ObjectFactory> registry = new ConcurrentHashMap<UUID,ObjectFactory>();
    private final JarFileLoader classLoader = new JarFileLoader(new URL[] {},Thread.currentThread().getContextClassLoader());
    private ApplicationContext parentCtx;

    @Override
    public ObjectFactory getObjectFactory(UUID clsid) {
        return registry.get(clsid);
    }

    public void addJarFile(File jarFile) throws IOException {
        // add the file to the classloader
        if(jarFile.exists()) System.out.println("jarFile = " + jarFile);
        classLoader.addJarFile(jarFile);
        // get the resources
        Resource springCtxResource = new ClassPathResource("META-INF/factories.xml",classLoader);
        // @todo: do we need to reset this at the end?
        Thread.currentThread().setContextClassLoader(classLoader);
        if(springCtxResource.exists()) {
            // create the spring context for the application
            String[] locations = new String[] {"/META-INF/factories.xml"};
            ClassPathXmlApplicationContext springCtx = new ClassPathXmlApplicationContext(locations,parentCtx);
            Map<String,ObjectFactory> factories = springCtx.getBeansOfType(ObjectFactory.class);
            // now register them with the registry
            for (Map.Entry<String, ObjectFactory> factoryEntry : factories.entrySet()) {
                UUID clsId = factoryEntry.getValue().getClassIdentifier();
                // double check if we already had a factory registered for this UUID
                if(!registry.containsKey(clsId)) {
                    registry.put(clsId,factoryEntry.getValue());
                } else {
                    // @todo: what to do with already registered ObjectFactory instances? overwrite?
                }
            }

        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.parentCtx = applicationContext;
    }

    private final class JarFileLoader extends URLClassLoader {

        public JarFileLoader(URL[] urls,ClassLoader parent) {
            super(urls,parent);
        }

        void addJarFile(File jarFile) throws IOException {
            // String urlPath = "jar:file://" + jarFile.getAbsoluteFile() + "!/";
            String urlPath = "file:"+jarFile.getAbsoluteFile();
            addURL (new URL (urlPath));
        }
    }
}
