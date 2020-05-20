package com.sourcey.materiallogindemo;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface data_api {
    @FormUrlEncoded
    @POST("/track/police_login.php")
    public void insertUser(
            @Field("qr_code") String qr_code,
            Callback<Response> callback);

}
