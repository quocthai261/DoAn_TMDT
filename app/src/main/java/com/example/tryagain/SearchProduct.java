package com.example.tryagain;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchProduct extends AppCompatActivity {
    private final String[] item_sort = {"Tăng dần", "Giảm dần"};
    private ArrayList<Product> productArrayList, filteredList;
    ProductAdapter productAdapter;
    FirebaseFirestore firestore;
    private RecyclerView rcv_Product;
    SearchView search_box;
    private FloatingActionButton float_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        search_box = findViewById(R.id.searching_box);
        float_btn = findViewById(R.id.float_btn_up);

        AutoCompleteTextView ac_tv_sort = findViewById(R.id.ac_tv_sort_price_search);
        ArrayAdapter<String> item_sort_adapter = new ArrayAdapter<>(this, R.layout.list_sort_price, item_sort);
        ac_tv_sort.setAdapter(item_sort_adapter);

        search_box.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                ac_tv_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item_sort = parent.getItemAtPosition(position).toString();
                        if (item_sort.equals("Tăng dần")) {
                            Collections.sort(filteredList, new ProductIncreasing());
                            for (Product product : filteredList) {
                                rcv_Product.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
                                productAdapter = new ProductAdapter(filteredList, new ProductAdapter.ItemClickListener() {
                                    @Override
                                    public void onItemClick(Product product) {
                                        Intent intent = new Intent(SearchProduct.this, Detail_Activity.class);
                                        intent.putExtra("Product", product);
                                        startActivity(intent);
                                    }
                                });
                                rcv_Product.setAdapter(productAdapter);
                            }
                            productAdapter.notifyDataSetChanged();
                        }
                        if (item_sort.equals("Giảm dần")) {
                            Collections.sort(filteredList, new ProductDecreasing());
                            for (Product product : filteredList) {
                                rcv_Product.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
                                productAdapter = new ProductAdapter(filteredList, new ProductAdapter.ItemClickListener() {
                                    @Override
                                    public void onItemClick(Product product) {
                                        Intent intent = new Intent(SearchProduct.this, Detail_Activity.class);
                                        intent.putExtra("Product", product);
                                        startActivity(intent);
                                    }
                                });
                                rcv_Product.setAdapter(productAdapter);
                            }
                            productAdapter.notifyDataSetChanged();
                        }
                    }
                });
                return true;
            }
        });

        firestore = FirebaseFirestore.getInstance();
        productArrayList = new ArrayList<>();
        rcv_Product = findViewById(R.id.rcv_product);
        rcv_Product.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(productArrayList, new ProductAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(SearchProduct.this, Detail_Activity.class);
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

        firestore.collection("Shirts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

        firestore.collection("Pants").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
        firestore.collection("Hats").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
        firestore.collection("Shoes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

        ac_tv_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item_sort = parent.getItemAtPosition(position).toString();
                if (item_sort.equals("Tăng dần")) {
                    productAdapter.notifyDataSetChanged();
                    Collections.sort(productArrayList, new ProductIncreasing());
                    rcv_Product.scrollToPosition(0);
                }
                if (item_sort.equals("Giảm dần")) {
                    productAdapter.notifyDataSetChanged();
                    Collections.sort(productArrayList, new ProductDecreasing());
                    rcv_Product.scrollToPosition(0);
                }

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterList(String text) {
        filteredList = new ArrayList<>();
        for (Product product : productArrayList) {
            if (product.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(product);
                rcv_Product.setLayoutManager(new GridLayoutManager(this, 2));
                productAdapter = new ProductAdapter(filteredList, new ProductAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(Product product) {
                        Intent intent = new Intent(SearchProduct.this, Detail_Activity.class);
                        intent.putExtra("Product", product);
                        startActivity(intent);
                    }
                });
                rcv_Product.setAdapter(productAdapter);
            }
        }
        productAdapter.notifyDataSetChanged();
    }

    public static class ProductIncreasing implements Comparator<Product> {
        @Override
        public int compare(Product p1, Product p2) {
            return Integer.compare(p1.getPrice(), p2.getPrice());
        }
    }
    public static class ProductDecreasing implements Comparator<Product> {
        @Override
        public int compare(Product p1, Product p2) {
            return Integer.compare(p2.getPrice(), p1.getPrice());
        }
    }
}