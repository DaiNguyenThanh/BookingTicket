package com.example.bookingticket.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookingticket.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class TicketActivity extends AppCompatActivity {
    private ImageView imageViewMovie;
    private TextView textViewMovieTitle;
    private TextView textViewShowTime;

    private TextView textViewShowDate;

    private TextView textViewCinema;
    private TextView textViewSeat;
    private TextView textViewPrice;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket);
        ImageView Back=findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicketActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                TicketActivity.this.finish();
            }
        });
        imageViewMovie=findViewById(R.id.imageViewMovie);
        textViewMovieTitle=findViewById(R.id.textViewMovieTitle);
        textViewShowTime=findViewById(R.id.textViewShowTime);
        textViewShowDate=findViewById(R.id.textViewShowDate);
        textViewCinema=findViewById(R.id.textViewCinema);
        textViewSeat=findViewById(R.id.textViewSeat);
        textViewPrice=findViewById(R.id.textViewPrice);
        Intent intent = getIntent();

        try {
            String ticketId = intent.getStringExtra("ticketId");
            fetchTicketDetails(ticketId);
        }
        catch (Exception e) {
        textViewMovieTitle.setText(intent.getStringExtra("title"));
        textViewShowTime.setText("Time: "+intent.getStringExtra("time"));
        textViewShowDate.setText("Date: "+intent.getStringExtra("date"));
        textViewCinema.setText("Cinema: "+intent.getStringExtra("cinema"));
        textViewSeat.setText("Seat: "+intent.getStringExtra("seat"));
        Glide.with(this)
                .load( intent.getStringExtra("image"))
                .into(imageViewMovie);
      }
    }
    private void fetchTicketDetails(String ticketId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tickets")
                .document(ticketId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Ticket document found, extract and display information
                        String movieTitle = documentSnapshot.getString("film");
                        String showTime = documentSnapshot.getString("time");
                        String showDate = documentSnapshot.getString("date");
                        String cinema = documentSnapshot.getString("cinema");
                        String seat = documentSnapshot.getString("seatId");
                        String price = documentSnapshot.getString("price");
                        String imageUrl = documentSnapshot.getString("image");

                        // Display ticket information in TextViews and ImageView
                        textViewMovieTitle.setText("Title: " + movieTitle);
                        textViewShowTime.setText("Time: " + showTime);
                        textViewShowDate.setText("Date: " + showDate);
                        textViewCinema.setText("Cinema: " + cinema);
                        textViewSeat.setText("Seat: " + seat);
                        textViewPrice.setText("Price: " + price);

                        Glide.with(this)
                                .load(imageUrl)
                                .into(imageViewMovie);
                    } else {
                    }
                })
                .addOnFailureListener(e -> {
                });
    }
}
