package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    ParkingAdapter myAdapter;
    ArrayList<PostedParking> parkingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton loginBtn = (MaterialButton) findViewById(R.id.login);
        MaterialButton postParkingBtn = (MaterialButton) findViewById(R.id.PostParkingButton);
        MaterialButton profileBtn = (MaterialButton) findViewById(R.id.ProfileButton);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
//            loginBtn.setVisibility(View.GONE);
            //User is Logged in
            loginBtn.setVisibility(View.GONE); // Delete loginBtn if already in user
            profileBtn.setVisibility(View.VISIBLE);
        }else{
            postParkingBtn.setVisibility(View.GONE);
            //No User is Logged in
        }

        recyclerView = findViewById(R.id.parkingList);
        database = FirebaseDatabase.getInstance().getReference("Parkings");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        parkingList = new ArrayList<>();
        myAdapter = new ParkingAdapter(this,parkingList);
        recyclerView.setAdapter(myAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PostedParking postedParkingItem = dataSnapshot.getValue(PostedParking.class);

                    parkingList.add(postedParkingItem);
                }

                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

}