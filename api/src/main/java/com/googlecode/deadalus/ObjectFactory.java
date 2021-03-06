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

package com.googlecode.deadalus;

import java.util.UUID;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public interface ObjectFactory<ObjectClass extends DeadalusObject> {
    /**
     * The unique identifier for this factory
     *
     * @return
     */
    UUID getClassIdentifier();

    /**
     * Create an object with the given arguments
     *
     * @param arguments
     * @return
     */
    ObjectClass createObject(Object... arguments);

    /**
     *
     * @return  The application this factory belongs to
     */
    DeadalusApplication getApplication();
}
