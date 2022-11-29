package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);

        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);

        MaterialButton signup = (MaterialButton) findViewById(R.id.signup);

        MaterialButton temp = (MaterialButton) findViewById(R.id.temp);

        //admin admin

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    //correct
                    Toast.makeText(SignIn.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignIn.this, RentParking.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(SignIn.this, "LOGIN FAILED!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, PostParking.class);
                startActivity(intent);
            }
        });
    }


}