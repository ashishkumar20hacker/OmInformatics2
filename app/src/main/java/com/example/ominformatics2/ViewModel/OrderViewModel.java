package com.example.ominformatics2.ViewModel;

import static com.example.ominformatics2.DataSource.MyApplication.app;
import static com.example.ominformatics2.DataSource.MyApplication.getOrderDao;
import static com.example.ominformatics2.DataSource.Retrofit.ApiHandler.getOrders;
import static com.example.ominformatics2.UI.Utils.checkInternetConnection;
import static com.example.ominformatics2.UI.Utils.isLocationPermissionGranted;
import static com.example.ominformatics2.UI.Utils.requestLocationPermission;

import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ominformatics2.DataSource.DbOrderModel;
import com.example.ominformatics2.DataSource.DeliveryStatus;
import com.example.ominformatics2.DataSource.MyApplication;
import com.example.ominformatics2.DataSource.OrderDao;
import com.example.ominformatics2.DataSource.Retrofit.ApiResponseInterface;
import com.example.ominformatics2.DataSource.Retrofit.OrderList;
import com.example.ominformatics2.DataSource.Retrofit.OrderModel;

import java.util.List;

public class OrderViewModel extends ViewModel {

    private static final String TAG = "OrderViewModel";

    private final OrderDao orderDao = getOrderDao();

    public void getObservableListAll(OrderListCallback orderListCallback) {
        MyApplication.getExecutorSingleService().execute(()-> {
            orderListCallback.getListFromDB(orderDao.getOrdersAll());
        });
    }

    public void getObservableListDelivered(OrderListCallback orderListCallback) {
        MyApplication.getExecutorSingleService().execute(()-> {
            orderListCallback.getListFromDB(orderDao.getOrdersDelivered());
        });
    }

    public void getObservableListPending(OrderListCallback orderListCallback) {
        MyApplication.getExecutorSingleService().execute(()-> {
            orderListCallback.getListFromDB(orderDao.getOrdersPending());
        });
    }

    public void getObservableListAsc(OrderListCallback orderListCallback) {
        MyApplication.getExecutorSingleService().execute(()-> {
            orderListCallback.getListFromDB(orderDao.getOrdersAsc());
        });
    }

    public void getObservableListDesc(OrderListCallback orderListCallback) {
        MyApplication.getExecutorSingleService().execute(()-> {
            orderListCallback.getListFromDB(orderDao.getOrdersDesc());
        });
    }

    public interface DistanceCallback {
        void onDistanceCalculated(boolean isWithin50Meters);
    }
    public interface OrderListCallback {
        void getListFromDB(LiveData<List<DbOrderModel>> listLiveData);
    }

    public void getDistanceIsLessThan50M(
            Activity activity,
            double latitude1,
            double longitude1,
            DistanceCallback callback) {

        if (!isLocationPermissionGranted(activity)) {
            requestLocationPermission(activity);
        } else {
            if (checkInternetConnection(activity)) {
                getUsersCurrentLocation(activity, latitude1, longitude1, new OnCalculate() {
                    @Override
                    public void onCalculate(boolean isLess) {
                        callback.onDistanceCalculated(isLess);
                    }
                });
            } else {
                Toast.makeText(activity, "Please connect to internet!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface OnCalculate {
        void onCalculate(boolean isLess);
    }

    public void getUsersCurrentLocation(
            Activity activity,
            double latitude1,
            double longitude1,
            OnCalculate onCalculate) {

        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPS = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.println(Log.ASSERT, TAG, "isGPS: " + isGPS);
        if (isGPS) {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude2 = location.getLatitude();
                    double longitude2 = location.getLongitude();
                    System.out.println("location2>>>" + latitude2 + "," + longitude2);
                    onCalculate.onCalculate(calculateDistance(latitude1, latitude2, longitude1, longitude2));
                }

                @Override
                public void onStatusChanged(String provider, int status, android.os.Bundle extras) {}

                @Override
                public void onProviderEnabled(String provider) {}

                @Override
                public void onProviderDisabled(String provider) {}
            };

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission(activity);
                return;
            }
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, Looper.myLooper());
        }
    }

    public boolean calculateDistance(
            double lat1,
            double lat2,
            double lon1,
            double lon2) {

        double lon1Rad = toRadians(lon1);
        double lon2Rad = toRadians(lon2);
        double lat1Rad = toRadians(lat1);
        double lat2Rad = toRadians(lat2);

        // Haversine formula
        double dlon = lon2Rad - lon1Rad;
        double dlat = lat2Rad - lat1Rad;
        double a = pow(sin(dlat / 2), 2.0) + cos(lat1Rad) * cos(lat2Rad) * pow(sin(dlon / 2), 2.0);
        double c = 2 * asin(sqrt(a));

        // Radius of earth in kilometers. Use 6371 for kilometers
        double radiusOfEarthKm = 6371.0;

        // Calculate the distance in kilometers
        double distanceKm = c * radiusOfEarthKm;

        // Convert the distance to meters
        double distanceMeters = distanceKm * 1000;
        System.out.println("distanceMeters " + distanceMeters);

        // Check if the distance is within 50 meters
        return distanceMeters <= 50;
    }

    public LiveData<Double> getTotalCollectedAmtInString() {
        // Assuming getOrderDao() and getTotalCollectedAmt() are implemented elsewhere
        return getOrderDao().getTotalCollectedAmt();
    }

    public LiveData<String> getTotalDelivery() {
        // Assuming orderDao is an available instance of the appropriate DAO class
        LiveData<Integer> doneLiveData = orderDao.getTotalDeliveryDone();
        LiveData<Integer> totalLiveData = orderDao.getTotalDelivery();

        MediatorLiveData<String> result = new MediatorLiveData<>();

        result.addSource(doneLiveData, done -> {
            Integer total = totalLiveData.getValue();
            if (total == null) total = 0;
            result.setValue(done + "/" + total);
        });

        result.addSource(totalLiveData, total -> {
            Integer done = doneLiveData.getValue();
            if (done == null) done = 0;
            result.setValue(done + "/" + total);
        });

        return result;
    }

}
