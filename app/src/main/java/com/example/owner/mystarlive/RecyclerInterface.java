package com.example.owner.mystarlive;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecyclerInterface {

    String JSONURL = "http://49.247.206.36/";

    @GET("livelist.php")
    Call<String> getlivelist();

    @GET("vodlist.php")
    Call<String> getvoidlist();

    @GET("goods.php")
    Call<String> getgoodslist();
}
