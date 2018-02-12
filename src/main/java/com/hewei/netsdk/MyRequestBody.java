package com.hewei.netsdk;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.ByteString;

/**
 * Created by fengyinpeng on 2018/2/9.
 */

public class MyRequestBody extends RequestBody {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    public ByteString content;
    public Object arg;
    public String md5;

    //仅测试时使用,传递a
    public String log;

    public static MyRequestBody create(ByteString content, String md5) {
        MyRequestBody body = new MyRequestBody();
        body.content = content;
        body.md5 = md5;
        return body;
    }

    @Override
    public MediaType contentType() {
        return MEDIA_TYPE;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        sink.write(content);
    }
}
