package com.example.tryagain;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;

public class Cart_Activity extends AppCompatActivity {

    private Button btn_payment;
    private TextView tv_total_price;
    private ArrayList<Cart> cartArrayList;
    CartAdapter cartAdapter;
    Product product_intent;
    FirebaseAuth auth;
    FirebaseUser user;
    double get_total_price;
    private String price, quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        tv_total_price = findViewById(R.id.tv_total_price);
        btn_payment = findViewById(R.id.btn_payment);
        RecyclerView rcv_cart = findViewById(R.id.rcv_cart);
        rcv_cart.setLayoutManager(new LinearLayoutManager(this));
        cartArrayList = new ArrayList<>();
        cartAdapter = new CartAdapter(cartArrayList);
        rcv_cart.setAdapter(cartAdapter);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        product_intent = (Product) getIntent().getSerializableExtra("Product");
        DatabaseReference Cart_Ref = FirebaseDatabase.getInstance().getReference().child("Cart");

        Cart_Ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double total_price = 0.0;
                double total_quantity = 0.0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Cart cart = dataSnapshot.getValue(Cart.class);
                    cartArrayList.add(cart);
                    if (cart != null) {
                        total_price = total_price + Double.parseDouble(cart.getTotal_Price());
                        total_quantity = total_quantity + Double.parseDouble(cart.getQuantity());
                        tv_total_price.setText(String.format("%sđ", ConvertPriceToString(total_price)));
                        btn_payment.setText(String.format("Mua hàng (%s)", ConvertPriceToString(total_quantity)));
                        price = tv_total_price.getText().toString();
                        quantity = cartAdapter.ConvertPriceToString(total_quantity);
                        get_total_price = total_price;
                    }
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Cart_Ref.child(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                if (cartArrayList.isEmpty() || cartArrayList == null) {
                    tv_total_price.setText("0đ");
                    btn_payment.setText("Mua hàng (0)");
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_payment = findViewById(R.id.btn_payment);

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String key = dataSnapshot.getKey();
                            if (Objects.equals(key, user.getUid())) {
                                Intent intent = new Intent(Cart_Activity.this, Payment_Activity.class);
                                intent.putExtra("price", price);
                                intent.putExtra("quantity", quantity);
                                intent.putExtra("getprice", get_total_price);
                                startActivity(intent);
                                finishAndRemoveTask();
                            }

                            cartAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public String ConvertPriceToString(double a) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(a);
    }
}