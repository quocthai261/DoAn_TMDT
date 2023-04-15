package com.example.tryagain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_Information extends AppCompatActivity {
    private CircleImageView civ_image_user;
    private TextView tv_user_name, tv_user_phone, tv_user_email, tv_user_address, tv_user_sex;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference mRef;
    StorageReference StorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        StorageRef = FirebaseStorage.getInstance().getReference().child("ProfileImage");

        civ_image_user = findViewById(R.id.civ_image_user);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_phone = findViewById(R.id.tv_user_phone);
        tv_user_email = findViewById(R.id.tv_user_email);
        tv_user_address = findViewById(R.id.tv_user_address);
        tv_user_sex = findViewById(R.id.tv_user_sex);

        mRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String UrlImageProfile = snapshot.child("Image").getValue(String.class);
                String username = snapshot.child("Name").getValue(String.class);
                String userphone = snapshot.child("Phone").getValue(String.class);
                String address = snapshot.child("Address").getValue(String.class);
                Glide.with(getApplicationContext()).load(UrlImageProfile).into(civ_image_user);
                tv_user_name.setText(username);
                tv_user_phone.setText(userphone);
                tv_user_address.setText(address);
                tv_user_email.setText(user.getEmail());

                if (snapshot.child("Sex").getValue(String.class) == null) {
                    tv_user_sex.setHint("(Trống)");
                } else {
                    tv_user_sex.setText(snapshot.child("Sex").getValue(String.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}