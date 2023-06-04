package com.example.tryagain;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Bank_Payment_Adapter extends RecyclerView.Adapter<Bank_Payment_Adapter.Bank_Payment_ViewHolder> {

    private final ArrayList<Bank_Payment> bank_paymentArrayList;
    private final onClickListener onClickListener;
    public int selectedPosition = -1;

    public Bank_Payment_Adapter(ArrayList<Bank_Payment> bank_paymentArrayList, Bank_Payment_Adapter.onClickListener onClickListener) {
        this.bank_paymentArrayList = bank_paymentArrayList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Bank_Payment_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank_payment, parent, false);
        return new Bank_Payment_ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull Bank_Payment_ViewHolder holder, int position) {
        Bank_Payment bank_payment = bank_paymentArrayList.get(position);
        holder.tv_short_name_bank_payment.setText(bank_payment.getShort_Name());
        holder.tv_bank_number_payment.setText(FormatBankNumber(bank_payment.getNumber()));
        Glide.with(holder.imv_logo_bank_payment.getContext())
                .load(bank_payment.getLogo())
                .override(30, 30)
                .into(holder.imv_logo_bank_payment);

        holder.cb_select_bank.setOnCheckedChangeListener(null);
        holder.cb_select_bank.setChecked(selectedPosition == position);
        holder.cb_select_bank.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });
        holder.itemView.setOnClickListener(v -> {
            onClickListener.onItemClick(bank_paymentArrayList.get(position));
            holder.cb_select_bank.setChecked(true);
        });
    }

    @Override
    public int getItemCount() {
        if (bank_paymentArrayList != null) {
            return bank_paymentArrayList.size();
        }
        return 0;
    }

    public interface onClickListener {
        void onItemClick(Bank_Payment bank_payment);
    }

    public static class Bank_Payment_ViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox cb_select_bank;
        private final ImageView imv_logo_bank_payment;
        private final TextView tv_short_name_bank_payment, tv_bank_number_payment;

        public Bank_Payment_ViewHolder(@NonNull View itemView) {
            super(itemView);

            cb_select_bank = itemView.findViewById(R.id.cb_select_bank);
            imv_logo_bank_payment = itemView.findViewById(R.id.imv_logo_bank_payment);
            tv_short_name_bank_payment = itemView.findViewById(R.id.tv_short_name_bank_payment);
            tv_bank_number_payment = itemView.findViewById(R.id.tv_bank_number_payment);
        }
    }

    public static String FormatBankNumber(String a) {
        return "*" + a.substring(a.length() - 4);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void UnSelectAll() {
        selectedPosition = -1;
        notifyDataSetChanged();
    }
}
