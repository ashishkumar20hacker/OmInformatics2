package com.example.ominformatics2.DataSource.Retrofit;

public class OrderList {

    private String address;
    private String customer_name;
    private String delivery_cost;
    private String latitude;
    private String longitude;
    private String order_id;
    private String order_no;

    public OrderList(String address, String customer_name, String delivery_cost, String latitude, String longitude, String order_id, String order_no) {
        this.address = address;
        this.customer_name = customer_name;
        this.delivery_cost = delivery_cost;
        this.latitude = latitude;
        this.longitude = longitude;
        this.order_id = order_id;
        this.order_no = order_no;
    }

    // Getters and Setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getDelivery_cost() {
        return delivery_cost;
    }

    public void setDelivery_cost(String delivery_cost) {
        this.delivery_cost = delivery_cost;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }
}
