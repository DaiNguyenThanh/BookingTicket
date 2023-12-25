package com.example.bookingticket.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bookingticket.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ChangeInfor extends AppCompatActivity {
    private EditText textName;
    private EditText mobile;
    private EditText email;
    private EditText address;
    private Button btChange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_infor);

        textName = findViewById(R.id.showName);
        mobile = findViewById(R.id.showPhone);

        address = findViewById(R.id.showAddress);
        btChange = findViewById(R.id.btChange);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userUid = preferences.getString("userUid", "");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("users").document(userUid);

        // Fetch user data from Firestore and update UI
        userDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // DocumentSnapshot contains the data
                        String fullName = documentSnapshot.getString("fullName");
                        String phone = documentSnapshot.getString("phone");
                        String userAddress = documentSnapshot.getString("address");

                        // Update UI with user data
                        textName.setText(fullName);
                        mobile.setText(phone);

                        address.setText(userAddress);
                    } else {
                        // Handle the case where the document does not exist
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle exceptions
                });

        btChange.setOnClickListener(v -> {
            // Retrieve the new values from your EditText fields
            String newName = textName.getText().toString();
            String newMobile = mobile.getText().toString();

            String newAddress = address.getText().toString();

            // Create a data object with the new values
            Map<String, Object> newData = new HashMap<>();
            newData.put("fullName", newName);
            newData.put("phone", newMobile);
            newData.put("address", newAddress);

            // Update the user document in Firestore
            userDocRef.update(newData)
                    .addOnSuccessListener(aVoid -> {
                        Intent intent = new Intent(ChangeInfor.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Optional
                        // Successfully updated the document
                        // You can perform any additional actions here
                    })
                    .addOnFailureListener(e -> {
                        // Handle errors during the update
                    });
        });
    }
}
