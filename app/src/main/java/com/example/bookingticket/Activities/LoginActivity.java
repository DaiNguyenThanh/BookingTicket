package com.example.bookingticket.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookingticket.R;

public class LoginActivity extends AppCompatActivity {
private EditText edtPassword, edtUsername;
private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView(){
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtUsername.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter your username and pasword", Toast.LENGTH_SHORT).show();
                } else if (edtUsername.getText().toString().equals("test")&& edtPassword.getText().toString().equals("test")) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else {
                    Toast.makeText(LoginActivity.this, "Your username and pasword is not correct", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}