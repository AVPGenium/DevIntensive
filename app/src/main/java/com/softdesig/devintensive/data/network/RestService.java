package com.softdesig.devintensive.data.network;

import com.softdesig.devintensive.data.network.request.UserLoginReq;
import com.softdesig.devintensive.data.network.responce.UserModelRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RestService {
//    @Headers({
//            "Custom-Header : my header Value"
//    })
//    @POST("login")
//    Call<UserModelRes> loginUser(@Header("Last-Modified") String lastMod,
//                                 @Body UserLoginReq req);
    @POST("login")
    Call<UserModelRes> loginUser(@Body UserLoginReq req);
}
