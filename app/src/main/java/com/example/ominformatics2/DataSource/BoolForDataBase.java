package com.example.ominformatics2.DataSource;

public enum BoolForDataBase {
    TRUE("true"),
    FALSE("false");

    private final String status;

    BoolForDataBase(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}