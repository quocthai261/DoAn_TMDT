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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final ArrayList<Category> categoryArrayList;

    private final ItemClickListener mItemListener;

    public CategoryAdapter(ArrayList<Category> categoryArrayList, ItemClickListener mItemListener) {
        this.categoryArrayList = categoryArrayList;
        this.mItemListener = mItemListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryArrayList.get(position);
        holder.tv_name_category.setText(category.getName());
        Glide.with(holder.imv_category.getContext())
                .load(category.getImage())
                .override(190, 190)
                .into(holder.imv_category);
        holder.itemView.setOnClickListener(v -> mItemListener.onItemClick(categoryArrayList.get(position)));

    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public interface ItemClickListener {
        void onItemClick(Category category);
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name_category;
        private final ImageView imv_category;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name_category = itemView.findViewById(R.id.tv_name_category);
            imv_category = itemView.findViewById(R.id.imv_category);
        }
    }
}
