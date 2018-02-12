package com.hewei.netsdk;

/**
 * Created by fengyinpeng on 2018/2/9.
 */

public class GenericBaseRequest<T> extends BaseRequest {
    public GenericBaseRequest(T data) {
        reqData = data;
    }

    public transient final T reqData;
}
