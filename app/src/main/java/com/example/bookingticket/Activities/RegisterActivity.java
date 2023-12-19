package com.example.bookingticket.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bookingticket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText fullNameEditText;
//    private EditText birthdayEditText;
//    private EditText genderEditText;
    private TextView btnLoginView;
    private AppCompatButton btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        btnSignUp=findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
                FirebaseUser user = mAuth.getCurrentUser();
                String fullName = fullNameEditText.getText().toString();

                addUserInfoToDatabase(user.getUid(), fullName);
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        btnLoginView=findViewById(R.id.btnLoginView);
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
    private void signUp() {
        emailEditText=findViewById(R.id.edtEmail);
        passwordEditText=findViewById(R.id.edtPassword);
        fullNameEditText=findViewById(R.id.edtUsername);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String fullName = fullNameEditText.getText().toString();
//        String birthday = birthdayEditText.getText().toString();
//        String gender = genderEditText.getText().toString();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, update UI
                            Log.d("SignUpActivity", "createUserWithEmail:success");

                            // Get the newly created user
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Add additional user information to Firestore or Realtime Database
                            if (user != null) {
                                addUserInfoToDatabase(user.getUid(), fullName);
                            }

                            // Navigate to the main activity or perform other actions
                        } else {
                            // If sign up fails, display a message to the user.
                            Log.w("SignUpActivity", "createUserWithEmail:failure", task.getException());
                            // Handle errors here
                        }
                    }
                });


    }
    private void addUserInfoToDatabase(String userId, String fullName) {
        // Example for Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fullName);
//        user.put("birthday", birthday);
//        user.put("gender", gender);

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("SignUpActivity", "User information added to Firestore successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("SignUpActivity", "Error adding user information to Firestore", e);
                    }
                });
    }
}