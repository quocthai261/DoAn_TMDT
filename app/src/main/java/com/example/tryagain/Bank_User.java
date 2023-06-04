package com.example.tryagain;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Bank_User extends AppCompatActivity {
    private ArrayList<Bank_Payment> paymentArrayList;
    private Bank_User_Adapter bank_user_adapter;
    private TextView tv_user_bank_count;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference User_Ref;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_user);
        tv_user_bank_count = findViewById(R.id.tv_user_bank_count);
        TextView tv_add_user_bank = findViewById(R.id.tv_add_user_bank);
        RecyclerView rcv_user_bank = findViewById(R.id.rcv_user_bank);

        rcv_user_bank.setLayoutManager(new LinearLayoutManager(this));
        paymentArrayList = new ArrayList<>();
        bank_user_adapter = new Bank_User_Adapter(paymentArrayList, new Bank_User_Adapter.ItemClickListener() {
            @Override
            public void onItemClick(Bank_Payment bank_user) {
                Intent intent = new Intent(Bank_User.this, User_Bank_Information.class);
                intent.putExtra("Bank_User", bank_user);
                startActivity(intent);
            }
        });
        rcv_user_bank.setAdapter(bank_user_adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcv_user_bank.addItemDecoration(itemDecoration);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        User_Ref = FirebaseDatabase.getInstance().getReference().child("Users");

        User_Ref.child(user.getUid()).child("Bank").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Bank_Payment bank_payment = dataSnapshot.getValue(Bank_Payment.class);
                    paymentArrayList.add(bank_payment);
                }
                tv_user_bank_count.setText("Tài khoản ngân hàng (" + paymentArrayList.size() + ")");
                bank_user_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        tv_add_user_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bank_User.this, Bank_Activity.class);
                intent.putExtra("Add_Bank", "Bank_User");
                startActivity(intent);
            }
        });
    }
}