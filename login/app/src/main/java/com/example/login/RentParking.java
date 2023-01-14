package com.example.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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

        MapView mapView = (MapView) findViewById(R.id.mapView);

        TextView address = (TextView) findViewById(R.id.address);
        TextView owner = (TextView) findViewById(R.id.owner);
        TextView cost = (TextView) findViewById(R.id.cost);
        TextView available = (TextView) findViewById(R.id.available);
        MaterialButton rentBtn = (MaterialButton) findViewById(R.id.rent);

        Bundle bundle = getIntent().getExtras();
        address.setText(bundle.getString("Address"));
        owner.setText("Owned by: " + bundle.getString("Owner"));
        cost.setText("Hourly price: " + bundle.getString("Price"));
        available.setText("Available: From " + bundle.getString("AvailableFrom") + " To " + bundle.getString("AvailableTo"));
        String parkingId = bundle.getString("parkingId");
        Geocoder geocoder = new Geocoder(this);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocationName(address.getText().toString(), 1);
                    if(addresses.size() > 0) {
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                        googleMap.setMyLocationEnabled(true);
                        googleMap.setTrafficEnabled(true);
                        googleMap.getUiSettings().setZoomControlsEnabled(true);

                        double latitude = addresses.get(0).getLatitude();
                        double longitude = addresses.get(0).getLongitude();
                        LatLng latLng = new LatLng(latitude, longitude);
                        googleMap.addMarker(new MarkerOptions().position(latLng)
                                .title(address.getText().toString())
                                .snippet("Cost: " + cost.getText().toString()));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        DocumentReference docRef = firebaseFirestore.collection("Parkings").document(parkingId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ownerId = document.getString("ownerId");
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
            public void onClick(View view) {
                if (firebaseUser == null){
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