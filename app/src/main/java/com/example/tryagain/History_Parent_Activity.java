package com.example.tryagain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class History_Parent_Activity extends AppCompatActivity {

    private History_Child_Adapter history_child_adapter;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_parent);

        RecyclerView rcv_history_parent = findViewById(R.id.rcv_history_parent);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rcv_history_parent.setLayoutManager(layoutManager);
        history_child_adapter = new History_Child_Adapter(Get_Data_History_Parent());
        rcv_history_parent.setAdapter(history_child_adapter);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Cart");

    }

    private List<History_Child> Get_Data_History_Parent() {

        List<History_Child> itemList = new ArrayList<>();
        FirebaseAuth auth2 = FirebaseAuth.getInstance();
        FirebaseUser user2 = auth2.getCurrentUser();
        DatabaseReference mRef2 = FirebaseDatabase.getInstance().getReference().child("History");
        mRef2.child(user2.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    History_Child history_child = new History_Child(dataSnapshot.child("History Date").getValue(String.class),
                            dataSnapshot.child("Total Quantity").getValue(String.class),
                            dataSnapshot.child("Total Price").getValue(String.class), Get_Data_History_Item(dataSnapshot.getKey()));
                    itemList.add(history_child);
                }
                history_child_adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return itemList;
    }

    private List<History_Item> Get_Data_History_Item(String child) {
        FirebaseAuth auth1 = FirebaseAuth.getInstance();
        FirebaseUser user1 = auth1.getCurrentUser();
        List<History_Item> item = new ArrayList<>();
        DatabaseReference mRef1 = FirebaseDatabase.getInstance().getReference().child("History");
        mRef1.child(user1.getUid()).child(child).child("History Product").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    History_Item history_item = dataSnapshot.getValue(History_Item.class);
                    item.add(history_item);
                }
                history_child_adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return item;
    }

}