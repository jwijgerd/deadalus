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

package com.googlecode.deadalus.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
@Controller
@Configurable
public class RestController {

    @RequestMapping("create/{clsId}/{lat}/{lon}")
    public String createObject(@PathVariable String clsId,@PathVariable double lat,@PathVariable double lon, Model model) {
        // create the object for this
        return "create";
    }

    @RequestMapping("move/{objectId}/{to.lat}/{to.lon}")
    public String move(@PathVariable("objectId") String objectId,@PathVariable("to.lat") double lat,@PathVariable("to.lon") double lon) {
        return "move";    
    }
}
