package com.example.bookingticket.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookingticket.R;

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
        Intent intent = getIntent();
        String a=intent.getStringExtra("image");
        imageViewMovie=findViewById(R.id.imageViewMovie);
        textViewMovieTitle=findViewById(R.id.textViewMovieTitle);
        textViewShowTime=findViewById(R.id.textViewShowTime);
        textViewShowDate=findViewById(R.id.textViewShowDate);
        textViewCinema=findViewById(R.id.textViewCinema);
        textViewSeat=findViewById(R.id.textViewSeat);
        textViewPrice=findViewById(R.id.textViewPrice);

        textViewMovieTitle.setText(intent.getStringExtra("title"));
        textViewShowTime.setText("Time: "+intent.getStringExtra("time"));
        textViewShowDate.setText("Date: "+intent.getStringExtra("date"));
        textViewCinema.setText("Cinema: "+intent.getStringExtra("cinema"));
        textViewSeat.setText("Seat: "+intent.getStringExtra("seat"));
        Glide.with(this)
                .load( intent.getStringExtra("image"))  // Assuming getImages() returns a list of image URLs
                .into(imageViewMovie);
    }
}
