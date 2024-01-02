package com.example.bookingticket.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.bookingticket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText fullNameEditText;
    private EditText edtAddressText;
    private EditText edtPhoneText;
    private TextView btnLoginView;
    private AppCompatButton btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ImageView Back=findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                RegisterActivity.this.finish();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values before calling signUp()
                emailEditText = findViewById(R.id.edtEmail);
                passwordEditText = findViewById(R.id.edtPassword);
                fullNameEditText = findViewById(R.id.edtUsername);
                edtAddressText = findViewById(R.id.edtAddress);
                edtPhoneText = findViewById(R.id.edtPhone);

                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String fullName = fullNameEditText.getText().toString();
                String edtAddress = edtAddressText.getText().toString();
                String edtPhone = edtPhoneText.getText().toString();

                signUp(email, password, fullName, edtAddress, edtPhone);
            }
        });

        btnLoginView = findViewById(R.id.btnLoginView);
        btnLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signUp(String email, String password, String fullName, String edtAddress, String edtPhone) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("SignUpActivity", "onComplete triggered");
                        if (task.isSuccessful()) {
                            // Sign up success, update UI
                            Log.d("SignUpActivity", "createUserWithEmail:success");

                            // Get the newly created user
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Add additional user information to Firestore or Realtime Database
                            if (user != null) {
                                addUserInfoToDatabase(user.getUid(), fullName, edtAddress, edtPhone);
                            }

                            // Navigate to the main activity or perform other actions
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign up fails, display a message to the user.
                            Log.w("SignUpActivity", "createUserWithEmail:failure", task.getException());
                            // Handle errors here
                        }
                    }
                });
    }

    private void addUserInfoToDatabase(String userId, String fullName, String edtAddress, String edtPhone) {
        // Example for Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fullName);
        user.put("address", edtAddress);
        user.put("phone", edtPhone);

        db.collection("users").document(userId)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("SignUpActivity", "User information added to Firestore successfully");
                        } else {
                            Log.w("SignUpActivity", "Error adding user information to Firestore", task.getException());
                        }
                    }
                });
    }
}
