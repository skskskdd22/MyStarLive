package com.example.owner.mystarlive;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface UserAPI {

    String BASE_URL = "http://49.247.193.211/";

    @Headers("Content-Type: application/json")
    @GET("login.php")
    Call<User> login(@Query("userid")String userid,
                     @Query("userpass")String userpass);

}
