package com.example.foodappclient;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.foodappclient.databinding.ActivityUpdateBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {


    ActivityUpdateBinding binding;

    DatabaseReference reference;

    ArrayList<Products> list = new ArrayList<>();
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference().child("Products");
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setHasFixedSize(true);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getProducts();
            }
        },300);





    }
    private void getProducts(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    list.clear();
                    binding.progressBar.setVisibility(View.GONE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Products products = dataSnapshot.getValue(Products.class);
                        list.add(products);

                    }

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(UpdateActivity.this,3);
                    binding.recyclerView.setLayoutManager(gridLayoutManager);

                    adapter = new ItemAdapter(UpdateActivity.this,list);
                    binding.recyclerView.setAdapter(adapter);
                }else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(UpdateActivity.this, "No products now!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(UpdateActivity.this, "Error: "+
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}