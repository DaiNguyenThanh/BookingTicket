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
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookingticket.Adapters.MovieListAdapter;
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