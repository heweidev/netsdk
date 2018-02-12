package com.hewei.netsdk;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fengyinpeng on 2018/2/9.
 */

public class BaseRequest {
    public final String _channel_id = "02";

    // 客户端版本号
    @SuppressWarnings("unused")
    public final String _client_version_no = "1.0.1";

    // 设备ID
    @SuppressWarnings("unused")
    public final String _deviceId = "99979fd958b95023";

    @SerializedName("reqData")
    public String data;
}
