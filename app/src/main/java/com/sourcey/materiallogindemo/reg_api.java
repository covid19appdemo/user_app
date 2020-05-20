package com.sourcey.materiallogindemo;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface reg_api {
    @FormUrlEncoded
    @POST("/track/user_reg.php")
    public void insertUser(
            @Field("u_name") String u_name,
            @Field("u_email") String u_email,
            @Field("u_state") String u_state,
            @Field("u_dist") String u_dist,
            @Field("u_area") String u_area,
            @Field("u_mobile_no") String u_mobile_no,
            @Field("u_vehicle_no") String u_vehicle_no,
            @Field("u_loc_latitude") String u_loc_latitude,
            @Field("u_loc_longitude") String u_loc_longitude,
            Callback<Response> callback);
}
