package com.example.foodappclient;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodappclient.Activities.DetailsActivity;
import com.example.foodappclient.Model.OrderModel;
import com.example.foodappclient.databinding.ItemOrdersBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;



public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private ArrayList<OrderModel> list;
    String from;

    ItemOrdersBinding binding;
    public OrderAdapter(ArrayList<OrderModel> list, String from) {
        this.list = list;
        this.from = from;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemOrdersBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrderModel model = list.get(position);
        assert model !=null;

        binding.orderId.setText("order id: "+model.getOrderId());
        binding.totalOrderAmount.setText("Total Amount: ₹ "+model.getTotalAmount());

        //status
        String status = model.getStatus();
        binding.orderStatus.setText(status);



        if (from.equals("visible")){
            binding.buttonsLyt.setVisibility(View.VISIBLE);
        }else {
            binding.buttonsLyt.setVisibility(View.GONE);
        }



        //date

        long timestamp = model.getTimestamp();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String date = dateFormat.format(timestamp);
        binding.orderDate.setText("Date: "+date);


        binding.btnOrderDetails.setOnClickListener(v -> {
            v.getContext().startActivity(new Intent(v.getContext(), DetailsActivity.class)
                            .putExtra("orderId", model.getOrderId())
                            .putExtra("userId", model.getUserId()));
        });



        
//        binding.btnReject.setOnClickListener(v -> {
//
//            String condition = binding.btnReject.getText().toString();
//            if (condition.equals("Cancel")){
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                assert user != null;
//                SimpleDateFormat newDte = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
//                String currentDate = newDte.format(new Date());
//
//
//                HashMap<String,Object> map = new HashMap<>();
//                map.put("status","Cancelled");
//                map.put("cancelled_by",user.getUid());
//                map.put("finishedDate",currentDate);
//
//                FirebaseDatabase.getInstance().getReference().child("Orders")
//                        .child(model.getUserId())
//                        .child(model.getOrderId())
//                        .updateChildren(map)
//                        .addOnCompleteListener(task -> {
//                            if (task.isSuccessful()){
//
//                                Toast.makeText(v.getContext(), "Order cancelled!", Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(v.getContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//            }
//            else if (condition.equals("Reports")){
//                Toast.makeText(v.getContext(), "No reports for this item!", Toast.LENGTH_SHORT).show();
//            }
//
//
//
//        });


    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrdersBinding binding;
        public ViewHolder( ItemOrdersBinding ordersBinding) {
            super(ordersBinding.getRoot());
            binding = ordersBinding;
        }
    }
}
