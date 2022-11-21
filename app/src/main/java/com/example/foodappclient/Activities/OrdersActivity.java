package com.example.foodappclient.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodappclient.Model.CartModel;
import com.example.foodappclient.Model.OrderModel;
import com.example.foodappclient.OrderAdapter;
import com.example.foodappclient.databinding.ActivityOrdersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class OrdersActivity extends AppCompatActivity {


    ActivityOrdersBinding binding;

    DatabaseReference reference;

    ArrayList<OrderModel> list = new ArrayList<>();
    OrderAdapter adapter;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference().child("Orders");
        user = FirebaseAuth.getInstance().getCurrentUser();


        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);


        new Handler().postDelayed(this::getOngoingOrders,600);


    }
    private void getOngoingOrders() {
        binding.recyclerView.setHasFixedSize(true);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    list.clear();
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){

                                for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()){
                                    CartModel model = dataSnapshot3.getValue(CartModel.class);
                                    OrderModel orderModel = dataSnapshot1.getValue(OrderModel.class);
                                    assert model != null;

                                    if (dataSnapshot3.child("publisher").getValue().toString().equals(user.getUid())){
                                        list.add(orderModel);
                                    }

//                                    if (model.getPublisher().equals(user.getUid())){
//                                        list.add(orderModel);
//                                    }

                                }
                            }

                        }
                    }

                    orderByDate();
                    adapter = new OrderAdapter(list,"visible");
                    binding.recyclerView.setAdapter(adapter);

                    if (list.size() > 0){
                        binding.recyclerView.setVisibility(View.VISIBLE);

                    }else {
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.GONE);

                        Toast.makeText(OrdersActivity.this, "No data", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(OrdersActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void orderByDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();

        Collections.sort(list, (o1, o2) -> {
            calendar.setTimeInMillis( o1.getTimestamp());
            calendar1.setTimeInMillis(o2.getTimestamp());


            String d1 = dateFormat.format(calendar.getTime());
            String d2 = dateFormat.format(calendar1.getTime());


            return d2.compareTo(d1);

        });
    }
}