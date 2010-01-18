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

import com.googlecode.deadalus.Event;
import com.googlecode.deadalus.Coordinate;

import java.util.UUID;
import java.io.Serializable;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class DamageEvent extends EventBase implements Event<DamageEvent.DamageInfo> {
    private final DamageInfo payload;

    public DamageEvent(Coordinate location, UUID creator,int damage,boolean fatal) {
        super(location, creator);
        payload = new DamageInfo(fatal,damage);
    }

    @Override
    public final String getType() {
        return "damage";
    }

    @Override
    public final DamageInfo getPayload() {
        return payload;
    }

    public static final class DamageInfo implements Serializable {
        private final boolean fatal;
        private final int damage;

        private DamageInfo(boolean fatal, int damage) {
            this.fatal = fatal;
            this.damage = damage;
        }

        public final boolean isFatal() {
            return fatal;
        }

        public final int getDamage() {
            return damage;
        }
    }
}
