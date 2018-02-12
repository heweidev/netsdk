package com.hewei.netsdk.exception;

import java.io.IOException;

/**
 * Created by fengyinpeng on 2018/2/9.
 */

public class NetException extends IOException {
    public NetException(int code) {
        this(code, null, null);
    }

    public NetException(int code, String msg) {
        this(code, msg, null);
    }

    public NetException(int code, String msg, Throwable e) {
        super("error: " + code + ", msg = " + msg, e);
    }
}
