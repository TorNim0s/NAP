package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;


public class RentParking extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    String ownerId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_parking);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        TextView address = (TextView) findViewById(R.id.address);
        TextView owner = (TextView) findViewById(R.id.owner);
        TextView cost = (TextView) findViewById(R.id.cost);
        TextView available = (TextView) findViewById(R.id.available);
        MaterialButton rentBtn = (MaterialButton) findViewById(R.id.rent);

        Bundle bundle = getIntent().getExtras();
        address.setText(bundle.getString("Address"));
        owner.setText("Owned by: " + bundle.getString("Owner"));
        cost.setText("Hourly price: " + bundle.getString("Price"));
        available.setText("Available until: " + bundle.getString("Hours"));
        String parkingId = bundle.getString("parkingId");
        DocumentReference docRef = firebaseFirestore.collection("Parkings").document(parkingId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ownerId = document.getString("id");
                    } else {
                        Toast.makeText(RentParking.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RentParking.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //need to change search for specific document
                Query query = firebaseFirestore.collection("PostedParking").whereEqualTo("parkingId", parkingId);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            if (!ownerId.equals(firebaseUser.getUid())) {
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("status", "Rented");
                                updates.put("renterId", firebaseUser.getUid());
                                document.getReference().update(updates);
                                Toast.makeText(RentParking.this, "Parking rented successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RentParking.this, MainActivity.class));
                            } else {
                                Toast.makeText(RentParking.this, "You can't rent your own parking", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RentParking.this, "Error retrieving parking data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        MaterialButton back = (MaterialButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RentParking.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}