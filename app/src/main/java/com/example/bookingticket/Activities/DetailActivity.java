package com.example.bookingticket.Activities;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleTextView;
    private TextView descriptionTextView;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.imageView4);
        titleTextView = findViewById(R.id.textView);
        descriptionTextView = findViewById(R.id.textView11);
        int movieId = getIntent().getIntExtra("id", -1);
        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Make the network request
        String url = "https://moviesapi.ir/api/v1/movies/"+movieId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse JSON response and update UI
                        try {
                            Datum datum = parseMovieDetails(response);
                            updateUI(datum);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network errors
                    }
                }
        );

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private Datum parseMovieDetails(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("id");
        String title = jsonObject.getString("title");
        String poster = jsonObject.getString("poster");
        String year = jsonObject.getString("year");
        String rated = jsonObject.getString("rated");
        String released = jsonObject.getString("released");
        String runtime = jsonObject.getString("runtime");
        String director = jsonObject.getString("director");
        String writer = jsonObject.getString("writer");
        String actors = jsonObject.getString("actors");
        String plot = jsonObject.getString("plot");
        String country = jsonObject.getString("country");
        String awards = jsonObject.getString("awards");
        String metascore = jsonObject.getString("metascore");
        String imdbRating = jsonObject.getString("imdb_rating");
        String imdbVotes = jsonObject.getString("imdb_votes");
        String imdbId = jsonObject.getString("imdb_id");
        String type = jsonObject.getString("type");

        // Parse genres array
        List<String> genres = new ArrayList<>();
        JSONArray genresArray = jsonObject.getJSONArray("genres");
        for (int i = 0; i < genresArray.length(); i++) {
            genres.add(genresArray.getString(i));
        }

        // Parse images array
        List<String> images = new ArrayList<>();
        JSONArray imagesArray = jsonObject.getJSONArray("images");
        for (int i = 0; i < imagesArray.length(); i++) {
            images.add(imagesArray.getString(i));
        }

        // Create and return Datum object
        return new Datum(id, title, poster, year, rated, released, runtime, director,
                writer, actors, plot, country, awards, metascore, imdbRating, imdbVotes,
                imdbId, type, genres, images);
    }

    private void updateUI(Datum datum) {
        // Use a library like Volley's ImageLoader to load the image into ImageView
        ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                // Optional: Implement caching if needed
            }
        });

//        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(
//                imageView,
//                R.drawable.placeholder_image, // Placeholder image resource
//                R.drawable.error_image); // Error image resource
//
//        imageLoader.get("URL_FOR_YOUR_IMAGE", imageListener);

        // Set title and description
        titleTextView.setText(datum.getTitle());
        descriptionTextView.setText(datum.getPlot());

        List<String> images = datum.getImages();
//        if (images != null && !images.isEmpty()) {
//            // Assuming you have an ImageView for each additional image
//            ImageView additionalImageView1 = findViewById(R.id.imageView2);
//            ImageView additionalImageView2 = findViewById(R.id.imageView3);
//            Glide.with(this)
//                    .load(images.get(2))  // Assuming getPoster() returns the image URL
//                    .into(imageView);
//            // Load the first additional image
//            Glide.with(this)
//                    .load(images.get(0))
//                    .into(additionalImageView1);
//
//            // Load the second additional image
//            Glide.with(this)
//                    .load(images.get(1))
//                    .into(additionalImageView2);
//
//            // Add more ImageViews and load more images as needed
//        }
    }
}

