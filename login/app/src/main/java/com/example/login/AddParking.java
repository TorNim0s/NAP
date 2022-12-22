package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.login.databinding.ActivityAddParkingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
        DocumentReference userRef = firebaseFirestore.collection("User").document(firebaseUser.getUid());

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = binding.city.getText().toString().trim();
                String street = binding.street.getText().toString().trim();
                int homeNum = Integer.parseInt(binding.homeNum.getText().toString());
                int parkingNum = Integer.parseInt(binding.homeNum.getText().toString());
                ParkingModel parking = new ParkingModel(firebaseUser.getUid(), city, street, homeNum, parkingNum);
                Task<DocumentReference> parkingRef = firebaseFirestore.collection("Parkings").add(parking);

//                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                UserModel user = document.toObject(UserModel.class);
//                                assert user != null;
//                                parkingRef.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                    @Override
//                                    public void onSuccess(DocumentReference documentReference) {
//                                        user.addParking(documentReference.getId());
//                                        userRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                            }
//                                        });
//                                    }
//                                });
//                            }
//                        }
//                    }
//                });

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