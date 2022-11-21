package com.example.foodappclient;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodappclient.databinding.ItemProductBinding;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context context;
    public static ArrayList<Products> productsList;

    ItemProductBinding binding;

    public ItemAdapter(Context context, ArrayList<Products> list) {
        this.context = context;
        this.productsList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemProductBinding.inflate(LayoutInflater.from(context));

        return  new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Products model = productsList.get(position);
    if(model!=null){


        if(model.getStock()){
            binding.imgSold.setVisibility(View.GONE);
        }else{
            binding.imgSold.setVisibility(View.VISIBLE);
        }
        binding.productTitle.setText(model.getName());
        binding.amount.setText("₹"+model.getPrice()+".0");
        try{
            Picasso.get().load(model.getImage())
                    .into(binding.productImage);
        }catch (Exception e){
            e.getMessage();
        }


        holder.itemView.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Select");
            builder.setCancelable(false);

            String[] items = {"Out Of Stock","Delete"};
            builder.setItems(items, (dialog, which) -> {
                dialog.dismiss();
                switch (which){
                    case 0:
                        setOutStock(model);

                        break;

                    case 1:
                        deletdItem(model,position);

                        break;


                }
            });
            builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

            builder.create().show();
        });



    }

    }

    private void deletdItem(Products model,int pos) {

        FirebaseDatabase.getInstance().getReference().child("Products")
                .child(model.getId())
                .removeValue();
        notifyItemRemoved(pos);
        Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
    }

    private void setOutStock(Products products) {

        HashMap<String,Object> map = new HashMap<>();
        map.put("stock",false);

        FirebaseDatabase.getInstance().getReference().child("Products")
                .child(products.getId())
                .updateChildren(map);
        Toast.makeText(context, "Item updated!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;
        public ViewHolder(@NonNull ItemProductBinding productBinding) {
            super(productBinding.getRoot());
            binding = productBinding;
        }
    }

}
