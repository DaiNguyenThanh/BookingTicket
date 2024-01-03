package com.example.bookingticket.Activities;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookingticket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountFragment extends Fragment {
    private  Button btnSignIn;

    private Button btnSignUp;
    private Button btnLogOut;
    private TextView showAddress;
    private TextView showEmail;
    private TextView showPhone;
    private TextView showName;
    private TextView btnChange;
    private ImageView btnHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnLogOut = view.findViewById(R.id.btnLogOut);
        checkLoginState();
        showName = view.findViewById(R.id.showName);
        showAddress = view.findViewById(R.id.showAddress);
        showEmail=view.findViewById((R.id.showEmail));
        showPhone=view.findViewById((R.id.showPhone));
        btnChange=view.findViewById(R.id.btnChange);
        btnHistory=view.findViewById(R.id.btnHistory);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String userUid = preferences.getString("userUid", "");
        String userName = preferences.getString("userName", "");
        String userEmail = preferences.getString("userEmail", "");
        showEmail.setText(userEmail);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), ChangeInfor.class);
                startActivity(intent);
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("users").document(userUid);

        userDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // DocumentSnapshot contains the data
                            String fullName = documentSnapshot.getString("fullName");
                            String address = documentSnapshot.getString("phone");
                            showPhone.setText(documentSnapshot.getString("address"));
                            // Update UI with additional user details
                            showName.setText(fullName);
                            showAddress.setText(address);
                        } else {
                            // Handle the case where the document does not exist
                            Log.d(TAG, "Document does not exist");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle exceptions
                        Log.e(TAG, "Error getting document", e);
                    }
                });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearLoginState();
                // Return to the main activity by restarting it
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void checkLoginState() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        } else {
        }
    }
    private void clearLoginState() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        preferences.edit().putBoolean("isLoggedIn", false).apply();
    }

}
