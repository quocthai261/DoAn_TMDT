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
import java.util.List;

public class History_Item_Adapter extends RecyclerView.Adapter<History_Item_Adapter.HistoryViewHolder> {

    private final List<History_Item> historyItemList;

    public History_Item_Adapter(List<History_Item> historyItemList) {
        this.historyItemList = historyItemList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History_Item historyItem = historyItemList.get(position);

        holder.tv_name_product_history.setText(historyItem.getName());
        holder.tv_price_history.setText(String.format("%sđ", ConvertPriceToString(Double.parseDouble(historyItem.getPrice()))));
        holder.tv_option_product_history.setText(String.format("Phân loại: size %s", historyItem.getSize()));
        holder.tv_quantity_history.setText(String.format("x%s", ConvertPriceToString(Double.parseDouble(historyItem.getQuantity()))));
        Glide.with(holder.imv_image_product_history.getContext())
                .load(historyItem.getImage())
                .override(60, 60)
                .into(holder.imv_image_product_history);
    }

    public String ConvertPriceToString(double a) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(a);
    }

    @Override
    public int getItemCount() {
        if (historyItemList != null) {
            return historyItemList.size();
        }
        return 0;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imv_image_product_history;

        private final TextView tv_name_product_history, tv_price_history, tv_option_product_history, tv_quantity_history;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imv_image_product_history = itemView.findViewById(R.id.imv_image_product_history);
            tv_name_product_history = itemView.findViewById(R.id.tv_name_product_history);
            tv_price_history = itemView.findViewById(R.id.tv_price_history);
            tv_option_product_history = itemView.findViewById(R.id.tv_option_product_history);
            tv_quantity_history = itemView.findViewById(R.id.tv_quantity_history);
        }
    }
}
