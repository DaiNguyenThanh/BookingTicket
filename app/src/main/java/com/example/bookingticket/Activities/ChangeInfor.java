package com.example.bookingticket.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
        ImageView Back=findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        address = findViewById(R.id.showAddress);
        btChange = findViewById(R.id.btChange);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userUid = preferences.getString("userUid", "");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("users").document(userUid);

        userDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String fullName = documentSnapshot.getString("fullName");
                        String phone = documentSnapshot.getString("phone");
                        String userAddress = documentSnapshot.getString("address");

                        textName.setText(fullName);
                        mobile.setText(phone);

                        address.setText(userAddress);
                    } else {
                    }
                })
                .addOnFailureListener(e -> {
                });

        btChange.setOnClickListener(v -> {
            String newName = textName.getText().toString();
            String newMobile = mobile.getText().toString();

            String newAddress = address.getText().toString();

            Map<String, Object> newData = new HashMap<>();
            newData.put("fullName", newName);
            newData.put("phone", newMobile);
            newData.put("address", newAddress);

            userDocRef.update(newData)
                    .addOnSuccessListener(aVoid -> {
                        Intent intent = new Intent(ChangeInfor.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                    });
        });
    }
}
