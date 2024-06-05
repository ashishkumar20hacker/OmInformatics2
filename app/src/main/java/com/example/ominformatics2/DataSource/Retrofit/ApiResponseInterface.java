package com.example.ominformatics2.DataSource.Retrofit;

import java.util.List;

public interface ApiResponseInterface {

    void onSuccess(List<OrderList> orderlist);

    void onError();

}
