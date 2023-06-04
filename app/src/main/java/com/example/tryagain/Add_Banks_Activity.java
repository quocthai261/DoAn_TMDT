package com.example.tryagain;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Add_Banks_Activity extends AppCompatActivity {

    private TextView tv_bank_name;
    private EditText et_bank_number, et_bank_user_name, et_bank_date;
    private Button btn_add_bank;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_banks);

        tv_bank_name = findViewById(R.id.tv_bank_name);
        et_bank_number = findViewById(R.id.et_bank_number);
        et_bank_user_name = findViewById(R.id.et_bank_user_name);
        et_bank_date = findViewById(R.id.et_bank_date);
        btn_add_bank = findViewById(R.id.btn_add_bank);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        Bank bank = (Bank) getIntent().getSerializableExtra("Banks");
        String activity = getIntent().getStringExtra("bank");
        DatabaseReference User_ref = FirebaseDatabase.getInstance().getReference().child("Users");
        tv_bank_name.setText(bank.getShort_Name());


        btn_add_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Number = et_bank_number.getText().toString();
                String Short_Name = bank.getShort_Name();
                String Full_Name = bank.getFull_Name();
                String User_Name = et_bank_user_name.getText().toString();
                String Date = et_bank_date.getText().toString();
                String Logo = bank.getLogo();
                HashMap<String, String> user_bank = new HashMap<>();
                user_bank.put("Number", Number);
                user_bank.put("Short_Name", Short_Name);
                user_bank.put("Full_Name", Full_Name);
                user_bank.put("User_Name", User_Name);
                user_bank.put("Date", Date);
                user_bank.put("Logo", Logo);
                User_ref.child(user.getUid()).child("Bank").child(bank.getShort_Name())
                        .setValue(user_bank).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                if (activity != null && activity.equals("Bank_User")) {
                                    Intent intent = new Intent(Add_Banks_Activity.this, Bank_User.class);
                                    startActivity(intent);
                                } else if (activity != null && activity.equals("Payment_Activity")) {
                                    Intent intent = new Intent(Add_Banks_Activity.this, Payment_Activity.class);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });
    }
}