package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class RentParking extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_parking);

        MaterialButton back = (MaterialButton) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RentParking.this, MainActivity.class);
                startActivity(intent);
            }
        });

        TextView address = (TextView)findViewById(R.id.address);
        TextView owner = (TextView)findViewById(R.id.owner);
        TextView cost = (TextView)findViewById(R.id.cost);
        TextView available = (TextView)findViewById(R.id.available);

        Bundle bundle = getIntent().getExtras();
        address.setText(bundle.getString("Address"));
        owner.setText("Owner: " + bundle.getString("Owner"));
        cost.setText("Hourly price: " + bundle.getString("Price"));
        available.setText("Available until: " + bundle.getString("Hours"));

    }

    // Load and use views afterwards

}