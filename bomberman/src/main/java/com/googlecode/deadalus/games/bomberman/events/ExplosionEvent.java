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
import com.googlecode.deadalus.geoutils.LengthUnit;

import java.util.UUID;
import java.io.Serializable;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class ExplosionEvent extends EventBase implements Event<ExplosionEvent.ExplosionInfo> {
    public static final ExplosionInfo MEDIUMBLAST = new ExplosionInfo(100,50);
    private final ExplosionInfo info;

    public ExplosionEvent(UUID creator, Coordinate location, ExplosionInfo info) {
        super(location, creator);
        this.info = info;
    }

    @Override
    public String getType() {
        return "explosion";
    }

    @Override
    public ExplosionInfo getPayload() {
        return info;
    }

    public static final class ExplosionInfo implements Serializable {
        private final int maxDamage;  // maximum damage
        private final int blastRadius;  // blast radius in meters

        protected ExplosionInfo(int maxDamage, int blastRadius) {
            this.maxDamage = maxDamage;
            this.blastRadius = blastRadius;
        }

        /**
         * Calculate the damage to a Player based on the distance to the center of the blast. The further away
         *
         * @param distanceToCenter
         * @param unit
         * @return
         */
        public final int calculateDamage(final double distanceToCenter, final LengthUnit unit) {
            if(distanceToCenter > blastRadius) {  // no damage
                return 0;
            } else {
                // distance 0 is maxDamage
                return (int) Math.round(((blastRadius - distanceToCenter) / blastRadius) * maxDamage);
            }
        }
    }
}
