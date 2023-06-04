package com.example.tryagain;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Bank_Activity extends AppCompatActivity {

    private ArrayList<Bank> bankArrayList;
    private Bank_Adapter bank_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        String activity = getIntent().getStringExtra("Add_Bank");

        RecyclerView rcv_bank = findViewById(R.id.rcv_bank);
        rcv_bank.setLayoutManager(new GridLayoutManager(this, 3));
        bankArrayList = new ArrayList<>();
        bank_adapter = new Bank_Adapter(bankArrayList, new Bank_Adapter.ItemClickListener() {
            @Override
            public void onItemClick(Bank bank) {
                Intent intent = new Intent(Bank_Activity.this, Add_Banks_Activity.class);
                if (activity != null && activity.equals("Bank_User")) {
                    intent.putExtra("Banks", bank);
                    intent.putExtra("bank", "Bank_User");
                    startActivity(intent);
                } else if (activity != null && activity.equals("Payment_Activity")) {
                    intent.putExtra("Banks", bank);
                    intent.putExtra("bank", "Payment_Activity");
                    startActivity(intent);
                }
            }
        });
        rcv_bank.setAdapter(bank_adapter);

        DatabaseReference Bank_Ref = FirebaseDatabase.getInstance().getReference().child("Banks");
        Bank_Ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Bank bank = dataSnapshot.getValue(Bank.class);
                    bankArrayList.add(bank);
                }
                bank_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}