package com.example.tryagain;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Payment_Activity extends AppCompatActivity {
    private String price, quantity;
    double getprice;
    private Button btn_payment;
    private TextView tv_user_name, tv_user_phone_number, tv_user_address, tv_price, tv_quantity, tv_total_price_product, tv_total_price_pay, tv_total_price;
    private ArrayList<Payment> paymentArrayList;
    private PaymentAdapter paymentAdapter;
    FirebaseAuth auth;

    FirebaseUser user;
    DatabaseReference Ref, mRef, mRef1;
    RecyclerView rcv_payment;
    ImageView imv_card_payment;
    RadioGroup radioGroup;
    RadioButton vietin_rad, vietcom_rad, mb_rad, non_rad;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        price = intent.getStringExtra("price");
        quantity = intent.getStringExtra("quantity");
        getprice = intent.getDoubleExtra("getprice", 0.0);


        tv_user_name = findViewById(R.id.user_name);
        tv_user_phone_number = findViewById(R.id.user_phone_number);
        tv_user_address = findViewById(R.id.user_address);
        tv_price = findViewById(R.id.total_price);
        tv_quantity = findViewById(R.id.quantity);
        btn_payment = findViewById(R.id.btn_payment);


        tv_total_price = findViewById(R.id.total_price_last);
        tv_total_price_product = findViewById(R.id.total_price_product);
        tv_total_price_pay = findViewById(R.id.total_price_pay);


        tv_price.setText(price);
        tv_quantity.setText(String.format("Tổng số tiền (%s sản phẩm):", quantity));

        tv_total_price_product.setText(price);
        double pay = getprice + 30000.0;

        tv_total_price_pay.setText(String.format("%sđ", ConvertPriceToString(pay)));
        tv_total_price.setText(String.format("%sđ", ConvertPriceToString(pay)));


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

        rcv_payment = findViewById(R.id.rcv_payment);
        rcv_payment.setLayoutManager(new LinearLayoutManager(this));
        paymentArrayList = new ArrayList<>();
        paymentAdapter = new PaymentAdapter(paymentArrayList);
        rcv_payment.setAdapter(paymentAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcv_payment.addItemDecoration(itemDecoration);

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

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                Push_History_Data();
                mRef.child(user.getUid()).removeValue();
                paymentAdapter.notifyDataSetChanged();
                Intent intent1 = new Intent(Payment_Activity.this, End_Process_Activity.class);
                startActivity(intent1);
                finishAndRemoveTask();
            }
        });

        radioGroup = findViewById(R.id.rad_group);
        vietin_rad = findViewById(R.id.vietin_rad);
        vietcom_rad = findViewById(R.id.vietcom_rad);
        mb_rad = findViewById(R.id.mb_rad);
        non_rad = findViewById(R.id.non_rad);
        imv_card_payment = findViewById(R.id.imv_card_payment);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (vietin_rad.isChecked()) {
                    imv_card_payment.setImageResource(R.drawable.img_vietin);
                    imv_card_payment.setVisibility(View.VISIBLE);
                } else if (vietcom_rad.isChecked()) {
                    imv_card_payment.setImageResource(R.drawable.img_vietcom);
                    imv_card_payment.setVisibility(View.VISIBLE);
                } else if (mb_rad.isChecked()) {
                    imv_card_payment.setImageResource(R.drawable.img_mb);
                    imv_card_payment.setVisibility(View.VISIBLE);
                } else {
                    imv_card_payment.setVisibility(View.GONE);
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