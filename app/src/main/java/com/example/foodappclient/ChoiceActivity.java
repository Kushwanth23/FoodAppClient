//Kushwanth23
package com.example.foodappclient;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodappclient.Activities.OptionsActivity;
import com.example.foodappclient.Activities.OrdersActivity;
import com.example.foodappclient.databinding.ActivityChoiceBinding;

public class ChoiceActivity extends AppCompatActivity {


    ActivityChoiceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        binding.btnVegetables.setOnClickListener(v -> {
//            Intent intent = new Intent(ChoiceActivity.this,VegetableActivity.class);
//            intent.putExtra("category","Vegetables");
//            startActivity(intent);
//
//        });
//
//        binding.btnFruits.setOnClickListener(v -> {
//            Intent intent = new Intent(ChoiceActivity.this,VegetableActivity.class);
//            intent.putExtra("category","Fruits");
//            startActivity(intent);
//
//        });

        binding.btnGrains.setOnClickListener(v -> {
            Intent intent = new Intent(ChoiceActivity.this,VegetableActivity.class);
            intent.putExtra("category","Food");
            startActivity(intent);

        });
        binding.btnUpdateStock.setOnClickListener(v -> {
            Intent intent = new Intent(ChoiceActivity.this,UpdateActivity.class);
            startActivity(intent);

        });


        binding.btnOrders.setOnClickListener(v -> {
            Intent intent = new Intent(ChoiceActivity.this, OrdersActivity.class);
            startActivity(intent);

        });

        binding.imgProfile.setOnClickListener(view -> startActivity(new Intent(ChoiceActivity.this, OptionsActivity.class)));

    }
}