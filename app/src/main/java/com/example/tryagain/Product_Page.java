package com.example.tryagain;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Product_Page extends AppCompatActivity {
    private final String[] item_sort_price = {"Mặc định", "Tăng dần", "Giảm dần"};
    private RecyclerView rcv_Product;
    private ArrayList<Product> productArrayList;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayout;
    private ImageButton im_btn_grid_view;
    private int CurrentTypeDisplay = Product.TYPE_GRID;
    private int CurrentPosition;
    private ProductAdapter productAdapter;
    FirebaseFirestore firestore;
    SearchView search_box;
    private FloatingActionButton float_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        float_btn = findViewById(R.id.float_btn);
        im_btn_grid_view = findViewById(R.id.im_btn_grid_view);

        AutoCompleteTextView ac_tv_sort_price = findViewById(R.id.ac_tv_sort_price);
        ArrayAdapter<String> item_sort_price_adapter = new ArrayAdapter<>(this, R.layout.list_sort_price, item_sort_price);
        ac_tv_sort_price.setAdapter(item_sort_price_adapter);

        gridLayoutManager = new GridLayoutManager(this, 2);
        linearLayout = new LinearLayoutManager(this);

        rcv_Product = findViewById(R.id.rcv_product);
        rcv_Product.setLayoutManager(gridLayoutManager);
        productArrayList = new ArrayList<>();
        setTypeDisplayRCV(Product.TYPE_GRID);
        productAdapter = new ProductAdapter(productArrayList, new ProductAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(Product_Page.this, Detail_Activity.class);
                intent.putExtra("Product", product);
                startActivity(intent);
            }
        });
        rcv_Product.setAdapter(productAdapter);
        rcv_Product.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0){
                    float_btn.hide();
                } else {
                    float_btn.show();
                }
            }
        });
        float_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rcv_Product.scrollToPosition(0);
            }
        });

        search_box = findViewById(R.id.searching_box);
        search_box.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }

        });

        Category category = (Category) getIntent().getSerializableExtra("Category");
        String collection = category.getCategory();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection(collection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list) {
                    Product product = d.toObject(Product.class);
                    productArrayList.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }
        });

        ac_tv_sort_price.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item_sort = parent.getItemAtPosition(position).toString();
                if (item_sort.equals("Tăng dần")) {
                    firestore.collection(collection).orderBy("Price", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            if (productArrayList != null) {
                                productArrayList.clear();
                            }
                            for (DocumentSnapshot d : list) {
                                Product product = d.toObject(Product.class);
                                productArrayList.add(product);
                            }
                            productAdapter.notifyDataSetChanged();
                            setTypeDisplayRCV(CurrentTypeDisplay);
                            rcv_Product.scrollToPosition(0);
                        }
                    });
                }
                if (item_sort.equals("Giảm dần")) {
                    firestore.collection(collection).orderBy("Price", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            if (productArrayList != null) {
                                productArrayList.clear();
                            }
                            for (DocumentSnapshot d : list) {
                                Product product = d.toObject(Product.class);
                                productArrayList.add(product);
                            }
                            productAdapter.notifyDataSetChanged();
                            setTypeDisplayRCV(CurrentTypeDisplay);
                            rcv_Product.scrollToPosition(0);
                        }
                    });
                }
                if (item_sort.equals("Mặc định")) {
                    firestore.collection(collection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            if (productArrayList != null) {
                                productArrayList.clear();
                            }
                            for (DocumentSnapshot d : list) {
                                Product product = d.toObject(Product.class);
                                productArrayList.add(product);
                            }
                            productAdapter.notifyDataSetChanged();
                            setTypeDisplayRCV(CurrentTypeDisplay);
                            rcv_Product.scrollToPosition(0);
                        }
                    });
                }
            }
        });

        search_box.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        im_btn_grid_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetCurrentPosition();
                onClickChangeTypeDisPlay();
            }
        });
    }

    private void SetCurrentPosition() {
        RecyclerView.LayoutManager layoutManager = rcv_Product.getLayoutManager();
        switch (CurrentTypeDisplay) {
            case Product.TYPE_GRID:
                CurrentPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                break;
            case Product.TYPE_LIST:
                CurrentPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                break;
        }
    }

    private void setTypeDisplayRCV(int typeDisplay) {
        if (productArrayList == null || productArrayList.isEmpty()) {
            return;
        }
        CurrentTypeDisplay = typeDisplay;
        for (Product product : productArrayList) {
            product.setTypeDisplay(typeDisplay);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onClickChangeTypeDisPlay() {
        if (CurrentTypeDisplay == Product.TYPE_GRID) {
            setTypeDisplayRCV(Product.TYPE_LIST);
            rcv_Product.setLayoutManager(linearLayout);
        } else if (CurrentTypeDisplay == Product.TYPE_LIST) {
            setTypeDisplayRCV(Product.TYPE_GRID);
            rcv_Product.setLayoutManager(gridLayoutManager);
        }
        productAdapter.notifyDataSetChanged();
        ChangeIconButton();
        rcv_Product.scrollToPosition(CurrentPosition);
    }

    private void ChangeIconButton() {
        switch (CurrentTypeDisplay) {
            case Product.TYPE_GRID:
                im_btn_grid_view.setImageResource(R.drawable.grid);
                break;
            case Product.TYPE_LIST:
                im_btn_grid_view.setImageResource(R.drawable.list);
                break;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterList(String text) {
        ArrayList<Product> filteredList = new ArrayList<>();
        for (Product product : productArrayList) {
            if (product.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(product);
                if (CurrentTypeDisplay == Product.TYPE_GRID) {
                    rcv_Product.setLayoutManager(gridLayoutManager);
                } else if (CurrentTypeDisplay == Product.TYPE_LIST) {
                    rcv_Product.setLayoutManager(linearLayout);
                }
                productAdapter = new ProductAdapter(filteredList, new ProductAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(Product product) {
                        Intent intent = new Intent(Product_Page.this, Detail_Activity.class);
                        intent.putExtra("Product", product);
                        startActivity(intent);
                    }
                });
                rcv_Product.setAdapter(productAdapter);
            }
        }
        productAdapter.notifyDataSetChanged();
    }
}