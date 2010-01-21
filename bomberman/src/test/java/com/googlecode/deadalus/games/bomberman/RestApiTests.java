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

import org.testng.annotations.Test;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author Joost van de Wijgerd <joost@vdwbv.com>
 */
public class RestApiTests {
    private final String testHost = "http://localhost:8080/";
    private final HttpClient httpClient = new DefaultHttpClient();

    @Test(enabled = false)
    public void testCreatePlayerAndDetonateBomd() throws IOException, InterruptedException {

        // first create the bomb
        HttpGet createBomb = new HttpGet(testHost+"create/3d82f2e1-d5c1-376e-8047-a98a395fcf4a/52.3777634,4.869633/a.json");
        HttpResponse response = httpClient.execute(createBomb);
        System.out.println(EntityUtils.toString(response.getEntity()));

        // give it some time to tick
        Thread.sleep(5000);
        // now create the player right on top of the bomd
        HttpGet createPlayer = new HttpGet(testHost+"create/43155b7e-69b0-39f4-99c7-7160b4d46e43/52.3777634,4.869533/a.json");
        response = httpClient.execute(createPlayer);
        System.out.println(EntityUtils.toString(response.getEntity()));
        // we should see an explosion
    }

    @Test(enabled = false)
    public void testCreatePlayer() throws IOException {
        HttpGet createPlayer = new HttpGet(testHost+"create/43155b7e-69b0-39f4-99c7-7160b4d46e43/52.3777634,4.869634/a.json");
        HttpResponse response = httpClient.execute(createPlayer);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    @Test(enabled = false)
    public void testCreateBomb() throws IOException {
        // first create the bomb
        HttpGet createBomb = new HttpGet(testHost+"create/3d82f2e1-d5c1-376e-8047-a98a395fcf4a/52.3777634,4.869633/a.json");
        HttpResponse response = httpClient.execute(createBomb);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}
