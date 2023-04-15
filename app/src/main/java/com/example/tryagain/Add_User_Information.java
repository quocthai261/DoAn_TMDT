package com.example.tryagain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Add_User_Information extends AppCompatActivity {

    private CircleImageView civ_Image;
    private EditText et_Name, et_Phone, et_Address, et_Sex;
    private TextView tv_Email, tv_save;
    private int RESULT_CODE = 26;
    private Uri img_uri;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference mRef;
    StorageReference StorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_information);

        et_Name = findViewById(R.id.et_name);
        et_Phone = findViewById(R.id.et_phone);
        tv_Email = findViewById(R.id.tv_email);
        et_Address = findViewById(R.id.et_address);
        et_Sex = findViewById(R.id.et_sex);
        tv_save = findViewById(R.id.tv_save);
        civ_Image = findViewById(R.id.civ_image);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        StorageRef = FirebaseStorage.getInstance().getReference().child("ProfileImage");
        tv_Email.setText(user.getEmail());

        civ_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_CODE);
            }
        });
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (img_uri != null) {
                    SaveData_Info();
                    ProgressDialog.show(Add_User_Information.this, "", "Đang khởi tạo tài khoản...", true);

                } else {
                    Toast.makeText(Add_User_Information.this, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE && resultCode == RESULT_OK && data != null) {
            img_uri = data.getData();
            civ_Image.setImageURI(img_uri);
        }
    }

    private void SaveData_Info() {
        String userName = et_Name.getText().toString().trim();
        String userPhone = et_Phone.getText().toString().trim();
        String userAddress = et_Address.getText().toString().trim();
        String userSex = et_Sex.getText().toString().trim();

        FirebaseAuth auth1 = FirebaseAuth.getInstance();
        FirebaseUser user1 = auth1.getCurrentUser();

        DatabaseReference mRef1 = FirebaseDatabase.getInstance().getReference().child("Users");
        StorageReference StorageRef1 = FirebaseStorage.getInstance().getReference().child("ProfileImage");

        StorageRef1.child(user1.getUid()).putFile(img_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    StorageRef1.child(user1.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("Name", userName);
                            hashMap.put("Phone", userPhone);
                            hashMap.put("Image", uri.toString());
                            hashMap.put("Address", userAddress);
                            hashMap.put("Sex", userSex);
                            hashMap.put("Email", user1.getEmail());

                            mRef1.child(user1.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Intent intent = new Intent(Add_User_Information.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

}