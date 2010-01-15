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

package com.googlecode.deadalus.games.bomberman.objects;

import com.googlecode.deadalus.ObjectFactory;
import com.googlecode.deadalus.DeadalusObject;
import com.googlecode.deadalus.DeadalusApplication;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
@Service()
@Configurable
public class BombObjectFactory implements ObjectFactory<Bomb> {
    private DeadalusApplication application;

    @Autowired
    public final void setApplication(DeadalusApplication application) {
        this.application = application;
    }

    @Override
    public final DeadalusApplication getApplication() {
        return application;
    }

    @Override
    public final UUID getClassIdentifier() {
        return Bomb.CLASSIDENT;
    }

    @Override
    public final Bomb createObject(Object... arguments) {
        // @todo: do we need to know the owner of the bomb here? probably not
        if(arguments.length > 0) {
            // @todo: add argument parsing + exception if something goes wrong
        }
        return new Bomb(UUID.randomUUID(), 20, 50);
    }
}
