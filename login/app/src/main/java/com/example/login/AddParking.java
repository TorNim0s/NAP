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

/**
 * The class AddParking.java is an activity class in the android application.
 * The main function of this class is to handle the logic for adding or editing a parking spot in the application.
 * The activity layout is inflated using data binding and user input is taken from various TextViews in the layout.
 * When the user clicks the "add" button, the inputted data is used to either create a new parking spot in the
 * Firebase Firestore database or update an existing one.
 * The class also has a button for navigating back to the previous screen.
 * Additionally, the class handles the editing of a parking spot by getting the data from
 * previous activity and displaying them in the layout.
 */

public class AddParking extends AppCompatActivity {

    ActivityAddParkingBinding binding;
    FirebaseFirestore firebaseFirestore;

    private String ParkingID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddParkingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Check if the intent contains any extras, if so it means the user is trying to edit an existing parking
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            // Change the text of the button and the title to "Edit parking"
            ((TextView) findViewById(R.id.addParking)).setText("Edit parking");
            ((MaterialButton) findViewById(R.id.addBtn)).setText("Edit");
//            findViewById(R.id.file).setVisibility(View.GONE);
            // Set the parking's city, street, home number and parking number in the corresponding text fields
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
                    // Get the user input from the form fields
                    String city = binding.city.getText().toString().trim();
                    String street = binding.street.getText().toString().trim();
                    int homeNum = Integer.parseInt(binding.homeNum.getText().toString());
                    int parkingNum = Integer.parseInt(binding.parkingNum.getText().toString());
                    // Create a new ParkingModel object with the user input
                    ParkingModel parking = new ParkingModel(firebaseUser.getUid(), city, street, homeNum, parkingNum);
                    // Check if the parking ID is null, meaning that this is a new parking
                    if(ParkingID == null) {
                        // If the parking ID is null, add the new parking to the "Parkings" collection in Firestore
                        firebaseFirestore.collection("Parkings").add(parking);
                        // Show a toast message to confirm that the parking was added successfully
                        Toast.makeText(AddParking.this, "Parking added successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddParking.this, Profile.class));
                    }
                else{
                    // If the parking ID is not null, this means that the user is editing an existing parking
                        // Create a Map to store the updates
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("city", city);
                    updates.put("street", street);
                    updates.put("homeNum", homeNum);
                    updates.put("parkingNum", parkingNum);
                    // Update the parking in the "Parkings" collection in Firestore
                    firebaseFirestore.collection("Parkings").document(ParkingID).update(updates);

                    // Show a toast message to confirm that the parking was edited successfully
                    Toast.makeText(AddParking.this, "Parking edited successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddParking.this, ParkingList.class));
                }
            }

        });

        //Sets an OnClickListener to the back button
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when the back button is clicked, the user is taken back to the Profile activity
                startActivity(new Intent(AddParking.this, Profile.class));
            }
        });
    }
}