package com.example.bookingticket.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingticket.Domain.Store;
import com.example.bookingticket.R;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private List<Store> storeList; // Assuming you have a model class named Store

    // Constructor to initialize the adapter with a list of stores
    public StoreAdapter(List<Store> storeList) {
        this.storeList = storeList;
    }

    // ViewHolder class to hold references to the views in each item
    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        TextView txtName, txtAddress, txtPhone;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
            txtName = itemView.findViewById(R.id.txtName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtPhone = itemView.findViewById(R.id.txtPhone);
        }
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create a ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cinema_item, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        // Bind data to the views in each item
        Store store = storeList.get(position);
        holder.txtName.setText(store.getTitle());
        holder.txtAddress.setText(store.getAddress());
        holder.txtPhone.setText(store.getPhone());

        // You may use a library like Picasso or Glide to load the image
        // Example using Picasso:
        // Picasso.get().load(store.getImageUrl()).into(holder.imgItem);
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }
}