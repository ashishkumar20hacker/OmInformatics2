package com.example.ominformatics2.DataSource;

import static com.example.ominformatics2.DataSource.Retrofit.ApiHandler.getOrders;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.ominformatics2.DataSource.Retrofit.ApiResponseInterface;
import com.example.ominformatics2.DataSource.Retrofit.OrderList;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {

    public static MyApplication app;

    private static OrderDao orderDao;

    private static ExecutorService service;

    public static OrderDao getOrderDao() {
        if (orderDao == null) {
            orderDao = OmInformaticsDatabase.getInstance(app).dao();
        }
        return orderDao;
    }

    public static ExecutorService getExecutorSingleService() {
        if (service == null) {
            service = Executors.newSingleThreadExecutor();
        }
        return service;
    }

    public static ExecutorService getExecutorMultipleService(int threadCount) {
        if (service == null) {
            service = Executors.newFixedThreadPool(threadCount);
        }
        return service;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        SharedPreferences prefs = app.getApplicationContext().getSharedPreferences("PREFS_NAME_2", Context.MODE_PRIVATE);
        boolean isFirstTime = prefs.getBoolean("PREF_FIRST_TIME_2", true);

        if (isFirstTime) {
            // Perform the data update here

            // For example, you can update a database or perform any other initialization
            MyApplication.getExecutorSingleService().execute(() -> {
                getOrders(new ApiResponseInterface() {
                    @Override
                    public void onSuccess(List<OrderList> orderlist) {
                        if (orderlist != null && !orderlist.isEmpty())
                            for (OrderList orderModel : orderlist) {
                                DbOrderModel dbOrderModel = new DbOrderModel(Integer.parseInt(orderModel.getOrder_id()),
                                        orderModel.getOrder_no(),
                                        orderModel.getCustomer_name(),
                                        Double.parseDouble(orderModel.getLatitude()),
                                        Double.parseDouble(orderModel.getLongitude()),
                                        orderModel.getAddress(),
                                        Double.parseDouble(orderModel.getDelivery_cost()),
                                        DeliveryStatus.Pending.getStatus());
                                MyApplication.getExecutorSingleService().execute(() -> orderDao.insert(dbOrderModel));
                            }
                    }

                    @Override
                    public void onError() {

                    }
                });

            });
        }
        // Set the flag to indicate that the app has been started before
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("PREF_FIRST_TIME_2", false);
        editor.apply();
    }
}
