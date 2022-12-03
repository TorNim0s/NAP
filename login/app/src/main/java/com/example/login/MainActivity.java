package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
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
    ArrayList<Park> parkingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        MaterialButton loginBtn = (MaterialButton) findViewById(R.id.login);

        /*

        // Delete loginBtn if already in user

        loginBtn.setVisibility(View.GONE);
        MaterialButton profileBtn = (MaterialButton) findViewById(R.id.ProfileButton);
        profileBtn.setVisibility(View.VISIBLE);


        // Delete Add parking if not logged in

        MaterialButton addParkingBtn = (MaterialButton) findViewById(R.id.AddParkingButton);
        addParkingBtn.setVisibility(View.GONE);

        */
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
                    Park parkItem = dataSnapshot.getValue(Park.class);

                    parkingList.add(parkItem);
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



    }
}