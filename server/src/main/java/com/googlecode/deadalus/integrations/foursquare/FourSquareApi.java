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

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.commons.codec.binary.Base64;
import com.googlecode.deadalus.Coordinate;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class FourSquareApi {
    private static final AuthScope AUTHSCOPE = new AuthScope("api.foursquare.com",80);
    private final HttpClient httpClient;

    public FourSquareApi() {
        this(new DefaultHttpClient());
        httpClient.getParams().setParameter(HTTP.EXPECT_CONTINUE,false);
        httpClient.getParams().setParameter(HTTP.USER_AGENT,"Deadalus 0.1.0");
    }

    public FourSquareApi(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Coordinate getCurrentLocation() throws IOException {
        String userName = "jwijgerd@gmail.com";
        String password = "I2l0v3JT";
        Base64 codec = new Base64(76,new byte[]{});
        // String encodedPW = Base64.encodeBase64URLSafeString((userName+":"+password).getBytes(Charset.forName(HTTP.UTF_8)));
        String encodedPW = codec.encodeToString((userName+":"+password).getBytes(Charset.forName(HTTP.UTF_8)));
        //((DefaultHttpClient)httpClient).getCredentialsProvider().setCredentials(AUTHSCOPE,new UsernamePasswordCredentials("jwijgerd","I2l0v3JT"));
        HttpGet historyMethod = new HttpGet("http://api.foursquare.com/v1/history.json?l=10");
        historyMethod.setHeader("Authorization", "Basic " + encodedPW);
        HttpResponse response = httpClient.execute(historyMethod);
        String content = EntityUtils.toString(response.getEntity());
        System.out.println(content);
        // now we need to parse the coordinate
        return null;
    }
}
