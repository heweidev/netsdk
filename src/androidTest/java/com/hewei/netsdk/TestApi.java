package com.baoneng.wss.netsdk;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by fengyinpeng on 2018/2/11.
 */

public interface TestApi {
    @POST("/test")
    Observable<Model.TestResponse> doRequest(@Body Model.TestRequest request);

    @POST("/test")
    Call<Model.TestResponse> doRequest2(@Body Model.TestRequest request);
}
