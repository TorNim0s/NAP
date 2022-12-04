package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class RentParking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_parking);

        Bundle bundle = getIntent().getExtras();
        Log.d("demo", bundle.getString("Owner"));
        Log.d("demo", bundle.getString("Address"));
        Log.d("demo", bundle.getString("Hours"));
        Log.d("demo", bundle.getString("Price"));
    }
}