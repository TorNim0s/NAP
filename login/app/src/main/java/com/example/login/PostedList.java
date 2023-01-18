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
 * The class "PostedList" is an Android activity that displays a list of parking spots that a user has posted.
 * It uses a RecyclerView to display the parking spots in a list format.
 * The class uses Firebase Firestore to retrieve the data of the parking spots that are posted by the current user.
 * It uses a ProgressDialog to display a loading message while the data is being fetched.
 * The class also has a MaterialButton that when clicked, takes the user back to the previous screen.
 * The class uses the "ActiveParking" model to store the data of each parking spot
 * and an "ActiveParkingAdapter" class to populate the RecyclerView with the parking spot data.
 */
public class PostedList extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ActiveParking> parkingModelArrayList;
    ActiveParkingAdapter myAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_list);

        // Initialize progress dialog and set message and cancelable
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        // Initialize recycler view and set its properties
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize FirebaseFirestore and FirebaseUser instances
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        parkingModelArrayList = new ArrayList<ActiveParking>();
        myAdapter = new ActiveParkingAdapter(PostedList.this, parkingModelArrayList);

        recyclerView.setAdapter(myAdapter);

        // Call the EventChangeListener method to listen for changes in the database
        EventChangeListener();

        // Initialize the back button and set an onClickListener to go back to the previous screen
        MaterialButton backBtn = (MaterialButton) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostedList.this, Profile.class));
            }
        });
    }

    private void EventChangeListener() {
        // Query the "PostedParking" collection where the "ownerId" field is equal to the current user's ID
        firebaseFirestore.collection("PostedParking").whereEqualTo("ownerId", firebaseUser.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        // Check for errors and dismiss progress dialog if there is an error
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
                        // Iterate through the document changes
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            // Check if the change is an added document
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                // Convert the document to an ActiveParking object and add it to the array list
                                ActiveParking activeParking = dc.getDocument().toObject(ActiveParking.class);
                                String postedId = dc.getDocument().getId();
                                parkingModelArrayList.add(activeParking);
                            }

                            myAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }
}