package com.app.http_animals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.http_animals.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.catAnim.setOnClickListener(view12 -> startActivity(new Intent(MainActivity.this,GenerateActivity.class).putExtra("name","cat")));
        binding.dogAnim.setOnClickListener(view1 -> startActivity(new Intent(MainActivity.this,GenerateActivity.class).putExtra("name","dog")));

    }
}