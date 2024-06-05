package com.example.ominformatics2.UI.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ominformatics2.DataSource.BoolForDataBase;
import com.example.ominformatics2.DataSource.DbOrderModel;
import com.example.ominformatics2.DataSource.DeliveryStatus;
import com.example.ominformatics2.DataSource.MyApplication;
import com.example.ominformatics2.R;
import com.example.ominformatics2.UI.Utils;
import com.example.ominformatics2.databinding.ActivityDeliveryBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class DeliveryActivity extends AppCompatActivity {

    private ActivityDeliveryBinding binding;
    private int orderId;
    private DbOrderModel order;
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap capturedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        orderId = getIntent().getIntExtra("orderId", 0);

        MyApplication.getExecutorSingleService().submit(() -> {
            order = MyApplication.getOrderDao().getOrderById(orderId);
            return setUI();
        });

        // Handle radio button change
        binding.radioGroupStatus.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioDamaged) {
                binding.damageDetailsTitle.setVisibility(View.VISIBLE);
                binding.damageDetails.setVisibility(View.VISIBLE);
                binding.damageAmount.setVisibility(View.VISIBLE);
            } else {
                binding.damageDetailsTitle.setVisibility(View.GONE);
                binding.damageDetails.setVisibility(View.GONE);
                binding.damageAmount.setVisibility(View.GONE);
            }
        });

        binding.btnTakePicture.setOnClickListener(v -> openCamera());

        // Submit button click
        binding.btnSubmit.setOnClickListener(v -> {
            if (validateIfDamaged()) {
                if ((order.getImageUrl() == null)) {
                    saveImageToCacheDirectory();
                } else {
                    updateDataInDB();
                }
            }
        });
    }

    private Object setUI() {
        binding.orderNo.setText("Order No.: " + order.getOrder_no());
        binding.customerName.setText("Customer Name:  " + order.getCustomer_name());
        binding.address.setText("Delivery Address: " + order.getAddress());
        binding.deliveryCharge.setText("Amount to be collected: ₹" + order.getDelivery_cost());

        if (!order.getImageUrl().isEmpty())
            binding.imagePreview.setImageURI(Uri.parse(order.getImageUrl()));
        binding.damageDetails.setText(order.getDamageDesc());
        binding.damageAmount.setText(String.valueOf(order.getAnotherAmt()));
        if (order.getIsDamaged().equals(BoolForDataBase.TRUE.getStatus())) {
            binding.radioGroupStatus.check(R.id.radioDamaged);
            binding.damageDetailsTitle.setVisibility(View.VISIBLE);
            binding.damageDetails.setVisibility(View.VISIBLE);
            binding.damageAmount.setVisibility(View.VISIBLE);
        } else {
            binding.radioGroupStatus.check(R.id.radioGood);
            binding.damageDetailsTitle.setVisibility(View.GONE);
            binding.damageDetails.setVisibility(View.GONE);
            binding.damageAmount.setVisibility(View.GONE);
        }
        return "UI Set Up Completed";
    }

    private void openCamera() {
        if (Utils.isCameraPermissionGranted(this)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Utils.requestCameraPermission(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            capturedBitmap = (Bitmap) extras.get("data");
            binding.imagePreview.setImageBitmap(capturedBitmap);
            binding.btnTakePicture.setText("Retake");
        }
    }

    private void saveImageToCacheDirectory() {
        if (capturedBitmap == null) {
            Toast.makeText(this, "No image captured", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File file = new File(getCacheDir(), "delivered_parcel_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            capturedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            order.setImageUrl(file.getAbsolutePath());
            updateDataInDB();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDataInDB() {
        if (binding.radioGroupStatus.getCheckedRadioButtonId() == R.id.radioDamaged) {
            order.setDamageDesc(binding.damageDetails.getText().toString());
            order.setAnotherAmt(Double.parseDouble(binding.damageAmount.getText().toString()));
        }

        order.setIsDamaged(
                binding.radioGroupStatus.getCheckedRadioButtonId() == R.id.radioDamaged ?
                        BoolForDataBase.TRUE.getStatus() :
                        BoolForDataBase.FALSE.getStatus()
        );
        order.setDelivery_status(DeliveryStatus.Delivered.getStatus());
        MyApplication.getExecutorSingleService().execute(() -> MyApplication.getOrderDao().update(order));
        // Proceed with submission
        Toast.makeText(getApplicationContext(), "Form submitted successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean validateIfDamaged() {

        if (binding.radioGroupStatus.getCheckedRadioButtonId() == R.id.radioDamaged) {
            if (binding.damageDetails.getText().toString().isEmpty()) {
                binding.damageDetails.setError("Please specify damage!");
                return false;
            }
            if (binding.damageAmount.getText().toString().isEmpty()) {
                binding.damageAmount.setError("Please enter damage amount!");
                return false;
            }
        }

        return true;
    }
}
