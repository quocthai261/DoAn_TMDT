package com.example.tryagain;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp_Activity extends AppCompatActivity {
    private EditText etEmail_Signup, etPass_Signup, etPass_CF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etEmail_Signup = findViewById(R.id.et_email_signup);
        etPass_Signup = findViewById(R.id.et_pass_signup);
        etPass_CF = findViewById(R.id.et_cf_pass_signup);
        Button btnSignUp = findViewById(R.id.btn_signup);
        TextView tvLogin = findViewById(R.id.tv_login);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp_Activity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignUp();
            }
        });
    }

    private void onClickSignUp() {
        String email = etEmail_Signup.getText().toString().trim();
        String password = etPass_Signup.getText().toString().trim();
        String password_cf = etPass_CF.getText().toString().trim();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (!password_cf.equals(password)) {
            Toast.makeText(SignUp_Activity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SignUp_Activity.this, Add_User_Information.class);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                Toast.makeText(SignUp_Activity.this, "Đăng ký thất bại",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}