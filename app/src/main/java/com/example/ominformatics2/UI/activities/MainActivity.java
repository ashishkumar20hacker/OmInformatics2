package com.example.ominformatics2.UI.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ominformatics2.DataSource.DbOrderModel;
import com.example.ominformatics2.R;
import com.example.ominformatics2.UI.adapters.OrderAdapter;
import com.example.ominformatics2.ViewModel.OrderViewModel;
import com.example.ominformatics2.databinding.ActivityMainBinding;
import com.example.ominformatics2.databinding.FilterDialogBinding;
import com.example.ominformatics2.databinding.LoadingDialogBinding;
import com.example.ominformatics2.databinding.SortDialogBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    OrderViewModel viewOrderModel;
    int filterType = R.id.radioAll, sortType = R.id.radioAsc;

    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewOrderModel = new ViewModelProvider(this).get(OrderViewModel.class);

        viewOrderModel.getObservableListAll(this::handleAdapter);

        viewOrderModel.getTotalCollectedAmtInString().observe(this, totalAmt -> {
            if (totalAmt == null) {
                binding.totalAmtTv.setText("₹0");
            } else {
                binding.totalAmtTv.setText(String.format("₹%s", totalAmt));
            }
        });


        viewOrderModel.getTotalDelivery().observe(this, totalDelivery -> {
            binding.totalDeliveriesDone.setText(totalDelivery);
        });

        binding.sort.setOnClickListener(v -> {
            openSortDialog();
        });

        binding.filter.setOnClickListener(v -> {
            openFilterDialog();
        });


        LoadingDialogBinding binding = LoadingDialogBinding.inflate(getLayoutInflater());
        loadingDialog = new Dialog(MainActivity.this, R.style.SheetDialog);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(binding.getRoot());

    }

    private void handleAdapter(LiveData<List<DbOrderModel>> listLiveData) {
        runOnUiThread(() -> {
            listLiveData.observe(this, dbOrderModels -> {
                OrderAdapter adapter = new OrderAdapter(this, orderModel -> {
                    showLoadingDialog();
                    viewOrderModel.getDistanceIsLessThan50M(this, orderModel.getLatitude(), orderModel.getLongitude(), isWithin50Meters -> {
                        dismissLoadingDialog();
                        if (isWithin50Meters) {
                            startActivity(new Intent(MainActivity.this, DeliveryActivity.class).putExtra("orderId", orderModel.getOrder_id()));
                        } else {
                            Toast.makeText(MainActivity.this, "Delivery location is too far!", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
                binding.orderRv.setAdapter(adapter);
                adapter.submitList(dbOrderModels);
            });
        });
    }

    private void openFilterDialog() {
        FilterDialogBinding binding = FilterDialogBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(MainActivity.this, R.style.SheetDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());

        if (filterType == R.id.radioPending) {
            binding.radioPending.setChecked(true);
        } else if (filterType == R.id.radioDelivered) {
            binding.radioDelivered.setChecked(true);
        } else {
            binding.radioAll.setChecked(true);
        }

        binding.applyBt.setOnClickListener(v -> {
            filterType = binding.radioGroupStatus.getCheckedRadioButtonId();
            if (filterType == R.id.radioPending) {
                viewOrderModel.getObservableListPending(this::handleAdapter);
            } else if (filterType == R.id.radioDelivered) {
                viewOrderModel.getObservableListDelivered(this::handleAdapter);
            } else {
                viewOrderModel.getObservableListAll(this::handleAdapter);
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openSortDialog() {
        SortDialogBinding binding = SortDialogBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(MainActivity.this, R.style.SheetDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());

        if (sortType == R.id.radioAsc) {
            binding.radioAsc.setChecked(true);
        } else if (sortType == R.id.radioDesc) {
            binding.radioDesc.setChecked(true);
        }

        binding.applyBt.setOnClickListener(v -> {
            sortType = binding.radioGroupStatus.getCheckedRadioButtonId();
            if (sortType == R.id.radioAsc) {
                viewOrderModel.getObservableListAsc(this::handleAdapter);
            } else if (sortType == R.id.radioDesc) {
                viewOrderModel.getObservableListDesc(this::handleAdapter);
            } else {
                viewOrderModel.getObservableListAll(this::handleAdapter);
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showLoadingDialog() {
        loadingDialog.show();
    }

    private void dismissLoadingDialog() {
        loadingDialog.dismiss();
    }


}