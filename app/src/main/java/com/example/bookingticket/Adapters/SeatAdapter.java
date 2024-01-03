package com.example.bookingticket.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bookingticket.Domain.Seat;
import com.example.bookingticket.R;

import java.util.List;

public class SeatAdapter extends BaseAdapter {
    private List<Seat> seatList;
    private LayoutInflater inflater;

    public SeatAdapter(Context context, List<Seat> seatList) {
        this.seatList = seatList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return seatList.size();
    }

    @Override
    public Seat getItem(int position) {
        return seatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.grid_item_seat, parent, false);
            holder = new ViewHolder();
            holder.seatNumberTextView = view.findViewById(R.id.seatNumberTextView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Seat seat = getItem(position);

        holder.seatNumberTextView.setText(seat.getSeatNumber());

        if (seat.isOccupied()) {
            view.setBackgroundResource(R.drawable.occupied_seat_background1);
        } else {
            view.setBackgroundResource(R.drawable.available_seat_background1);
        }

        return view;
    }

    private static class ViewHolder {
        TextView seatNumberTextView;
    }
}
