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
import com.googlecode.deadalus.games.bomberman.events.DamageEvent;
import com.googlecode.deadalus.geoutils.LengthUnit;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.nio.charset.Charset;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class Player implements DeadalusObject {
    public static final UUID CLASSIDENT = UUID.nameUUIDFromBytes(Player.class.getName().getBytes(Charset.forName("UTF-8")));
    private final UUID id;
    private Context context;
    // custom properties
    private final AtomicInteger health = new AtomicInteger(100);


    protected Player(UUID id) {
        this.id = id;
    }

    @Override
    public final UUID getId() {
        return id;
    }

    @Override
    public final UUID getClsId() {
        return CLASSIDENT;
    }

    @Override
    public final void setContext(Context context) {
        this.context = context;
    }

    @Override
    public final void onEvent(Event event) {
        // all the logic needs to go here
        if("explosion".equals(event.getType())) {
            // a bomb just exploded near us, now let's see what damage this has done
            // @todo: we probably want to check the last update time and do something with it, possibly notify the
            // @todo: user and give him some time to update his position to get out of harms way
            // get the distance
            double distanceToBlast = context.getCurrentLocation().distance(event.getOriginatingLocation(),LengthUnit.METRES);
            int damage = ((ExplosionEvent)event).getPayload().calculateDamage(distanceToBlast,LengthUnit.METRES);
            // now we need to apply the damage
            if(damage > 0) applyDamage((ExplosionEvent)event,damage);
        } else if("tick".equals(event.getType())) {
            // @todo: what do we want to do on a tick?
        }
    }

    private void applyDamage(ExplosionEvent event, int damage) {
        // @todo: when the Shield is implemented we need to take damage out of the shield
        // apply the damage to the health, we also need to notify the bomb of the damage done
        final int newHealth = health.addAndGet(-damage);
        if(newHealth <= 0) {  // oops we died!
            // notify the Bomb that it has made a Kill!
            System.out.println("Oh no, I died!");
            context.send(new DamageEvent(context.getCurrentLocation(),getId(),damage,true),event.getOriginationObjectId(),null);
            // @todo: does this Player object have a next life or should the User instantiate a new Life?
        } else {
            // notify the exploding Bomb of the damage done
            System.out.println("Sustained "+damage+" Damage. Health = "+health.get());
            context.send(new DamageEvent(context.getCurrentLocation(),getId(),damage,false),event.getOriginationObjectId(),null);
        }
    }
}