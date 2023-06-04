package com.example.tryagain;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class User_Bank_Information extends AppCompatActivity {

    private TextView tv_name_of_bank, tv_number_of_bank, tv_user_name_of_bank;
    private Button btn_delete_bank;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference User_Ref;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bank_information);
        tv_name_of_bank = findViewById(R.id.tv_name_of_bank);
        tv_number_of_bank = findViewById(R.id.tv_number_of_bank);
        tv_user_name_of_bank = findViewById(R.id.tv_user_name_of_bank);
        btn_delete_bank = findViewById(R.id.btn_delete_bank);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        User_Ref = FirebaseDatabase.getInstance().getReference().child("Users");

        Bank_Payment bank_user = (Bank_Payment) getIntent().getSerializableExtra("Bank_User");
        tv_name_of_bank.setText(String.format("%s - %s", bank_user.getShort_Name(), bank_user.getFull_Name()));
        tv_number_of_bank.setText(String.format("******%s", Bank_Payment_Adapter.FormatBankNumber(bank_user.getNumber())));
        tv_user_name_of_bank.setText(bank_user.getUser_Name());
        btn_delete_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setMessage("Bạn chắc chắn muốn muốn xóa Tài khoản ngân hàng này?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                User_Ref.child(user.getUid()).child("Bank").child(bank_user.getShort_Name()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Intent intent = new Intent(User_Bank_Information.this, Bank_User.class);
                                        startActivity(intent);
                                    }
                                });

                            }
                        })
                        .setNegativeButton("Thoát", null).show();

            }
        });
    }
}