package com.hewei.netsdk.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.hewei.netsdk.ErrorCode;
import com.hewei.netsdk.NetManager;
import com.hewei.netsdk.exception.NetException;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by fengyinpeng on 2018/2/9.
 */

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    String MODEL = "model";
    String RESP_CODE = "responseCode";
    String RESP_MESSAGE = "responseMsg";
    String NO_ERR = "000000";

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        String code = null;
        String msg = null;
        String model = null;
        T ret = null;

        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if (RESP_CODE.equals(name)) {
                    code = jsonReader.nextString();
                } else if (RESP_MESSAGE.equals(name)) {
                    msg = jsonReader.nextString();
                } else if (MODEL.equals(name)) {
                    JsonToken token = jsonReader.peek();
                    if (token == JsonToken.STRING) {
                        try {
                            model = NetManager.decrypt(jsonReader.nextString());
                        } catch (Exception e) {
                            break;
                        }
                        ret = adapter.fromJson(model);
                    } else if (token == JsonToken.BEGIN_OBJECT) {
                        ret = adapter.read(jsonReader);
                    } else {
                        jsonReader.skipValue();
                    }
                }
            }
            jsonReader.endObject();
        } finally {
            value.close();
        }

        if (!NO_ERR.equals(code)) {
            throw new NetException(ErrorCode.ERR_INVALID_RESPCODE, "code: " + code + ", msg = " + msg);
        }
        if (ret == null) {
            throw new NetException(ErrorCode.ERR_INVALID_MODEL, model);
        }

        return ret;
    }
}
