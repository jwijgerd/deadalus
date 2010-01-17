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
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Autowired;
import com.googlecode.deadalus.RegionServerRegistry;
import com.googlecode.deadalus.Coordinate;
import com.googlecode.deadalus.RegionServer;
import com.googlecode.deadalus.DeadalusObject;
import com.googlecode.deadalus.rest.editor.UUIDEditor;
import com.googlecode.deadalus.rest.editor.CoordinateEditor;

import javax.swing.text.View;
import java.util.UUID;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
@Controller
@Configurable
public class RestController {
    private RegionServerRegistry regionServerRegistry;

    @Autowired
    public void setRegionServerRegistry(RegionServerRegistry regionServerRegistry) {
        this.regionServerRegistry = regionServerRegistry;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(UUID.class,new UUIDEditor());
        binder.registerCustomEditor(Coordinate.class,new CoordinateEditor());
    }

    @RequestMapping(value = "/test")
    public String test() {
        return "success";    
    }

    @RequestMapping(value = "/create/{clsId}/{latlon}",method = {RequestMethod.GET})
    public String createObject(@PathVariable UUID clsId,@PathVariable Coordinate latlon, Model model) {
        // first get the RegionServer for this coordinate
        RegionServer regionServer = regionServerRegistry.findByCoordinate(latlon);
        // @todo: if this is not a local server than should we send a Redirect?
        DeadalusObject object = regionServer.createObject(clsId,latlon);
        // @todo: need some way of passing in arbitrary parameters
        model.addAttribute("createdObject",object);
        return "create";
    }

    @RequestMapping("/move/{objectId}/{to.latlon}")
    public String move(@PathVariable("objectId") UUID objectId,@PathVariable("to.latlon") Coordinate to) {
        RegionServer regionServer = regionServerRegistry.findByObjectId(objectId);
        regionServer.moveObject(objectId,to);
        return "move";    
    }
}
