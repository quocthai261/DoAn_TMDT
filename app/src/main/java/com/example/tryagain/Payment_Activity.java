package com.example.tryagain;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Payment_Activity extends AppCompatActivity {

    private String quantity;
    double getprice, pay;
    private TextView tv_user_name, tv_user_phone_number, tv_user_address, tv_add_user_bank;
    private CheckBox cb_auto, cb_momo;
    private ArrayList<Payment> paymentArrayList;
    private PaymentAdapter paymentAdapter;

    private ArrayList<Bank_Payment> bank_paymentArrayList;
    private Bank_Payment_Adapter bank_payment_adapter;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference Ref, mRef, mRef1;
    FirebaseFirestore firestore;

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        String price = intent.getStringExtra("price");
        quantity = intent.getStringExtra("quantity");
        getprice = intent.getDoubleExtra("getprice", 0.0);

        tv_user_name = findViewById(R.id.user_name);
        tv_user_phone_number = findViewById(R.id.user_phone_number);
        tv_user_address = findViewById(R.id.user_address);
        TextView tv_price = findViewById(R.id.total_price);
        TextView tv_quantity = findViewById(R.id.quantity);
        Button btn_payment = findViewById(R.id.btn_payment);

        TextView tv_total_price = findViewById(R.id.total_price_last);
        TextView tv_total_price_product = findViewById(R.id.total_price_product);
        TextView tv_total_price_pay = findViewById(R.id.total_price_pay);

        tv_price.setText(price);
        tv_quantity.setText(String.format("Tổng số tiền (%s sản phẩm):", quantity));

        tv_total_price_product.setText(price);
        pay = getprice + 30000.0;

        tv_total_price_pay.setText(String.format("%sđ", ConvertPriceToString(pay)));
        tv_total_price.setText(String.format("%sđ", ConvertPriceToString(pay)));

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Cart");
        mRef1 = FirebaseDatabase.getInstance().getReference().child("History");
        Ref = FirebaseDatabase.getInstance().getReference().child("Users");

        Ref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_user_name.setText(snapshot.child("Name").getValue(String.class));
                tv_user_phone_number.setText(snapshot.child("Phone").getValue(String.class));
                tv_user_address.setText(snapshot.child("Address").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        RecyclerView rcv_payment = findViewById(R.id.rcv_payment);
        rcv_payment.setLayoutManager(new LinearLayoutManager(this));
        paymentArrayList = new ArrayList<>();
        paymentAdapter = new PaymentAdapter(paymentArrayList);
        rcv_payment.setAdapter(paymentAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcv_payment.addItemDecoration(itemDecoration);

        RecyclerView rcv_bank_payment = findViewById(R.id.rcv_bank_payment);
        rcv_bank_payment.setLayoutManager(new LinearLayoutManager(this));
        bank_paymentArrayList = new ArrayList<>();
        bank_payment_adapter = new Bank_Payment_Adapter(bank_paymentArrayList, bank_payment -> {
            cb_auto.setChecked(false);
            cb_momo.setChecked(false);
        });
        rcv_bank_payment.setAdapter(bank_payment_adapter);
        rcv_bank_payment.addItemDecoration(itemDecoration);

        mRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Cart cart = dataSnapshot.getValue(Cart.class);
                    String Name = cart.getName();
                    String Image = cart.getImage();
                    String Size = cart.getSize();
                    String Price = cart.getPrice();
                    String Id = cart.getId();
                    String Quantity = cart.getQuantity();
                    Payment payment = new Payment(Name, Size, Image, Price, Id, Quantity);
                    paymentArrayList.add(payment);
                }
                paymentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Ref.child(user.getUid()).child("Bank").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Bank_Payment bank_payment = dataSnapshot.getValue(Bank_Payment.class);
                    bank_paymentArrayList.add(bank_payment);
                }
                bank_payment_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        btn_payment.setOnClickListener(v -> {
            mRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Update_Warehouse("Hats", dataSnapshot);
                        Update_Warehouse("Pants", dataSnapshot);
                        Update_Warehouse("Shirts", dataSnapshot);
                        Update_Warehouse("Shoes", dataSnapshot);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                }
            });

            Push_History_Data();
            mRef.child(user.getUid()).removeValue();
            paymentAdapter.notifyDataSetChanged();
            Intent intent1 = new Intent(Payment_Activity.this, End_Process_Activity.class);
            startActivity(intent1);
            finishAndRemoveTask();
        });

        cb_auto = findViewById(R.id.cb_auto);
        cb_momo = findViewById(R.id.cb_momo);

        if (bank_payment_adapter.selectedPosition != -1) {
            cb_auto.setChecked(false);
            cb_momo.setChecked(false);
            Log.d("CHECCK3", "aaaaa");

        }
        cb_auto.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                cb_momo.setChecked(false);
                bank_payment_adapter.UnSelectAll();
            }
        });
        cb_momo.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                cb_auto.setChecked(false);
                bank_payment_adapter.UnSelectAll();
            }
        });
        tv_add_user_bank = findViewById(R.id.tv_add_user_bank);
        tv_add_user_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Payment_Activity.this, Bank_Activity.class);
                intent1.putExtra("Add_Bank", "Payment_Activity");
                startActivity(intent1);
            }
        });
    }


    private void Update_Warehouse(String collection, DataSnapshot dataSnapshot) {
        firestore.collection(collection)
                .whereEqualTo("Id", dataSnapshot.child("Id")
                        .getValue(String.class)).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            int warehouse = Integer.parseInt(product.getWarehouse());
                            int quantity = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("Quantity").getValue(String.class)));
                            int rs = warehouse - quantity;
                            if (rs >= 0) {
                                firestore.collection(collection).document(document.getId()).update("Warehouse", String.valueOf(rs));
                            } else {
                                Toast.makeText(Payment_Activity.this, "Sản phẩm trong kho đã hết", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void Push_History_Data() {
        for (Payment payment : paymentArrayList) {
            String date = realtimevalue();
            mRef1.child(user.getUid()).child(date).child("History Product")
                    .child(payment.getId() + "_size_" + payment.getSize()).setValue(payment);

            Map<String, Object> History = new HashMap<>();
            History.put("History Date", Date_Payment());
            History.put("Total Quantity", Total_Quantity_Payment());
            History.put("Total Price", Total_Price_Payment());
            mRef1.child(user.getUid()).child(date).updateChildren(History);
        }
    }

    private String Total_Price_Payment() {
        Intent intent = getIntent();
        getprice = intent.getDoubleExtra("getprice", 0.0);
        double pay = getprice + 30000.0;
        return String.valueOf(pay);
    }

    private String Total_Quantity_Payment() {
        Intent intent = getIntent();
        return quantity = intent.getStringExtra("quantity");
    }

    public String ConvertPriceToString(double a) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(a);
    }

    private String realtimevalue() {
        Date dateNtime = Calendar.getInstance().getTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String time = timeFormat.format(dateNtime);
        String date = dateFormat.format(dateNtime);
        return date + time;
    }

    public String Date_Payment() {
        Date dateNtime = Calendar.getInstance().getTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String time = timeFormat.format(dateNtime);
        String date = dateFormat.format(dateNtime);
        return date + " - " + time;
    }
}