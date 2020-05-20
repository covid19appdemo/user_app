package com.sourcey.materiallogindemo;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface loginapi {
    @FormUrlEncoded
    @POST("/track/user_login.php")
    public void insertUser(
            @Field("u_uid") String u_uid,
            @Field("u_password") String u_password,
            Callback<Response> callback);

}
