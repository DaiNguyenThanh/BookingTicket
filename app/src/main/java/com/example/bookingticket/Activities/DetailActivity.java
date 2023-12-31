package com.example.bookingticket.Activities;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.bookingticket.Domain.Datum;
import com.example.bookingticket.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView showDuration;
    private TextView showRating;
    private TextView showReleasedDate;
    private TextView showCountry;
    private TextView showGenres;
    private TextView showDirector;
    private TextView showActor;
    private Button buttonBooking;
//    private ImageView Back;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.showImage);
        titleTextView = findViewById(R.id.showFilmName);
        descriptionTextView = findViewById(R.id.showPlot);
        showDuration = findViewById(R.id.showDuration);
        showRating = findViewById(R.id.showRating);
        showReleasedDate = findViewById(R.id.showReleasedDate);
        showCountry = findViewById(R.id.showCountry);
        showGenres = findViewById(R.id.showGenres);
        showDirector = findViewById(R.id.showDirector);
        showActor = findViewById(R.id.showActor);
        buttonBooking = findViewById(R.id.btnBooking);
        ImageView Back=findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        db = FirebaseFirestore.getInstance();

        buttonBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLoginState();

            }
        });

        String movieId = getIntent().getStringExtra("id");

        fetchMovieDetails(movieId);
    }
    private void checkLoginState() {
        // Use PreferenceManager for Fragments
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        // Update UI based on login state
        if (!isLoggedIn) {
            Intent intent = new Intent(DetailActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            DetailActivity.this.finish();
            // User is not logged in
            // btnSignIn.setVisibility(View.VISIBLE);  // Show the sign-in button
        } else {
            Intent intent = new Intent(DetailActivity.this, BookingActivity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            startActivity(intent);
            // User is logged in
            //btnSignIn.setVisibility(View.GONE);  // Hide the sign-in button
            // Optionally, show user information or perform other actions for a logged-in user
        }
    }
    private void fetchMovieDetails(String movieId) {
        db.collection("films").document(movieId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Datum datum = documentSnapshot.toObject(Datum.class);
                        updateUI(datum);
                    } else {
                        // Handle the case where the document doesn't exist
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors while fetching data
                });
    }

    private void updateUI(Datum datum) {
        titleTextView.setText(datum.getTitle());
        descriptionTextView.setText(datum.getPlot());
        showDuration.setText(datum.getRuntime());
        showRating.setText(datum.getImdbRating());
        showReleasedDate.setText(datum.getReleased());
        showCountry.setText(datum.getCountry());

        // Convert the list of genres to a comma-separated string
        String genresString = String.join(", ", datum.getGenres());
        showGenres.setText(genresString);

        showDirector.setText(datum.getDirector());
        showActor.setText(datum.getActors());

        // Assuming you have an ImageView for each additional image
        if (!datum.getImages().isEmpty()) {
            Glide.with(this)
                    .load(datum.getImages().get(0))  // Assuming getImages() returns a list of image URLs
                    .into(imageView);
        }
    }
}

