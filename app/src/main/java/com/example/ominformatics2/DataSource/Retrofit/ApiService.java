package com.example.ominformatics2.DataSource.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("orderlist.php")
    Call<OrderModel> getOrderList();

}
