package com.example.bookingticket.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingticket.Adapters.SeatAdapter;
import com.example.bookingticket.Domain.Seat;
import com.example.bookingticket.R;

import java.util.ArrayList;
import java.util.List;

public class SeatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seat_activity);

        GridView gridView = findViewById(R.id.gridView);

        // Create a list of seats (you can replace this with your actual data)
        List<Seat> seatList = generateSeatList();

        // Create and set the adapter for the GridView
        SeatAdapter seatAdapter = new SeatAdapter(this, seatList);
        gridView.setAdapter(seatAdapter);
    }

    private List<Seat> generateSeatList() {
        // Replace this with your logic to generate a list of seats
        List<Seat> seatList = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            seatList.add(new Seat("Seat " + i, i % 2 == 0)); // Alternate between occupied and available
        }
        return seatList;
    }
}