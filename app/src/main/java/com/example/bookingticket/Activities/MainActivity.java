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
import android.os.Bundle;
import android.os.Handler;
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
import com.example.bookingticket.Domain.ListMovie;
import com.example.bookingticket.Domain.SliderItem;
import com.example.bookingticket.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private RecyclerView.Adapter adapterShowingNow, adapterComingSoon;
    private RecyclerView recyclerViewShowingNow, recyclerViewComingSoon;
    private StringRequest mStringRequest, mStringRequest2, mStringRequest3;
    private RequestQueue  mRequestQueue;
    private LinearLayout tabCinema;
    private LinearLayout tabHome;


    private NestedScrollView scrollView;
    private Handler sliderHander = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                // Return to the main activity by restarting it
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        initView();
        banner();
        sendRequest();
        sendRequestComingSoon();

    }

    private void sendRequest() {
        mRequestQueue = Volley.newRequestQueue(this);
        mStringRequest = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ListMovie items = gson.fromJson(response, ListMovie.class);
                adapterShowingNow = new MovieListAdapter(items);
                recyclerViewShowingNow.setAdapter(adapterShowingNow);
            }
        }, error -> Log.i("errror", "onErrorRequest: " + error.toString()));
        mRequestQueue.add(mStringRequest);
    }
    private void sendRequestComingSoon() {
        mRequestQueue = Volley.newRequestQueue(this);
        mStringRequest2 = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ListMovie items = gson.fromJson(response, ListMovie.class);
                adapterComingSoon = new MovieListAdapter(items);
                recyclerViewComingSoon.setAdapter(adapterComingSoon);
            }
        }, error -> Log.i("errror", "onErrorRequest: " + error.toString()));
        mRequestQueue.add(mStringRequest2);
    }

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
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
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
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHander.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHander.postDelayed(sliderRunnable,2000);
    }

    private void initView() {
        viewPager2 = findViewById(R.id.viewpagaSlider);
        recyclerViewShowingNow = findViewById(R.id.viewNow);
        recyclerViewShowingNow.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        recyclerViewComingSoon = findViewById(R.id.viewComing);
        recyclerViewComingSoon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

    }
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

}