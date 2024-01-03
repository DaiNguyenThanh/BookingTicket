package com.example.bookingticket.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookingticket.Domain.Datum;
import com.example.bookingticket.R;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView showDuration;
    private TextView showRating;
    private Spinner spinnerTime;
    private Spinner spinnerDate;
    private Spinner spinnerCinema;
    private Spinner spinnerShowingID;

    private Button btnContinue;

    private FirebaseFirestore db;
    private String firstImageUrl;
    private String showingId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_activity);
        imageView = findViewById(R.id.showImage);
        titleTextView = findViewById(R.id.showFilmName);
        db = FirebaseFirestore.getInstance();
        ImageView Back=findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showDuration = findViewById(R.id.showDuration);
        showRating = findViewById(R.id.showRating);
        spinnerTime = findViewById(R.id.spinnerTime);
        spinnerDate = findViewById(R.id.spinnerDate);
        spinnerShowingID = findViewById(R.id.spinnerShowingid);


        btnContinue=findViewById(R.id.btnContinue);
        spinnerCinema = findViewById(R.id.spinnerCinema);
        String movieId = getIntent().getStringExtra("id");
        fetchCinema(movieId);

        spinnerCinema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCinema = spinnerCinema.getSelectedItem().toString();
                fetchShowingTimeAndDate(movieId, spinnerCinema.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        fetchMovieDetails(movieId);
//        fetchShowing(movieId);
//        fetchDate(movieId);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String selectedCinema = spinnerCinema.getSelectedItem().toString();
                    String selectedTime = spinnerTime.getSelectedItem().toString();
                    String selectedDate = spinnerDate.getSelectedItem().toString();
                    String selectedshowingID = spinnerShowingID.getSelectedItem().toString();

                    if(!selectedCinema.isEmpty()&&!selectedTime.isEmpty()&&!selectedDate.isEmpty()) {

                        Intent intent = new Intent(BookingActivity.this, SeatActivity.class);
                        intent.putExtra("cinema", selectedCinema);
                        intent.putExtra("time", selectedTime);
                        intent.putExtra("date", selectedDate);
                        intent.putExtra("title", titleTextView.getText());
                        intent.putExtra("image", firstImageUrl);
                        intent.putExtra("id", getIntent().getStringExtra("id"));
                        intent.putExtra("showingId", selectedshowingID);

                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(BookingActivity.this, "Please select cinema, time, and date", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(BookingActivity.this, "Not have Showing, PLease try later", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void fetchShowingTimeAndDate(String movieId, String selectedCinema) {
        // Query to fetch cinema document based on the selected cinema name
        db.collection("stores")
                .whereEqualTo("title", selectedCinema)
                .get()
                .addOnSuccessListener(cinemaQuerySnapshot -> {
                    if (!cinemaQuerySnapshot.isEmpty()) {
                        // Assuming there's only one document with the given name
                        String cinemaId = cinemaQuerySnapshot.getDocuments().get(0).getId();

                        // Query the showing documents using the cinema ID
                        db.collection("showing")
                                .whereEqualTo("filmId", movieId)
                                .whereArrayContains("stores", cinemaId)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    List<String> timeOptions = new ArrayList<>();
                                    List<String> dateOptions = new ArrayList<>();
                                    List<String> showingIds = new ArrayList<>(); // List to store showing IDs

                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        String time = document.getString("time");
                                        Date date = document.getDate("date");
                                        String showingId = document.getId(); // Get the showing ID

                                        if (time != null && !timeOptions.contains(time)) {
                                            timeOptions.add(time);
                                        }

                                        if (date != null) {
                                            String formattedDate = formatDate(date);
                                            if (!dateOptions.contains(formattedDate)) {
                                                dateOptions.add(formattedDate);
                                            }
                                        }

                                        if (!showingIds.contains(showingId)) {
                                            showingIds.add(showingId);
                                        }
                                    }

                                    updateSpinnerTime(timeOptions);
                                    updateSpinnerDate(dateOptions);
                                    updateSpinnerShowingId(showingIds);
                                })
                                .addOnFailureListener(e -> {
                                });
                    } else {
                    }
                })
                .addOnFailureListener(e -> {
                });
    }
    private void updateSpinnerShowingId(List<String> showingIds) {
        ArrayAdapter<String> showingIdAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, showingIds);
        Spinner spinnerShowingId = findViewById(R.id.spinnerShowingid);
        spinnerShowingId.setAdapter(showingIdAdapter);
    }
    private void fetchMovieDetails(String movieId) {
        db.collection("films").document(movieId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Datum datum = documentSnapshot.toObject(Datum.class);
                        updateUI(datum);
                    } else {
                    }
                })
                .addOnFailureListener(e -> {
                });
    }
    private void fetchShowing(String movieId){
        db.collection("showing")
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
                });
    }
    private Map<String, String> storeIdToNameMap = new HashMap<>();

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

                    fetchStoreNames(storeIds);
                })
                .addOnFailureListener(e -> {
                });
    }

    private void fetchStoreNames(List<String> storeIds) {
        List<Task<String>> tasks = new ArrayList<>();

        for (String storeId : storeIds) {
            Task<String> storeNameTask = fetchStoreName(storeId);
            tasks.add(storeNameTask);
        }

        Tasks.whenAllSuccess(tasks)
                .addOnSuccessListener(storeNames -> {
                    List<String> storeNamesList = new ArrayList<>();
                    for (Object storeName : storeNames) {
                        if (storeName instanceof String) {
                            storeNamesList.add((String) storeName);
                        }
                    }
                    updateSpinnerCinema(storeNamesList);
                })
                .addOnFailureListener(e -> {
                });
    }

    private Task<String> fetchStoreName(String storeId) {
        final TaskCompletionSource<String> tcs = new TaskCompletionSource<>();

        db.collection("stores")
                .document(storeId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String storeName = documentSnapshot.getString("title");
                        if (storeName != null) {
                            storeIdToNameMap.put(storeId, storeName);
                            tcs.setResult(storeName);
                        }
                    } else {
                        tcs.setException(new Exception("Store document doesn't exist"));
                    }
                })
                .addOnFailureListener(e -> {
                    tcs.setException(e);
                });

        return tcs.getTask();
    }
    private void fetchDate(String movieId){
        db.collection("showing")
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


        String genresString = String.join(", ", datum.getGenres());

        if (!datum.getImages().isEmpty()) {
            firstImageUrl = datum.getImages().get(0);

            Glide.with(this)
                    .load(datum.getImages().get(0))
                    .into(imageView);
        }
    }
    private String formatDate(Date date) {
        // Define the date format you want
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }
}
