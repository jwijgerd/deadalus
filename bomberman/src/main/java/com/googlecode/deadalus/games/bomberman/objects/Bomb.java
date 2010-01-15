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
import com.googlecode.deadalus.geoutils.LengthUnit;

import java.util.UUID;
import java.nio.charset.Charset;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class Bomb implements DeadalusObject {
    public static final UUID CLASSIDENT = UUID.nameUUIDFromBytes(Bomb.class.getName().getBytes(Charset.forName("UTF-8")));
    private final UUID id;
    private Context context;
    private int activationRadius = 20;   // will be triggered within 20 metres
    private int blastRadius = 50;       // will have an effect up to 50 metres
    private boolean active = true;

    protected Bomb(UUID id) {
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
        if("create".equals(event.getType()) || "enter".equals(event.getType())) {
            // check the distance, are they within range to trip the bomd
            if(event.getOriginatingLocation().distance(context.getCurrentLocation(), LengthUnit.METRES) < activationRadius) {
                // blast away ;-)
            }
            // for now we can only explode once and then we become inactive
        }
    }
}
