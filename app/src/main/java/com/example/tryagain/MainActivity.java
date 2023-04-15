package com.example.tryagain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.tryagain.databinding.ActivityMainBinding;
public class MainActivity extends AppCompatActivity{

    ActivityMainBinding binding;
    SharedPreferences sharedPreferences;

    boolean isNightMode;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*replaceFragment(new HomepageFragment());*/
        if(savedInstanceState == null){
            replaceFragment(new HomepageFragment());
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.homepage:
                    replaceFragment(new HomepageFragment());
                    break;
                case R.id.user:
                    replaceFragment(new UserFragment());
                    break;
                case R.id.catalog_menu:
                    replaceFragment(new CatalogFragment());
                    break;
                case R.id.setting:
                    replaceFragment(new SettingFragment());
                    break;
            }
            return true;
        });
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        isNightMode = sharedPreferences.getBoolean("night_mode", false);
        if (isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }
}