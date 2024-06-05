package com.example.ominformatics2.DataSource.Retrofit;

import java.util.List;

public class OrderModel {

     List<OrderList> orderlist;

    public OrderModel(List<OrderList> orderlist) {
        this.orderlist = orderlist;
    }

    public List<OrderList> getOrderlist() {
        return orderlist;
    }

    public void setOrderlist(List<OrderList> orderlist) {
        this.orderlist = orderlist;
    }
}
