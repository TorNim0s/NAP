package com.example.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The class MainActivity.java is the main activity of the application.
 * It is responsible for displaying a list of parking spots (ActiveParking) that have been posted by users.
 * The activity starts by checking if the user is logged in, if so, the "Login" button is hidden
 * and the "Post Parking" and "Profile" buttons are shown.
 * The parking spots are displayed in a recycler view using the adapter class PostedParkingAdapter.
 * The activity also has event listeners for the login, post parking, and profile buttons.
 * The event listener for the parking spots is implemented using Firestore and listens for any
 * changes made to the collection "PostedParking" and updates the recycler view accordingly.
 * Additionally, it uses a ProgressDialog to inform the user that the data is being fetched.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    RecyclerView recyclerView;
    PostedParkingAdapter myAdapter;
    ArrayList<ActiveParking> parkingList;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton loginBtn = (MaterialButton) findViewById(R.id.login);
        MaterialButton postParkingBtn = (MaterialButton) findViewById(R.id.PostParkingButton);
        MaterialButton profileBtn = (MaterialButton) findViewById(R.id.ProfileButton);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Show a progress dialog while data is being fetched
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        // Get the current user and check if they are logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //User is Logged in
            loginBtn.setVisibility(View.GONE); // Delete loginBtn if already in user
            profileBtn.setVisibility(View.VISIBLE);
        } else {
            postParkingBtn.setVisibility(View.GONE);
            //No User is Logged in
        }
        // Initialize the recycler view and set its layout manager
        recyclerView = findViewById(R.id.parkingList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Initialize Firebase Firestore and set up a listener to listen for changes to the "PostedParking" collection
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Initialize the adapter and set it to the recycler view
        parkingList = new ArrayList<>();
        myAdapter = new PostedParkingAdapter(this, parkingList);
        recyclerView.setAdapter(myAdapter);

        //EventChangeListener();
        // Set up onClickListeners for the buttons
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
            }
        });

        postParkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PostParking.class);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Profile.class));
            }
        });


    }

    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //map.setTrafficEnabled(true);
        //map.setMyLocationEnabled(true);
        map.setOnMarkerClickListener(this);

        LatLng telAviv = new LatLng(32.073658, 34.790480);
        map.moveCamera(CameraUpdateFactory.newLatLng(telAviv));
        map.animateCamera(CameraUpdateFactory.zoomTo(12));

        EventChangeListener();
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        String postedId = (String) marker.getTag();
        Intent intent = new Intent(this, RentParking.class);
        intent.putExtra("ids", postedId);
        startActivity(intent);
        return true;
    }

    private void EventChangeListener() {
//        // Listen for changes to the "PostedParking" collection in Firebase Firestore
        Geocoder geocoder = new Geocoder(this);

        firebaseFirestore.collection("PostedParking")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        // If there is an error, log it and dismiss the progress dialog
                        if (error != null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        // For each change to the collection, check if it is an ADDED change
                        // and if the parking is available
                        // If so, add it to the parkingList and update the adapter
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                ActiveParking activeParking = dc.getDocument().toObject(ActiveParking.class);

                                if (activeParking.status.equals("Available")) {
                                    try {
                                        String[] temp = activeParking.address.split(",");
                                        StringBuilder add = new StringBuilder();
                                        for (int i = 0; i < 2; i++) {
                                            add.append(temp[i]);
                                        }
                                        List<Address> addresses = geocoder.getFromLocationName(add.toString(), 1);
                                        if (addresses.size() > 0) {
                                            double latitude = addresses.get(0).getLatitude();
                                            double longitude = addresses.get(0).getLongitude();
                                            LatLng latLng = new LatLng(latitude, longitude);
                                            Objects.requireNonNull(map.addMarker(new MarkerOptions().position(latLng)
                                                            .title(activeParking.address)
                                                            .snippet("Cost: " + activeParking.price)))
                                                    .setTag(dc.getDocument().getId() + "," + activeParking.parkingId + "," + activeParking.ownerId);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            // If progress dialog is showing, dismiss it
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    }
                });
    }

}