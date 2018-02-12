package com.hewei.netsdk;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.assertEquals;

/**
 * Created by fengyinpeng on 2018/2/11.
 */

@RunWith(AndroidJUnit4.class)
public class RequestTest {
    private MockWebServer server;

    @Before
    public void init() throws IOException {
        server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("{\"model\": {\"name\": \"John\", \"sex\": \"MALE\"}}"));
        server.start();

        String baseUrl = "http://" + server.getHostName() + ":" + server.getPort();
        NetManager.getInstance().init(InstrumentationRegistry.getTargetContext(), baseUrl);
    }

    @Test
    public void doRequest() throws IOException, InterruptedException {
        TestApi api = NetManager.getInstance().createApi(TestApi.class);
        Model.TestRequest request = new Model.TestRequest();
        request.id = 123;
        Model.TestResponse res = api.doRequest2(request).execute().body();

        assertEquals(res.name, "John");
        assertEquals(res.sex, Model.Sex.MALE);
    }
}
