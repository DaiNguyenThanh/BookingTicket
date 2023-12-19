package com.example.bookingticket.Activities;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookingticket.R;

public class AccountFragment extends Fragment {
    private  Button btnSignIn;

    private Button btnSignUp;
    private Button btnLogOut;


    // This is called when the fragment is created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Any additional setup for your fragment can go here

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

                btnLogOut = view.findViewById(R.id.btnLogOut);
        checkLoginState();
        // Set click listener
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
        // Use PreferenceManager for Fragments
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        // Update UI based on login state
        if (!isLoggedIn) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
            // User is not logged in
           // btnSignIn.setVisibility(View.VISIBLE);  // Show the sign-in button
        } else {
            // User is logged in
            //btnSignIn.setVisibility(View.GONE);  // Hide the sign-in button
            // Optionally, show user information or perform other actions for a logged-in user
        }
    }
    private void clearLoginState() {
        // Clear the isLoggedIn state
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        preferences.edit().putBoolean("isLoggedIn", false).apply();
    }

}
