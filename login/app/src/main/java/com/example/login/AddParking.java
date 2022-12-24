package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.login.databinding.ActivityAddParkingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class AddParking extends AppCompatActivity {

    ActivityAddParkingBinding binding;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddParkingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = binding.city.getText().toString().trim();
                String street = binding.street.getText().toString().trim();
                int homeNum = Integer.parseInt(binding.homeNum.getText().toString());
                int parkingNum = Integer.parseInt(binding.parkingNum.getText().toString());
                ParkingModel parking = new ParkingModel(firebaseUser.getUid(), city, street, homeNum, parkingNum);
                firebaseFirestore.collection("Parkings").add(parking);

                Toast.makeText(AddParking.this, "Parking added successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddParking.this, Profile.class));
            }

        });


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code to go back to the previous screen goes here
                startActivity(new Intent(AddParking.this, Profile.class));
            }
        });
    }
}