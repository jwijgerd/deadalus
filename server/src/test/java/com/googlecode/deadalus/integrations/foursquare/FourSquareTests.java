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

package com.googlecode.deadalus.integrations.foursquare;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.codehaus.jackson.map.ObjectMapper;
import org.easymock.EasyMock;

import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.googlecode.deadalus.Coordinate;
import com.googlecode.deadalus.DeadalusUser;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class FourSquareTests {
    @Test(enabled = false)
    public void testGetCurrentLocation() throws IOException {
        DeadalusUser user = EasyMock.createMock(DeadalusUser.class);
        FourSquareApi api = new FourSquareApi();
        LocationUpdate update = api.getLastKnownLocation(user);
        Assert.assertNotNull(update);
    }

    @Test
    public void testObjectMapper() throws IOException, ParseException {
        String content = "{\"checkins\":[{\"id\":5709205,\"created\":\"Sun, 17 Jan 10 12:32:54 +0000\",\"venue\":{\"id\":538760,\"name\":\"VDW HQ\",\"address\":\"Visseringstraat 21A\",\"city\":\"Amsterdam\",\"state\":\"NH\",\"zip\":\"1051KH\",\"geolat\":52.3777634,\"geolong\":4.869633},\"display\":\"Joost v. @ VDW HQ\"},{\"id\":5708035,\"created\":\"Sun, 17 Jan 10 11:42:16 +0000\",\"venue\":{\"id\":68943,\"name\":\"Razmataz\",\"address\":\"Huge de Grootplein 9-11\",\"crossstreet\":\"Jan van Galenstraat\",\"city\":\"Amsterdam\",\"state\":\"The Netherlands\",\"zip\":\"1052KV\",\"geolat\":52.374197,\"geolong\":4.8745459,\"phone\":\"0204868408\"},\"display\":\"Joost v. @ Razmataz\"},{\"id\":5586218,\"created\":\"Sat, 16 Jan 10 08:55:41 +0000\",\"venue\":{\"id\":538760,\"name\":\"VDW HQ\",\"address\":\"Visseringstraat 21A\",\"city\":\"Amsterdam\",\"state\":\"NH\",\"zip\":\"1051KH\",\"geolat\":52.3777634,\"geolong\":4.869633},\"display\":\"Joost v. @ VDW HQ\"},{\"id\":5502723,\"created\":\"Fri, 15 Jan 10 17:51:27 +0000\",\"venue\":{\"id\":538760,\"name\":\"VDW HQ\",\"address\":\"Visseringstraat 21A\",\"city\":\"Amsterdam\",\"state\":\"NH\",\"zip\":\"1051KH\",\"geolat\":52.3777634,\"geolong\":4.869633},\"display\":\"Joost v. @ VDW HQ\"},{\"id\":5480836,\"created\":\"Fri, 15 Jan 10 13:08:57 +0000\",\"venue\":{\"id\":538760,\"name\":\"VDW HQ\",\"address\":\"Visseringstraat 21A\",\"city\":\"Amsterdam\",\"state\":\"NH\",\"zip\":\"1051KH\",\"geolat\":52.3777634,\"geolong\":4.869633},\"display\":\"Joost v. @ VDW HQ\"},{\"id\":5472333,\"created\":\"Fri, 15 Jan 10 08:49:56 +0000\",\"venue\":{\"id\":593672,\"name\":\"Stadsdeel westerpark\",\"address\":\"\",\"city\":\"\",\"state\":\"\",\"geolat\":52.386729,\"geolong\":4.878363},\"display\":\"Joost v. @ Stadsdeel westerpark\"},{\"id\":5415300,\"created\":\"Thu, 14 Jan 10 20:16:05 +0000\",\"venue\":{\"id\":538760,\"name\":\"VDW HQ\",\"address\":\"Visseringstraat 21A\",\"city\":\"Amsterdam\",\"state\":\"NH\",\"zip\":\"1051KH\",\"geolat\":52.3777634,\"geolong\":4.869633},\"display\":\"Joost v. @ VDW HQ\"},{\"id\":5373359,\"created\":\"Thu, 14 Jan 10 08:03:42 +0000\",\"venue\":{\"id\":151994,\"name\":\"eBuddy HQ\",\"address\":\"Keizersgracht\",\"city\":\"Amsterdam\",\"state\":\"The Netherlands\",\"geolat\":52.3693618,\"geolong\":4.8845306},\"display\":\"Joost v. @ eBuddy HQ\"},{\"id\":5371827,\"created\":\"Thu, 14 Jan 10 07:18:20 +0000\",\"venue\":{\"id\":538760,\"name\":\"VDW HQ\",\"address\":\"Visseringstraat 21A\",\"city\":\"Amsterdam\",\"state\":\"NH\",\"zip\":\"1051KH\",\"geolat\":52.3777634,\"geolong\":4.869633},\"display\":\"Joost v. @ VDW HQ\"},{\"id\":5328181,\"created\":\"Wed, 13 Jan 10 21:19:04 +0000\",\"venue\":{\"id\":55912,\"name\":\"Struik\",\"address\":\"Rozengracht 160\",\"city\":\"Amsterdam\",\"state\":\"Netherlands\",\"zip\":\"1013 MS\",\"geolat\":52.3728,\"geolong\":4.8784},\"display\":\"Joost v. @ Struik\"}]}";
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yy HH:mm:ss Z");
        Map<String,Object> jsonContent = mapper.readValue(content, Map.class);
        ArrayList<Map<String,Object>> checkins = (ArrayList<Map<String,Object>>) jsonContent.get("checkins");
        for (Map<String, Object> checkin : checkins) {
            Date date = dateFormat.parse((String)checkin.get("created"));
            Coordinate coord = FourSquareUtils.venueToCoordinate((Map<String,Object>) checkin.get("venue"));
            System.out.println(date + " "+coord.getLatitude() +","+coord.getLongitude());
        }
    }
}
