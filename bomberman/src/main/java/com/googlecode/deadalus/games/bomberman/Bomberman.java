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

import com.googlecode.deadalus.DeadalusApplication;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class Bomberman implements DeadalusApplication {
    @Override
    public String getName() {
        return "Bomberman";
    }

    @Override
    public String getDescription() {
        return "Try to damage other players by placing bombs on strategic places!";
    }
}
