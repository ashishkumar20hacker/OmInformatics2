package com.example.ominformatics2.DataSource;

public enum DeliveryStatus {

    Pending("Pending"),
    Delivered("Delivered");

    private final String status;

    DeliveryStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
