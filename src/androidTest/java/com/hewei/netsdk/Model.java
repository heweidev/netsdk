package com.hewei.netsdk;

/**
 * Created by fengyinpeng on 2018/2/11.
 */

public class Model {
    public enum Sex {
        MALE, FEMALE;
    }

    public static final class TestRequest {
        public int id;
    }

    public static final class TestResponse {
        public String name;
        public Sex sex;

        @Override
        public String toString() {
            return name + ", " + sex;
        }
    }
}
