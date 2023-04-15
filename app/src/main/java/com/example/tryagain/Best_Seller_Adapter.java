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

public class Best_Seller_Adapter extends RecyclerView.Adapter<Best_Seller_Adapter.BestsellerViewHolder> {
    private final ArrayList<Best_Seller_Product> best_seller_productArrayList;
    private final ItemClickListener itemClickListener;

    public Best_Seller_Adapter(ArrayList<Best_Seller_Product> best_seller_productArrayList, ItemClickListener itemClickListener) {
        this.best_seller_productArrayList = best_seller_productArrayList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public BestsellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_best_seller, parent, false);
        return new BestsellerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BestsellerViewHolder holder, int position) {
        Best_Seller_Product product = best_seller_productArrayList.get(position);
        holder.tv_name.setText(product.getName());
        NumberFormat formatter = new DecimalFormat("#,###");
        holder.tv_price.setText(String.format("%sđ", formatter.format(product.getPrice())));

        Glide.with(holder.iv_bestseller.getContext())
                .load(product.getImage())
                .override(124, 126)
                .into(holder.iv_bestseller);
        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(best_seller_productArrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        if (best_seller_productArrayList != null){
            return best_seller_productArrayList.size();
        }
        return 0;
    }
    public interface ItemClickListener {
        void onItemClick(Best_Seller_Product best_seller_product);
    }
    public static class BestsellerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_bestseller;
        private final TextView tv_name, tv_price;
        public BestsellerViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_bestseller = itemView.findViewById(R.id.imv_popular);
            tv_name = itemView.findViewById(R.id.tv_name_popular);
            tv_price = itemView.findViewById(R.id.tv_price_popular);
        }
    }
}
