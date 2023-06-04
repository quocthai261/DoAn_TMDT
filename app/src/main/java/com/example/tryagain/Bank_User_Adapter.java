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

public class Bank_User_Adapter extends RecyclerView.Adapter<Bank_User_Adapter.Bank_User_ViewHolder>{
    private final ArrayList<Bank_Payment> bank_userArrayList;
    private final ItemClickListener itemClickListener;

    public Bank_User_Adapter(ArrayList<Bank_Payment> bank_userArrayList, ItemClickListener itemClickListener) {
        this.bank_userArrayList = bank_userArrayList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public Bank_User_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_bank, parent, false);
        return new Bank_User_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Bank_User_ViewHolder holder, int position) {
        Bank_Payment bank_user = bank_userArrayList.get(position);
        if (bank_user == null){
            return;
        }
        holder.tv_user_name_bank.setText(String.format("%s - %s", bank_user.getShort_Name(), bank_user.getFull_Name()));
        holder.tv_user_bank_number.setText(Bank_Payment_Adapter.FormatBankNumber(bank_user.getNumber()));
        Glide.with(holder.imv_user_logo_bank.getContext())
                .load(bank_user.getLogo())
                .override(50, 50)
                .into(holder.imv_user_logo_bank);
        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(bank_userArrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        if (bank_userArrayList != null) {
            return bank_userArrayList.size();
        }
        return 0;
    }

    public interface ItemClickListener {
        void onItemClick(Bank_Payment bank_user);
    }

    public static class Bank_User_ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_user_name_bank, tv_user_bank_number;
        private final ImageView imv_user_logo_bank;
        public Bank_User_ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_user_name_bank = itemView.findViewById(R.id.tv_user_name_bank);
            tv_user_bank_number = itemView.findViewById(R.id.tv_user_bank_number);
            imv_user_logo_bank = itemView.findViewById(R.id.imv_user_logo_bank);
        }
    }
}
