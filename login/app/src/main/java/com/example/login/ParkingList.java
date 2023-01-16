package com.example.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * The class ParkingList is an activity that displays a list of parking spots owned by the currently logged-in user.
 * It retrieves the data from Firestore and displays it in a RecyclerView.
 * The activity also has a back button that allows the user to navigate back to the previous screen.
 * The data is retrieved from the "Parkings" collection in Firestore and filtered by the owner's id.
 * It also has a progress dialog that appears while the data is being fetched from Firestore.
 * The parking spots are represented by the ParkingModel class and are displayed using the OwnedParkingAdapter class.
 */
public class ParkingList extends AppCompatActivity {

    // Declaring variables for recycler view, adapter, FirebaseFirestore, FirebaseUser and ProgressDialog
    RecyclerView recyclerView;
    ArrayList<ParkingModel> parkingModelArrayList;
    OwnedParkingAdapter myAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_list);

        // Initializing and setting properties for ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        // Initializing and setting properties for recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initializing FirebaseFirestore and FirebaseUser
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        parkingModelArrayList = new ArrayList<ParkingModel>();
        myAdapter = new OwnedParkingAdapter(ParkingList.this, parkingModelArrayList);

        // Setting adapter for recycler view
        recyclerView.setAdapter(myAdapter);

        // calling method to listen for changes in the Firestore collection
        EventChangeListener();

        // code to go back to the previous screen goes here
        MaterialButton backBtn = (MaterialButton) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code to go back to the previous screen goes here
                startActivity(new Intent(ParkingList.this, Profile.class));
            }
        });
    }

    private void EventChangeListener() {
        // setting up a listener to listen for changes in the "Parkings" collection where the ownerId is equal to the current user's ID
        firebaseFirestore.collection("Parkings").whereEqualTo("ownerId", firebaseUser.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        // loop through the changes in the collection
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED || dc.getType() == DocumentChange.Type.MODIFIED) {
                                // add the new or modified parking to the arraylist and set the parkingId
                                parkingModelArrayList.add(dc.getDocument().toObject(ParkingModel.class).setParkingId(dc.getDocument().getId()));
                            }
                            // notify the adapter of the changes
                            myAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }
}