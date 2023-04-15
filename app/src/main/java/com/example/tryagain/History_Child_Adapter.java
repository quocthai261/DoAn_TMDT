package com.example.tryagain;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class History_Child_Adapter extends RecyclerView.Adapter<History_Child_Adapter.History_Child_ViewHolder> {

    private final List<History_Child> historyChildList;
    public History_Child_Adapter(List<History_Child> historyChildList) {
        this.historyChildList = historyChildList;
    }

    @NonNull
    @Override
    public History_Child_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_child, parent, false);
        return new History_Child_ViewHolder(v);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull History_Child_ViewHolder holder, int position) {
        History_Child history_child = historyChildList.get(position);
        holder.tv_history_child_date.setText(history_child.getHistory_child_date());
        holder.tv_history_child_quantity
                .setText(String.format("%s sản phẩm", history_child.getHistory_child_total_quantity()));
        holder.tv_history_child_total_price
                .setText(String.format("%sđ", ConvertPriceToString(Double.parseDouble(history_child.getHistory_child_total_price()))));

        History_Item_Adapter history_item_adapter;
        history_item_adapter = new History_Item_Adapter(history_child.getHistoryItemList());
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.rcv_history_child.getContext(),
                LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(holder.rcv_history_child.getContext(), DividerItemDecoration.VERTICAL);
        holder.rcv_history_child.addItemDecoration(itemDecoration);
        holder.rcv_history_child.setLayoutManager(layoutManager);
        holder.rcv_history_child.setAdapter(history_item_adapter);
        history_item_adapter.notifyDataSetChanged();
    }

    private String ConvertPriceToString(double a) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(a);
    }

    @Override
    public int getItemCount() {
        if (historyChildList != null) {
            return historyChildList.size();
        }
        return 0;
    }

    public static class History_Child_ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_history_child_date, tv_history_child_quantity, tv_history_child_total_price;
        private final RecyclerView rcv_history_child;

        public History_Child_ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_history_child_date = itemView.findViewById(R.id.tv_history_child_date);
            tv_history_child_quantity = itemView.findViewById(R.id.tv_history_child_quantity);
            tv_history_child_total_price = itemView.findViewById(R.id.tv_history_child_total_price);
            rcv_history_child = itemView.findViewById(R.id.rcv_history_child);
        }
    }
}
