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

import com.googlecode.deadalus.DeadalusObject;
import com.googlecode.deadalus.Context;
import com.googlecode.deadalus.Event;
import com.googlecode.deadalus.games.bomberman.events.ExplosionEvent;
import com.googlecode.deadalus.geoutils.LengthUnit;

import java.util.UUID;
import java.nio.charset.Charset;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class Player implements DeadalusObject {
    public static final UUID CLASSIDENT = UUID.nameUUIDFromBytes(Player.class.getName().getBytes(Charset.forName("UTF-8")));
    private final UUID id;
    private Context context;
    // custom properties


    protected Player(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public UUID getClsId() {
        return CLASSIDENT;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onEvent(Event event) {
        // all the logic needs to go here
        if("explosion".equals(event.getType())) {
            // a bomb just exploded near us, now let's see what damage this has done
        } else if("tick".equals(event.getType())) {
            // @todo: what do we want to do on a tick?
        }
    }
}