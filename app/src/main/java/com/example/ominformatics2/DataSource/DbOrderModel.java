package com.example.ominformatics2.DataSource;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "OrderData")
public class DbOrderModel {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "orderId")
    private int order_id;

    @ColumnInfo(name = "orderNo")
    private String order_no;

    @ColumnInfo(name = "customerName")
    private String customer_name;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "deliveryCost")
    private double delivery_cost;

    @ColumnInfo(name = "deliveryStatus", defaultValue = "Pending")
    private String delivery_status;

    @ColumnInfo(name = "imageUrl", defaultValue = "")
    private String imageUrl;

    @ColumnInfo(name = "isDamaged", defaultValue = "false")
    private String isDamaged;

    @ColumnInfo(name = "damageDesc")
    private String damageDesc;

    @ColumnInfo(name = "anotherAmt")
    private double anotherAmt;

    public DbOrderModel(int order_id, String order_no, String customer_name, double latitude, double longitude, String address, double delivery_cost, String delivery_status) {
        this.order_id = order_id;
        this.order_no = order_no;
        this.customer_name = customer_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.delivery_cost = delivery_cost;
        this.delivery_status = delivery_status;
    }

    // Getters and Setters
    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDelivery_cost() {
        return delivery_cost;
    }

    public void setDelivery_cost(double delivery_cost) {
        this.delivery_cost = delivery_cost;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIsDamaged() {
        return isDamaged;
    }

    public void setIsDamaged(String isDamaged) {
        this.isDamaged = isDamaged;
    }

    public String getDamageDesc() {
        return damageDesc;
    }

    public void setDamageDesc(String damageDesc) {
        this.damageDesc = damageDesc;
    }

    public double getAnotherAmt() {
        return anotherAmt;
    }

    public void setAnotherAmt(double anotherAmt) {
        this.anotherAmt = anotherAmt;
    }
}
