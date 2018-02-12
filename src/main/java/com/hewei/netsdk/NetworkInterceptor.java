package com.hewei.netsdk;

import android.content.Context;
import android.text.TextUtils;

import com.hewei.netsdk.exception.NetException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NetworkInterceptor implements Interceptor {
    private static final String MD5_HEADER = "summary";
    private Context mContext;
    private NetManager mNetManager;

    public NetworkInterceptor(Context context, NetManager manager){
        mContext = context.getApplicationContext();
        mNetManager = manager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!mNetManager.isNetworkAvailable()) {
            throw new NetException(ErrorCode.ERR_NOT_CONNECTED);
        }

        //如果有 md5 标识,在header 添加 md5
        Request request = chain.request();
        Request.Builder builder = request.newBuilder()
                .removeHeader("User-Agent")
                .addHeader("User-Agent", mNetManager.getUserAgent());

        RequestBody requestBody = request.body();
        if (requestBody instanceof MyRequestBody) {
            String md5 = ((MyRequestBody) request.body()).md5;
            if (!TextUtils.isEmpty(md5)) {
                builder.addHeader(MD5_HEADER, md5);
            }
        }

        Request newRequest = builder.post(requestBody)
                .build();
        return chain.proceed(newRequest);
    }
}
