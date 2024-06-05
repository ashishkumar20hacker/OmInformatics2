package com.example.ominformatics2.DataSource;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrderDao {

    @Insert(entity = DbOrderModel.class, onConflict = OnConflictStrategy.REPLACE)
    void insert(DbOrderModel model);

    @Delete
    void delete(DbOrderModel model);

    @Update
    void update(DbOrderModel model);

    @Query("SELECT * FROM OrderData")
    LiveData<List<DbOrderModel>> getOrdersAll();

    @Query("SELECT * FROM OrderData WHERE deliveryStatus = 'Delivered'")
    LiveData<List<DbOrderModel>> getOrdersDelivered();

    @Query("SELECT * FROM OrderData WHERE deliveryStatus = 'Pending'")
    LiveData<List<DbOrderModel>> getOrdersPending();

    @Query("SELECT * FROM OrderData ORDER BY customerName ASC")
    LiveData<List<DbOrderModel>> getOrdersAsc();

    @Query("SELECT * FROM OrderData ORDER BY customerName DESC")
    LiveData<List<DbOrderModel>> getOrdersDesc();

    @Query("SELECT * FROM OrderData WHERE orderId = :orderId")
    DbOrderModel getOrderById(int orderId);

    @Query("SELECT SUM(deliveryCost) FROM OrderData WHERE deliveryStatus = 'Delivered'")
    LiveData<Double> getTotalCollectedAmt();

    @Query("SELECT COUNT(deliveryStatus) FROM OrderData WHERE deliveryStatus = 'Delivered'")
    LiveData<Integer> getTotalDeliveryDone();

    @Query("SELECT COUNT(deliveryStatus) FROM OrderData")
    LiveData<Integer> getTotalDelivery();

}
