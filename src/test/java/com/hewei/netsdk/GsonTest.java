package com.hewei.netsdk;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by fengyinpeng on 2018/2/9.
 */

public class GsonTest {
    private static final String TAG = "GsonTest";

    public static class TestRequst {
        public String key;
        public boolean b;
        public Sex sex;

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TestRequst)) {
                return false;
            }

            TestRequst req = (TestRequst) obj;
            return ((key == null && req.key == null) || (key != null && key.equals(req.key)))
                    && b == req.b && sex == req.sex;
        }
    }

    public enum Sex {
        Male("M"), Female("F");

        private String item;

        Sex(String item) {
            this.item = item;
        }

        public String getItem() {
            return item;
        }
    }

    @Before
    public void init() {
        NetManager.getInstance().initGson();
    }

    @Test
    public void testRequestToGson() {
        Gson gson = NetManager.getInstance().getGson();
        TestRequst req = new TestRequst();
        req.key = "Hello";
        req.b = true;
        req.sex = Sex.Male;

        GenericBaseRequest<TestRequst> request = new GenericBaseRequest<>(req);
        String str = gson.toJson(request);
        System.out.println(str);
    }

    @Test
    public void testRequestToGson2() {
        Gson gson = NetManager.getInstance().getGson();
        TestRequst req = new TestRequst();
        req.key = "Hello";
        req.b = true;
        req.sex = Sex.Male;

        String str = gson.toJson(req);
        System.out.println(str);
        TestRequst req2 = gson.fromJson(str, TestRequst.class);
        assertEquals(req, req2);
    }

    @Test
    public void testRetrofit() {

    }
}
