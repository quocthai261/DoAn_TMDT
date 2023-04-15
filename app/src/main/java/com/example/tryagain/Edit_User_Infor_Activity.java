package com.example.tryagain;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_User_Infor_Activity extends AppCompatActivity {

    private CircleImageView civ_edit_Image;
    private EditText et_edit_Name, et_edit_Phone, et_edit_Address, et_edit_Sex;
    private TextView tv_edit_Email;
    private final int RESULT_CODE = 26;
    private Uri img_uri;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference mRef, mRef1;
    StorageReference StorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_infor);

        et_edit_Name = findViewById(R.id.et_edit_name);
        et_edit_Phone = findViewById(R.id.et_edit_phone);
        tv_edit_Email = findViewById(R.id.tv_edit_email);
        et_edit_Address = findViewById(R.id.et_edit_address);
        et_edit_Sex = findViewById(R.id.et_edit_sex);
        TextView tv_save_Infor = findViewById(R.id.tv_save_infor);
        civ_edit_Image = findViewById(R.id.civ_edit_image);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mRef1 = FirebaseDatabase.getInstance().getReference().child("Comments");
        StorageRef = FirebaseStorage.getInstance().getReference().child("ProfileImage");


        tv_edit_Email.setText(user.getEmail());
        civ_edit_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_CODE);
            }
        });

        mRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Name = snapshot.child("Name").getValue(String.class);
                String Image = snapshot.child("Image").getValue(String.class);
                String Phone = snapshot.child("Phone").getValue(String.class);
                String Address = snapshot.child("Address").getValue(String.class);
                String Sex = snapshot.child("Sex").getValue(String.class);
                et_edit_Name.setText(Name);
                et_edit_Phone.setText(Phone);
                et_edit_Address.setText(Address);
                et_edit_Sex.setText(Sex);
                Glide.with(getApplicationContext()).load(Image).into(civ_edit_Image);
                tv_edit_Email.setText(user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        tv_save_Infor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (img_uri == null) {
                    Save_Info_Without_Image();
                    ProgressDialog.show(Edit_User_Infor_Activity.this, "", "Đang cập nhật thông tin...", true);
                } else {
                    Save_Info();
                    ProgressDialog.show(Edit_User_Infor_Activity.this, "", "Đang cập nhật thông tin...", true);
                }
            }
        });

    }

    private void Save_Info_Without_Image() {
        String userName = et_edit_Name.getText().toString().trim();
        String userPhone = et_edit_Phone.getText().toString().trim();
        String userAddress = et_edit_Address.getText().toString().trim();
        String userSex = et_edit_Sex.getText().toString().trim();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Name", userName);
        hashMap.put("Phone", userPhone);
        hashMap.put("Address", userAddress);
        hashMap.put("Sex", userSex);
        hashMap.put("Email", user.getEmail());

        mRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                        if (user.getUid().equals(snapshot2.child("id").getValue(String.class))) {
                            HashMap<String, Object> Update_cmt = new HashMap<>();
                            Update_cmt.put("name", userName);
                            mRef1.child(Objects.requireNonNull(snapshot1.getKey()))
                                    .child(Objects.requireNonNull(snapshot2.getKey()))
                                    .updateChildren(Update_cmt);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mRef.child(user.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Intent intent = new Intent(Edit_User_Infor_Activity.this, User_Information.class);
                startActivity(intent);
                finishAndRemoveTask();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE && resultCode == RESULT_OK && data != null) {
            img_uri = data.getData();
            civ_edit_Image.setImageURI(img_uri);
        }
    }

    private void Save_Info() {
        String userName = et_edit_Name.getText().toString().trim();
        String userPhone = et_edit_Phone.getText().toString().trim();
        String userAddress = et_edit_Address.getText().toString().trim();
        String userSex = et_edit_Sex.getText().toString().trim();

        StorageRef.child(user.getUid()).putFile(img_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    StorageRef.child(user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("Name", userName);
                            hashMap.put("Phone", userPhone);
                            hashMap.put("Image", uri.toString());
                            hashMap.put("Address", userAddress);
                            hashMap.put("Sex", userSex);
                            hashMap.put("Email", user.getEmail());

                            mRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                            if (user.getUid().equals(snapshot2.child("id").getValue(String.class))) {
                                                HashMap<String, Object> Update_cmt = new HashMap<>();
                                                Update_cmt.put("name", userName);
                                                Update_cmt.put("profileImage", uri.toString());
                                                mRef1.child(Objects.requireNonNull(snapshot1.getKey()))
                                                        .child(Objects.requireNonNull(snapshot2.getKey()))
                                                        .updateChildren(Update_cmt);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            mRef.child(user.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Intent intent = new Intent(Edit_User_Infor_Activity.this, User_Information.class);
                                    startActivity(intent);
                                    finishAndRemoveTask();
                                }
                            });

                        }
                    });
                }
            }
        });
    }


}