package com.example.tryagain;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Detail_Activity extends AppCompatActivity {
    private TextView tv_Name, tv_Description, tv_Plus, tv_Count, tv_Minus, tv_detail_price, tv_check;
    private ImageView imv_Product;
    private RecyclerView rcvCmt;
    private Button btn_add_to_cart;
    private ImageButton btnSendCmt;
    private EditText ed_Cmt;
    private CommentAdapter mCommentAdapter;
    private ArrayList<Comment> mCommentList;
    private ImageButton ibtn_cart;
    private NotificationBadge badge;
    private RadioGroup radio_gr_Size;
    private RadioButton rbtn_Size;
    Product product_intent;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference mRef;
    StorageReference StorageRef;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        radio_gr_Size = findViewById(R.id.radio_gr_size);
        tv_detail_price = findViewById(R.id.tv_detail_price);

        badge = findViewById(R.id.badge);
        ibtn_cart = findViewById(R.id.i_btn_cart);
        ed_Cmt = findViewById(R.id.et_Cmt);
        tv_Name = findViewById(R.id.tv_name_product);
        tv_Description = findViewById(R.id.tv_description);
        imv_Product = findViewById(R.id.imv_Product);
        tv_Plus = findViewById(R.id.tv_plus);
        tv_Count = findViewById(R.id.tv_count);
        tv_Minus = findViewById(R.id.tv_minus);
        btnSendCmt = findViewById(R.id.btn_send);
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart);
        rcvCmt = findViewById(R.id.rcv_comment);
        tv_check = findViewById(R.id.tv_check);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        product_intent = (Product) getIntent().getSerializableExtra("Product");
        StorageRef = FirebaseStorage.getInstance().getReference().child("ProfileImage");

        rcvCmt.setLayoutManager(new LinearLayoutManager(this));

        mCommentList = new ArrayList<>();
        mCommentAdapter = new CommentAdapter(mCommentList);
        rcvCmt.setAdapter(mCommentAdapter);

        tv_detail_price.setText(String.format("%sđ", ConvertPriceToString(product_intent.getPrice())));
        Glide.with(this).load(product_intent.getImage()).into(imv_Product);
        tv_Name.setText(product_intent.getName());
        tv_Description.setText(product_intent.getDescription());

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radio_Id = radio_gr_Size.getCheckedRadioButtonId();
                rbtn_Size = findViewById(radio_Id);
                String size = rbtn_Size.getText().toString();

                double quantity = Double.parseDouble(tv_Count.getText().toString());
                double price = product_intent.getPrice();
                double T_price = quantity * price;
                String id_sp = product_intent.getId();
                String path_child = id_sp + "_size_" + size;
                DatabaseReference Cart_Ref = FirebaseDatabase.getInstance().getReference().child("Cart");

                Map<String, Object> cart = new HashMap<>();
                cart.put("Image", product_intent.getImage());
                cart.put("Name", product_intent.getName());
                cart.put("Price", String.valueOf(Double.parseDouble(String.valueOf(product_intent.getPrice()))));
                cart.put("Total_Price", String.valueOf(T_price));
                cart.put("Id", product_intent.getId());
                cart.put("Quantity", tv_Count.getText().toString());
                cart.put("Size", size);

                Cart_Ref.child(user.getUid()).child(path_child).updateChildren(cart);
            }
        });
        GetQuantityOfCart();
        ibtn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Detail_Activity.this, Cart_Activity.class);
                startActivity(intent);
            }
        });

        btnSendCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Name = snapshot.child("Name").getValue(String.class);
                        String comment = ed_Cmt.getText().toString();
                        String id = user.getUid();
                        String image = snapshot.child("Image").getValue(String.class);
                        String date = Date_Comment();
                        Comment comment1 = new Comment(Name, comment, id, image, date);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef1 = database.getReference("Comments");
                        myRef1.child(product_intent.getId()).child(realtimevalue()).setValue(comment1);
                        ed_Cmt.setText("");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        tv_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String present_value_string = tv_Count.getText().toString();
                int present_value_int = Integer.parseInt(present_value_string);
                present_value_int++;
                tv_Count.setText(String.valueOf(present_value_int));
            }
        });
        tv_Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String present_value_string = tv_Count.getText().toString();
                int present_value_int = Integer.parseInt(present_value_string);
                present_value_int--;
                if (present_value_int < 1) {
                    tv_Count.setText("1");
                    return;
                }
                tv_Count.setText(String.valueOf(present_value_int));

            }
        });
        GetCommentFromRealtimeDatabase();
    }

    private void GetQuantityOfCart() {

        DatabaseReference Cart_Ref = FirebaseDatabase.getInstance().getReference().child("Cart");
        Cart_Ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int size = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    size++;
                }
                badge.setNumber(size);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetCommentFromRealtimeDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Comments");

        myRef.child(product_intent.getId()).addValueEventListener(new ValueEventListener() {

            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mCommentList != null) {
                    mCommentList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Comment comments = dataSnapshot.getValue(Comment.class);
                    mCommentList.add(comments);
                }
                mCommentAdapter.notifyDataSetChanged();
                if (mCommentAdapter.getItemCount() == 0) {
                    rcvCmt.setVisibility(View.GONE);
                    tv_check.setText("Đánh giá từ người dùng: (Trống)");
                } else {
                    rcvCmt.setVisibility(View.VISIBLE);
                    tv_check.setText("Đánh giá từ người dùng:");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }



    public String realtimevalue() {
        Date dateNtime = Calendar.getInstance().getTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String time = timeFormat.format(dateNtime);
        String date = dateFormat.format(dateNtime);
        return date + time;
    }

    public String Date_Comment() {
        Date dateNtime = Calendar.getInstance().getTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String time = timeFormat.format(dateNtime);
        String date = dateFormat.format(dateNtime);
        return time + ", " + date;
    }

    public String ConvertPriceToString(double a) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(a);
    }
}