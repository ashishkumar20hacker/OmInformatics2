package com.example.ominformatics2.UI.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ominformatics2.DataSource.DbOrderModel;
import com.example.ominformatics2.DataSource.DeliveryStatus;
import com.example.ominformatics2.databinding.ItemOrderBinding;

public class OrderAdapter extends ListAdapter<DbOrderModel, OrderAdapter.ViewHolder> {

    private final Context context;
    private final OnItemClickListener onItemClick;

    public interface OnItemClickListener {
        void onItemClick(DbOrderModel orderModel);
    }

    public OrderAdapter(Context context, OnItemClickListener onItemClick) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DbOrderModel orderModel = getItem(position);
        if (orderModel == null) return;

        if (orderModel.getDelivery_status().equals(DeliveryStatus.Delivered.getStatus())) {
            holder.binding.deliveryStatusTv.setTextColor(Color.GREEN);
        } else {
            holder.binding.deliveryStatusTv.setTextColor(Color.parseColor("#058EF7"));
        }
        holder.binding.orderNoTv.setText(orderModel.getOrder_no());
        holder.binding.customerNameTv.setText(orderModel.getCustomer_name());
        holder.binding.deliveryStatusTv.setText(orderModel.getDelivery_status());
        holder.binding.amtTv.setText("₹" + orderModel.getDelivery_cost());

        holder.itemView.setOnClickListener(v -> onItemClick.onItemClick(orderModel));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemOrderBinding binding;

        public ViewHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static final DiffUtil.ItemCallback<DbOrderModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<DbOrderModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull DbOrderModel oldItem, @NonNull DbOrderModel newItem) {
            return oldItem.getOrder_id() == newItem.getOrder_id();
        }

        @Override
        public boolean areContentsTheSame(@NonNull DbOrderModel oldItem, @NonNull DbOrderModel newItem) {
            return oldItem.getDelivery_status().equals(newItem.getDelivery_status());
        }
    };
}
