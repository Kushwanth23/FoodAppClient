package com.example.foodappclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodappclient.Model.CartModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    Context context;
    private ArrayList<CartModel> list;


    public DetailsAdapter(Context context, ArrayList<CartModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (from.equals("cart")){
//            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false));
//
//        }
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_details, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartModel model = list.get(position);
        assert model !=null;

        holder.price.setText(new StringBuilder("₹").append(model.getPrice()));
        holder.productName.setText(model.getName());
        try {
            Picasso.get().load(model.getImage()).placeholder(R.drawable.fruits)
                    .into(holder.productImage);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName,quantity,price;
        ImageView productImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            quantity = itemView.findViewById(R.id.itemCount);
            price = itemView.findViewById(R.id.price);
            productImage = itemView.findViewById(R.id.productImage);



        }
    }
}
