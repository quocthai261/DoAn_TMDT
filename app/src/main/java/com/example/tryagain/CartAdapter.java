package com.example.tryagain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    FirebaseAuth auth;
    FirebaseUser user;
    private final ArrayList<Cart> cartArrayList;

    public CartAdapter(ArrayList<Cart> cartArrayList) {
        this.cartArrayList = cartArrayList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        Cart cart = cartArrayList.get(position);
        holder.tv_name_product_cart.setText(cart.getName());
        holder.tv_price_cart.setText(String.format("%sđ", ConvertPriceToString(Double.parseDouble(cart.getPrice()))));
        holder.tv_option_product_cart.setText(String.format("Phân loại: size %s", cart.getSize()));
        holder.tv_quantity.setText(ConvertPriceToString(Double.parseDouble(cart.getQuantity())));
        holder.tv_total_price.setText(String.format("%sđ", ConvertPriceToString(Double.parseDouble(cart.getTotal_Price()))));
        Glide.with(holder.imv_image_product_cart.getContext())
                .load(cart.getImage())
                .override(96, 96)
                .into(holder.imv_image_product_cart);
        holder.tv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String present_value_string = holder.tv_quantity.getText().toString();
                int present_value_int = Integer.parseInt(present_value_string);
                present_value_int--;
                if (present_value_int < 1) {
                    new AlertDialog.Builder(v.getContext())
                            .setMessage("Bạn chắc chắn muốn bỏ sản phẩm này?")
                            .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cartArrayList.clear();
                                    FirebaseDatabase.getInstance().getReference("Cart").child(user.getUid()).child(cart.getId() + "_size_" + cart.getSize()).removeValue();
                                }
                            })
                            .setNegativeButton("Không", null).show();
                    present_value_int = 1;
                }
                holder.tv_quantity.setText(String.valueOf(present_value_int));
                double quantity = Double.parseDouble(holder.tv_quantity.getText().toString());
                double price = Double.parseDouble(cart.getPrice());
                double T_price = quantity * price;
                cartArrayList.clear();
                cart.setQuantity(String.valueOf(quantity));
                cart.setTotal_Price(String.valueOf(T_price));
                FirebaseDatabase.getInstance().getReference("Cart").child(user.getUid())
                        .child(cart.getId() + "_size_" + cart.getSize())
                        .setValue(cart);
            }
        });

        holder.tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String present_value_string = holder.tv_quantity.getText().toString();
                int present_value_int = Integer.parseInt(present_value_string);
                present_value_int++;
                holder.tv_quantity.setText(String.valueOf(present_value_int));
                double quantity = Double.parseDouble(holder.tv_quantity.getText().toString());
                double price = Double.parseDouble(cart.getPrice());
                double T_price = quantity * price;
                cartArrayList.clear();
                cart.setQuantity(String.valueOf(quantity));
                cart.setTotal_Price(String.valueOf(T_price));
                FirebaseDatabase.getInstance().getReference("Cart").child(user.getUid())
                        .child(cart.getId() + "_size_" + cart.getSize())
                        .setValue(cart);
            }
        });
        holder.i_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setMessage("Bạn chắc chắn muốn bỏ sản phẩm này?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FirebaseDatabase.getInstance().getReference("Cart").child(user.getUid()).child(cart.getId() + "_size_" + cart.getSize()).removeValue();
                                cartArrayList.clear();
                            }

                        })
                        .setNegativeButton("Không", null).show();
            }
        });
    }

    public String ConvertPriceToString(double a) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(a);
    }


    @Override
    public int getItemCount() {
        if (cartArrayList != null) {
            return cartArrayList.size();
        }
        return 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imv_image_product_cart;
        private final TextView tv_name_product_cart, tv_price_cart, tv_option_product_cart, tv_quantity, tv_total_price, tv_minus, tv_plus;
        private final ImageButton i_btn_delete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imv_image_product_cart = itemView.findViewById(R.id.imv_image_product_cart);
            tv_name_product_cart = itemView.findViewById(R.id.tv_name_product_cart);
            tv_price_cart = itemView.findViewById(R.id.tv_price_cart);
            tv_option_product_cart = itemView.findViewById(R.id.tv_option_product_cart);
            tv_quantity = itemView.findViewById(R.id.tv_count);
            tv_total_price = itemView.findViewById(R.id.tv_total_price);
            tv_minus = itemView.findViewById(R.id.tv_minus);
            tv_plus = itemView.findViewById(R.id.tv_plus);
            i_btn_delete = itemView.findViewById(R.id.i_btn_delete);
        }
    }
}
