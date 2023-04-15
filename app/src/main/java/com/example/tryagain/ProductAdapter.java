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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final ArrayList<Product> productArrayList;
    private final ItemClickListener mItemListener;

    public ProductAdapter(ArrayList<Product> productArrayList, ItemClickListener mItemListener) {
        this.productArrayList = productArrayList;
        this.mItemListener = mItemListener;
    }

    @Override
    public int getItemViewType(int position) {
        Product product = productArrayList.get(position);
        return product.getTypeDisplay();
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_grid, parent, false);
        switch (viewType) {
            case Product.TYPE_GRID:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_grid, parent, false);
                break;
            case Product.TYPE_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list, parent, false);
                break;
        }
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        if (product == null) {
            return;
        }
        holder.tv_Name.setText(product.getName());
        NumberFormat formatter = new DecimalFormat("#,###");
        holder.tv_Price.setText(String.format("%sđ", formatter.format(product.getPrice())));
        holder.tv_Location.setText(product.getLocation());
        Glide.with(holder.imv_Product.getContext())
                .load(product.getImage())
                .override(190, 190)
                .into(holder.imv_Product);
        holder.itemView.setOnClickListener(v -> mItemListener.onItemClick(productArrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        if (productArrayList != null) {
            return productArrayList.size();
        }
        return 0;
    }

    public interface ItemClickListener {
        void onItemClick(Product product);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_Name, tv_Price, tv_Location;
        private final ImageView imv_Product;

        public ProductViewHolder(View itemView) {
            super(itemView);
            tv_Name = itemView.findViewById(R.id.tv_name_product);
            tv_Price = itemView.findViewById(R.id.tv_price_product);
            tv_Location = itemView.findViewById(R.id.tv_location_product);
            imv_Product = itemView.findViewById(R.id.imv_product);
        }
    }
}
