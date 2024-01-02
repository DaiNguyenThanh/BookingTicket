package com.example.bookingticket.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookingticket.Domain.Ticket;
import com.example.bookingticket.R;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private List<Ticket> ticketList;

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);
        // Bind the ticket data to the ViewHolder
        holder.bind(ticket);
    }

    @Override
    public int getItemCount() {
        return ticketList != null ? ticketList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Define your views in the ticket_item layout
        private ImageView imgItem;
        private TextView txtFilm;
        private TextView txtPrice;
        private TextView txtCinema;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your views
            txtCinema = itemView.findViewById(R.id.txtName);
            imgItem = itemView.findViewById(R.id.imgItem);
            txtFilm = itemView.findViewById(R.id.txtFilm);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            // Other view bindings...
        }

        public void bind(Ticket ticket) {
            txtCinema.setText(ticket.getCinema());
            txtFilm.setText(ticket.getTitle());
            txtPrice.setText(ticket.getPrice().toString());
            Glide.with(itemView.getContext())
                    .load(ticket.getImage())
                    .into(imgItem);
            // Bind ticket data to views
//            txtName.setText(ticket.getName());
//            txtAddress.setText(ticket.getAddress());
            // Other view bindings...
        }
    }
}