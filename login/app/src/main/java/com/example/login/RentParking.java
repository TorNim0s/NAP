package com.example.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

        //MapView mapView = (MapView) findViewById(R.id.mapView);

        TextView address = (TextView) findViewById(R.id.address);
        TextView owner = (TextView) findViewById(R.id.owner);
        TextView cost = (TextView) findViewById(R.id.cost);
        TextView available = (TextView) findViewById(R.id.available);
        TextView parkingId = (TextView) findViewById(R.id.parkingId);
        //TextView ownerId = (TextView) findViewById(R.id.ownerId);
        MaterialButton rentBtn = (MaterialButton) findViewById(R.id.rent);

        Bundle bundle = getIntent().getExtras();
        String postedId = bundle.getString("parkingId");
//        String postedId = "hlXNCYXVNT9fJIZA6StC";
        Log.d("------", "postedID " + postedId);
        DocumentReference docRef = firebaseFirestore.collection("PostedParking").document(postedId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("------", "00000000");
                        parkingId.setText(document.getString("parkingId"));
                        Log.d("------", "id " + parkingId.getText().toString());
                        address.setText(document.getString("address"));
                        owner.setText("Owned by: ");
                        cost.setText("Hourly price: " + document.getString("price"));
                        available.setText("Available: From " + document.get("startTime") + " To " + document.get("endTime"));
                    }
                }
            }
        });

        Log.d("------", "111111111");
        String parkID = parkingId.getText().toString();
        Log.d("------", "parkingID " + parkID);
        DocumentReference docRef1 = firebaseFirestore.collection("Parkings").document(parkID);
        Log.d("------", "222222222222");
        docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ownerId = document.getString("ownerId");
                        Log.d("------", ownerId);
                    } else {
                        Toast.makeText(RentParking.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RentParking.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("------", "33333333333333333333");

        rentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseUser == null) {
                    Intent intent = new Intent(RentParking.this, SignIn.class);
                    startActivity(intent);
                    return;
                }

                //need to change search for specific document
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