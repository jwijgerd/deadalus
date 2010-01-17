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

package com.googlecode.deadalus.rest.editor;

import com.googlecode.deadalus.Coordinate;

import java.beans.PropertyEditorSupport;

/**
 * Takes a comma separated lat,lon pair and generates a Coordinate object
 *
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class CoordinateEditor extends PropertyEditorSupport {

    @Override
    public final String getAsText() {
        Coordinate coordinate = (Coordinate)getValue();
        return (Double.toString(coordinate.getLatitude())+","+Double.toString(coordinate.getLongitude()));
    }

    @Override
    public final void setAsText(String text) throws IllegalArgumentException {
        String[] latlon = text.split(",");
        if(latlon.length != 2) throw new IllegalArgumentException("Input should be of form \"<double(lat)>,<double<lon>\"");
        setValue(new Coordinate(Double.parseDouble(latlon[0]),Double.parseDouble(latlon[1])));
    }
}
