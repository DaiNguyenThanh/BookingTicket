package com.example.bookingticket.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookingticket.Adapters.MovieListAdapter;
import com.example.bookingticket.Adapters.ResultAdapter;
import com.example.bookingticket.Adapters.SliderAdapters;
import com.example.bookingticket.Domain.Datum;
import com.example.bookingticket.Domain.ListMovie;
import com.example.bookingticket.Domain.SliderItem;
import com.example.bookingticket.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private RecyclerView.Adapter adapterShowingNow, adapterComingSoon;
    private RecyclerView recyclerViewShowingNow, recyclerViewComingSoon;
    private LinearLayout tabCinema;
    private LinearLayout tabHome;
    private LinearLayout tabAccount;
    private EditText edtSearch;
    private RecyclerView recyclerView;
    private ResultAdapter resultAdapter;
    private List<String> allItems;
    private FirebaseFirestore db;
    private CollectionReference moviesCollection;

    private NestedScrollView scrollView;
    private Handler sliderHander = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        moviesCollection = db.collection("films");

        tabAccount = findViewById(R.id.tabAccount);
        tabAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView = findViewById(R.id.fragmentContainer);
                scrollView.removeAllViews();
                scrollView.scrollTo(0, 0);
                AccountFragment accountFragment = new AccountFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, accountFragment).commit();
            }
        });

        tabCinema = findViewById(R.id.tabCinema);
        tabCinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView = findViewById(R.id.fragmentContainer);
                scrollView.removeAllViews();
                scrollView.scrollTo(0, 0);
                CinemaFragment cinemaFragment = new CinemaFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, cinemaFragment).commit();
            }
        });

        tabHome = findViewById(R.id.tabHome);
        tabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        initView();
        banner();
        sendRequest();
        sendRequestComingSoon(); // Uncomment and implement if needed

        // Initialize your items (replace this with your data)

        // Add items to allItems...

        // Initialize UI components
        // Initialize UI components
        edtSearch = findViewById(R.id.edtSearch);
        recyclerView = findViewById(R.id.recyclerView);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        ResultAdapter resultAdapter1= new ResultAdapter(new ArrayList<>()); // Use ResultAdapter if that's the actual adapter you are using
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(resultAdapter1);

        // Set up TextWatcher for the search functionality
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Filter the movies based on the search text
                loadMovies(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    private void loadMovies(String query) {
        // Reference to the "films" collection
        db.collection("films")
                .whereGreaterThanOrEqualTo("title", query)
                .whereLessThanOrEqualTo("title", query + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // List to store movies
                        List<Datum> movies = new ArrayList<>();

                        // Iterate through the documents in the collection
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convert each document to a Movie object
                            Datum movie = document.toObject(Datum.class);
                            movies.add(movie);
                        }

                        // Update the RecyclerView with the list of movies
                        if(!movies.isEmpty())
                         resultAdapter.updateList(movies);
                    } else {
                        // Handle errors
                        // Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
    private List<String> filterItems(String query) {
        List<String> filteredList = new ArrayList<>();
        for (String item : allItems) {
            // Implement your filtering logic here
            if (item.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    private void sendRequest() {
        moviesCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Datum> data = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Datum datum = document.toObject(Datum.class);
                        datum.setId(document.getId());

                        data.add(datum);
                    }

                    ListMovie listMovie = new ListMovie();
                    listMovie.setData(data);
                    adapterShowingNow = new MovieListAdapter(listMovie);
                    recyclerViewShowingNow.setAdapter(adapterShowingNow);
                })
                .addOnFailureListener(e -> {
                    Log.e("error", "Error getting movies: ", e);
                });
    }
    private void sendRequestComingSoon() {
        moviesCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Datum> data = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Datum datum = document.toObject(Datum.class);
                        datum.setId(document.getId());
                        data.add(datum);
                    }

                    ListMovie listMovie = new ListMovie();
                    listMovie.setData(data);
                    adapterShowingNow = new MovieListAdapter(listMovie);
                    recyclerViewComingSoon.setAdapter(adapterShowingNow);
                })
                .addOnFailureListener(e -> {
                    Log.e("error", "Error getting movies: ", e);
                });
    }
    // Implement sendRequestComingSoon() if needed

    private void banner() {
        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.slider1));
        sliderItems.add(new SliderItem(R.drawable.slider2));
        sliderItems.add(new SliderItem(R.drawable.slider3));
        sliderItems.add(new SliderItem(R.drawable.slider4));

        viewPager2.setAdapter(new SliderAdapters(sliderItems, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(4);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHander.removeCallbacks(sliderRunnable);
            }
        });
    }

    private Runnable sliderRunnable = () -> viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);

    @Override
    protected void onPause() {
        super.onPause();
        sliderHander.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHander.postDelayed(sliderRunnable, 2000);
    }

    private void initView() {
        viewPager2 = findViewById(R.id.viewpagaSlider);
        recyclerViewShowingNow = findViewById(R.id.viewNow);
        recyclerViewShowingNow.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewComingSoon = findViewById(R.id.viewComing);
        recyclerViewComingSoon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

}