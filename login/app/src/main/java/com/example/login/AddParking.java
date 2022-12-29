package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.databinding.ActivityAddParkingBinding;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class AddParking extends AppCompatActivity {

    ActivityAddParkingBinding binding;
    FirebaseFirestore firebaseFirestore;

    private String ParkingID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddParkingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            ((TextView) findViewById(R.id.addParking)).setText("Edit parking");
            ((MaterialButton) findViewById(R.id.addBtn)).setText("Edit");
//            findViewById(R.id.file).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.city)).setText(bundle.getString("City"));
            ((TextView) findViewById(R.id.street)).setText(bundle.getString("Street"));
            ((TextView) findViewById(R.id.homeNum)).setText(bundle.getString("homeNum"));
            ((TextView) findViewById(R.id.parkingNum)).setText(bundle.getString("parkingNum"));
            ParkingID = bundle.getString("parkingId");
        }

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
                if(ParkingID == null) {
                    firebaseFirestore.collection("Parkings").add(parking);

                    Toast.makeText(AddParking.this, "Parking added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddParking.this, Profile.class));
                }
                else{
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("city", city);
                    updates.put("street", street);
                    updates.put("homeNum", homeNum);
                    updates.put("parkingNum", parkingNum);

                    firebaseFirestore.collection("Parkings").document(ParkingID).update(updates);

                    Toast.makeText(AddParking.this, "Parking edited successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddParking.this, ParkingList.class));
                }
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