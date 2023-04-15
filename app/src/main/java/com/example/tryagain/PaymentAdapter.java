package com.example.tryagain;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private final ArrayList<Payment> paymentArrayList;

    public PaymentAdapter(ArrayList<Payment> paymentArrayList) {
        this.paymentArrayList = paymentArrayList;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new PaymentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = paymentArrayList.get(position);
        if (payment == null) {
            return;
        }
        holder.tv_name_product_payment.setText(payment.getName());
        holder.tv_price_payment.setText(String.format("%sđ", ConvertPriceToString(Double.parseDouble(payment.getPrice()))));
        holder.tv_option_product_payment.setText(String.format("Phân loại: size %s", payment.getSize()));
        holder.tv_quantity_payment.setText(String.format("x%s", ConvertPriceToString(Double.parseDouble(payment.getQuantity()))));
        Glide.with(holder.imv_image_product_payment.getContext())
                .load(payment.getImage())
                .override(70, 70)
                .into(holder.imv_image_product_payment);
    }

    public String ConvertPriceToString(double a) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(a);
    }

    @Override
    public int getItemCount() {
        if (paymentArrayList != null) {
            return paymentArrayList.size();
        }
        return 0;
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imv_image_product_payment;
        private final TextView tv_name_product_payment, tv_price_payment, tv_option_product_payment, tv_quantity_payment;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            imv_image_product_payment = itemView.findViewById(R.id.imv_image_product_payment);
            tv_name_product_payment = itemView.findViewById(R.id.tv_name_product_payment);
            tv_price_payment = itemView.findViewById(R.id.tv_price_payment);
            tv_option_product_payment = itemView.findViewById(R.id.tv_option_product_payment);
            tv_quantity_payment = itemView.findViewById(R.id.tv_quantity_payment);
        }
    }
}
