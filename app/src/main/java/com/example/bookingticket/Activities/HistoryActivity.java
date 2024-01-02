package com.example.bookingticket.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingticket.Adapters.TicketAdapter;
import com.example.bookingticket.Domain.Ticket;
import com.example.bookingticket.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private TicketAdapter ticketAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerViewArea);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Assuming you have a TicketAdapter class for your RecyclerView
        ticketAdapter = new TicketAdapter();
        recyclerView.setAdapter(ticketAdapter);

        // Replace "yourUserId" with the actual user ID
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HistoryActivity.this);
        String userId = preferences.getString("userUid", "DEFAULT_USER_ID");

        loadTickets(userId);
    }

    private void loadTickets(String userId) {
        db.collection("tickets")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Ticket> ticketList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Ticket ticket = document.toObject(Ticket.class);
                        String filmTitle = document.getString("film");
                        ticket.setTitle(filmTitle);

                        ticketList.add(ticket);

                    }
                    ticketAdapter.setTicketList(ticketList);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

}
