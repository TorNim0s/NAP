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

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PostedParkingAdapter myAdapter;
    ArrayList<ActiveParking> parkingList;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton loginBtn = (MaterialButton) findViewById(R.id.login);
        MaterialButton postParkingBtn = (MaterialButton) findViewById(R.id.PostParkingButton);
        MaterialButton profileBtn = (MaterialButton) findViewById(R.id.ProfileButton);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //User is Logged in
            loginBtn.setVisibility(View.GONE); // Delete loginBtn if already in user
            profileBtn.setVisibility(View.VISIBLE);
        } else {
            postParkingBtn.setVisibility(View.GONE);
            //No User is Logged in
        }

        recyclerView = findViewById(R.id.parkingList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();

        parkingList = new ArrayList<>();
        myAdapter = new PostedParkingAdapter(this, parkingList);
        recyclerView.setAdapter(myAdapter);

        EventChangeListener();

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

    private void EventChangeListener() {
        firebaseFirestore.collection("PostedParking")
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

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                ActiveParking activeParking = dc.getDocument().toObject(ActiveParking.class);
                                if (activeParking.status.equals("Available")) {
                                    parkingList.add(activeParking);
                                }
                            }

                            myAdapter.notifyDataSetChanged();

                        }
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
    }

}