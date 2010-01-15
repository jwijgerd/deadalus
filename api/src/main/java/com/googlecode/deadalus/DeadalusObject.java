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
import java.io.Serializable;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public interface DeadalusObject extends Serializable {
    /**
     * @return The globally unique identifier of this object instance
     */
    UUID getId();

    /**
     * @return The class id of this object
     */
    UUID getClsId();

    /**
     * Should be used to interact with the environment, will be set by the Runtime RegionServer.
     *
     * @param context
     */
    void setContext(Context context);

    /**
     * Event handling method called by the system
     *
     * @param event
     */
    void onEvent(Event event);
}
