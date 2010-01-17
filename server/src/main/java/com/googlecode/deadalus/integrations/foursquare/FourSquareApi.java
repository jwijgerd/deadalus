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

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.googlecode.deadalus.Coordinate;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class FourSquareApi {
    private static final AuthScope AUTHSCOPE = new AuthScope("api.foursquare.com", 80);
    private final HttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper();
    private final DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yy HH:mm:ss Z");

    public FourSquareApi() {
        this(new DefaultHttpClient());
        httpClient.getParams().setParameter(HTTP.EXPECT_CONTINUE, false);
        httpClient.getParams().setParameter(HTTP.USER_AGENT, "Deadalus 0.1.0");
    }

    public FourSquareApi(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public LocationUpdate getLastKnownLocation() throws IOException {
        String userName = "jwijgerd@gmail.com";
        String password = "xxxxxxx";
        Base64 codec = new Base64(76, new byte[]{});
        // String encodedPW = Base64.encodeBase64URLSafeString((userName+":"+password).getBytes(Charset.forName(HTTP.UTF_8)));
        String encodedPW = codec.encodeToString((userName + ":" + password).getBytes(Charset.forName(HTTP.UTF_8)));
        //((DefaultHttpClient)httpClient).getCredentialsProvider().setCredentials(AUTHSCOPE,new UsernamePasswordCredentials("jwijgerd","I2l0v3JT"));
        HttpGet historyMethod = new HttpGet("http://api.foursquare.com/v1/history.json?l=5");
        historyMethod.setHeader("Authorization", "Basic " + encodedPW);
        HttpResponse response = httpClient.execute(historyMethod);
        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity());
            // now we need to parse the coordinate
            Map<String, Object> jsonContent = mapper.readValue(content, Map.class);
            ArrayList<Map<String, Object>> checkins = (ArrayList<Map<String, Object>>) jsonContent.get("checkins");
            for (Map<String, Object> checkin : checkins) {
                try {
                    Date date = dateFormat.parse((String) checkin.get("created"));
                    // now get the venue
                    Coordinate newLocation = FourSquareUtils.venueToCoordinate((Map<String,Object>) checkin.get("venue"));
                    if(newLocation != null) {
                        return new LocationUpdate(null,newLocation,date);
                    }
                } catch (ParseException e) {
                    // ignore
                }

            }
            return null;
        } else {
            response.getEntity().consumeContent();
            return null;
        }
    }
}
