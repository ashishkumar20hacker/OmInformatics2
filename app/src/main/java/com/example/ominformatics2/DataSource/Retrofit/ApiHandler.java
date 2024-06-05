package com.example.ominformatics2.DataSource.Retrofit;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiHandler {

    private static final String TAG = "ApiHandler";

    public static void getOrders(ApiResponseInterface apiResponseInterface) {
        ApiService apiService = RetrofitApiClient.getInstance().getClient().create(ApiService.class);
        Call<OrderModel> call = apiService.getOrderList();
        call.enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                if (response.isSuccessful()) {
                    OrderModel root = response.body();
                    if (root != null) {
                        apiResponseInterface.onSuccess(root.getOrderlist());
                    }
                } else {
                    Log.e(TAG, "Failed to fetch product details");
                    // Handle unsuccessful response
                    apiResponseInterface.onError();
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                apiResponseInterface.onError();
            }
        });
    }
}
