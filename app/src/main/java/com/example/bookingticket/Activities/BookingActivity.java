package com.example.bookingticket.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookingticket.Domain.Datum;
import com.example.bookingticket.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView showDuration;
    private TextView showRating;
    private Spinner spinnerTime;
    private Spinner spinnerDate;
    private Spinner spinnerCinema;
    private Button btnContinue;

    private FirebaseFirestore db;
    private String firstImageUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_activity);
        imageView = findViewById(R.id.showImage);
        titleTextView = findViewById(R.id.showFilmName);
        db = FirebaseFirestore.getInstance();

        showDuration = findViewById(R.id.showDuration);
        showRating = findViewById(R.id.showRating);
        spinnerTime = findViewById(R.id.spinnerTime);
        spinnerDate = findViewById(R.id.spinnerDate);
        btnContinue=findViewById(R.id.btnContinue);
        spinnerCinema = findViewById(R.id.spinnerCinema);

        String movieId = getIntent().getStringExtra("id");
        fetchMovieDetails(movieId);
        fetchShowing(movieId);
        fetchDate(movieId);
        fetchCinema(movieId);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCinema = spinnerCinema.getSelectedItem().toString();
                String selectedTime = spinnerTime.getSelectedItem().toString();
                String selectedDate = spinnerDate.getSelectedItem().toString();

                Intent intent = new Intent(BookingActivity.this, SeatActivity.class);
                intent.putExtra("cinema", selectedCinema);
                intent.putExtra("time", selectedTime);
                intent.putExtra("date", selectedDate);
                intent.putExtra("title", titleTextView.getText());
                intent.putExtra("image", firstImageUrl);
                intent.putExtra("id", getIntent().getStringExtra("id"));

                startActivity(intent);
            }
        });
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
    private void fetchShowing(String movieId){
        db.collection("showing") // Replace "your_collection_name" with the actual name of your collection
                .whereEqualTo("filmId", movieId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> timeOptions = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String time = document.getString("time");
                        if (time != null && !timeOptions.contains(time)) {
                            timeOptions.add(time);
                        }
                    }

                    updateSpinnerTime(timeOptions);
                })
                .addOnFailureListener(e -> {
                    // Handle errors while fetching data
                });
    }
    private void fetchCinema(String movieId) {
        db.collection("showing")
                .whereEqualTo("filmId", movieId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> storeIds = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        List<String> stores = (List<String>) document.get("stores");
                        if (stores != null) {
                            storeIds.addAll(stores);
                        }
                    }

                    // Fetch store names based on store IDs
                    fetchStoreNames(storeIds);
                })
                .addOnFailureListener(e -> {
                    // Handle errors while fetching data
                });
    }

    private void fetchStoreNames(List<String> storeIds) {
        List<String> storeNames = new ArrayList<>();

        for (String storeId : storeIds) {
            db.collection("stores")
                    .document(storeId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String storeName = documentSnapshot.getString("title");
                            if (storeName != null) {
                                storeNames.add(storeName);
                                updateSpinnerCinema(storeNames);
                            }
                        } else {
                            // Handle the case where the store document doesn't exist
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle errors while fetching store names
                    });
        }
    }
    private void fetchDate(String movieId){
        db.collection("showing") // Replace "your_collection_name" with the actual name of your collection
                .whereEqualTo("filmId", movieId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> dateOptions = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Date date = document.getDate("date");
                        if (date != null && !dateOptions.contains(date)) {
                            String formattedDate = formatDate(date);
                            dateOptions.add(formattedDate);
                        }

                    }

                    updateSpinnerDate(dateOptions);
                })
                .addOnFailureListener(e -> {
                    // Handle errors while fetching data
                });
    }
    private void updateSpinnerCinema(List<String> timeOptions) {
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeOptions);
        spinnerCinema.setAdapter(timeAdapter);


    }
    private void updateSpinnerDate(List<String> timeOptions) {
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeOptions);
        spinnerDate.setAdapter(timeAdapter);
    }
    private void updateSpinnerTime(List<String> timeOptions) {
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeOptions);
        spinnerTime.setAdapter(timeAdapter);
    }
    private void updateUI(Datum datum) {
        titleTextView.setText(datum.getTitle());

        showDuration.setText(datum.getRuntime());
        showRating.setText(datum.getImdbRating());


        // Convert the list of genres to a comma-separated string
        String genresString = String.join(", ", datum.getGenres());

        // Assuming you have an ImageView for each additional image
        if (!datum.getImages().isEmpty()) {
            firstImageUrl = datum.getImages().get(0);

            Glide.with(this)
                    .load(datum.getImages().get(0))  // Assuming getImages() returns a list of image URLs
                    .into(imageView);
        }
    }
    private String formatDate(Date date) {
        // Define the date format you want
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }
}
