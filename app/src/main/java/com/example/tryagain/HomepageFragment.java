package com.example.tryagain;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomepageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomepageFragment extends Fragment {
    private ArrayList<Best_Seller_Product> best_seller_products;
    private Best_Seller_Adapter best_seller_adapter;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    private NotificationBadge badge;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public HomepageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomepageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomepageFragment newInstance(String param1, String param2) {
        HomepageFragment fragment = new HomepageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv_watch_more = view.findViewById(R.id.tv_watch_more);
        badge = view.findViewById(R.id.badge);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        EditText search = view.findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchProduct.class);
                startActivity(intent);
            }
        });

        ViewFlipper viewFlipper = view.findViewById(R.id.viewflipper);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        viewFlipper.startFlipping();

        RecyclerView rcv_best_seller = view.findViewById(R.id.rcv_popular_product);
        rcv_best_seller.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        best_seller_products = new ArrayList<>();
        best_seller_adapter = new Best_Seller_Adapter(best_seller_products, new Best_Seller_Adapter.ItemClickListener() {
            @Override
            public void onItemClick(Best_Seller_Product best_seller_product) {
                Intent intent = new Intent(getContext(), SearchProduct.class);
                intent.putExtra("Best_Seller_Product", best_seller_product);
                startActivity(intent);
            }
        });
        rcv_best_seller.setAdapter(best_seller_adapter);


        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Popular Product").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list) {
                    Best_Seller_Product best_seller_product = d.toObject(Best_Seller_Product.class);
                    best_seller_products.add(best_seller_product);
                }
                best_seller_adapter.notifyDataSetChanged();
            }
        });

        rcv_best_seller.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        tv_watch_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchProduct.class);
                startActivity(intent);
            }
        });
        ImageButton ibtn_cart = view.findViewById(R.id.i_btn_cart);
        ibtn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Cart_Activity.class);
                startActivity(intent);
            }
        });
        GetQuantityOfCart();
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
}