package com.example.bookingticket.Adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Make sure to include the necessary Glide dependency
import com.example.bookingticket.Activities.DetailActivity;
import com.example.bookingticket.Domain.Datum;
import com.example.bookingticket.R;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<Datum> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Datum item);
    }

    public ResultAdapter(List<Datum> items) {
        this.items = items;

    }

    public void updateList(List<Datum> updatedItems) {
        this.items = updatedItems;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Datum item = items.get(position);
        holder.textView.setText(item.getTitle());

        if (item.getImages() != null && item.getImages().size() > 0) {
            String firstImage = item.getImages().get(0);
            Glide.with(holder.itemView.getContext())
                    .load(firstImage)
                    .into(holder.imageView);
        } else {
            // Handle the case where there are no images
            // holder.imageView.setImageResource(R.drawable.placeholder_image);
        }
        holder.itemView.setOnClickListener(v -> {
            if (listener != null && item != null) {
                listener.onItemClick(item);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textViewItemResult);
            imageView = view.findViewById(R.id.imageViewItemResult);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(items.get(position));
                }
            });
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
