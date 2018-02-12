package com.hewei.netsdk.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.hewei.netsdk.BaseRequest;
import com.hewei.netsdk.GenericBaseRequest;
import com.hewei.netsdk.NetManager;
import com.hewei.netsdk.MyRequestBody;

import java.io.IOException;

import okhttp3.RequestBody;
import okio.ByteString;
import retrofit2.Converter;

/**
 * Created by fengyinpeng on 2018/2/9.
 */

final class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        GenericBaseRequest<T> request = new GenericBaseRequest(value);
        if (value instanceof String) {
            request.data = (String) value;
        } else {
            String rawData = gson.toJson(value);
            request.data = NetManager.cryptoEngine(rawData);
        }

        String strData = gson.toJson(request, BaseRequest.class);
        String md5 = NetManager.md5(strData);
        return MyRequestBody.create(ByteString.encodeUtf8(strData), md5);
    }
}
