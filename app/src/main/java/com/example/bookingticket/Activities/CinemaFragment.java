package com.example.bookingticket.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingticket.Adapters.StoreAdapter;
import com.example.bookingticket.Domain.Store;
import com.example.bookingticket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CinemaFragment extends Fragment {

    private RecyclerView recyclerView;
    private StoreAdapter storeAdapter;
    private List<Store> storeList;
    private FirebaseFirestore firestore;
    private CollectionReference storesCollection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cinema, container, false);

        firestore = FirebaseFirestore.getInstance();
        storesCollection = firestore.collection("stores");

        recyclerView = view.findViewById(R.id.recyclerViewArea);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        storeList = new ArrayList<>();
        storeAdapter = new StoreAdapter(storeList);
        recyclerView.setAdapter(storeAdapter);

        fetchDataFromFirestore();

        return view;
    }

    private void fetchDataFromFirestore() {
        storesCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Store store = document.toObject(Store.class);
                        if (store != null) {
                            storeList.add(store);
                        }
                    }
                    storeAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Error fetching data from Firestore", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
