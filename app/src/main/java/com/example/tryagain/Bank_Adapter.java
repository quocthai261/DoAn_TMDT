package com.example.tryagain;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Bank_Adapter extends RecyclerView.Adapter<Bank_Adapter.BankViewHolder>{

    private final ArrayList<Bank> bankArrayList;
    private final ItemClickListener itemClickListener;

    public Bank_Adapter(ArrayList<Bank> bankArrayList, ItemClickListener itemClickListener) {
        this.bankArrayList = bankArrayList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank, parent, false);
        return new BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankViewHolder holder, int position) {
        Bank bank = bankArrayList.get(position);
        if (bank == null){
            return;
        }
        holder.tv_full_name.setText(bank.getFull_Name());
        holder.tv_short_name.setText(bank.getShort_Name());
        Glide.with(holder.imv_logo.getContext())
                .load(bank.getLogo())
                .override(50, 50)
                .into(holder.imv_logo);
        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(bankArrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        if (bankArrayList != null) {
            return bankArrayList.size();
        }
        return 0;
    }

    public interface ItemClickListener {
        void onItemClick(Bank bank);
    }

    public static class BankViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_full_name, tv_short_name;
        private final ImageView imv_logo;

        public BankViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_full_name = itemView.findViewById(R.id.tv_full_name_bank);
            tv_short_name = itemView.findViewById(R.id.tv_short_name_bank);
            imv_logo = itemView.findViewById(R.id.imv_logo_bank);
        }
    }
}
