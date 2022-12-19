package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        TextView logoutText = (TextView) findViewById(R.id.logoutBtn);

        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(Profile.this, MainActivity.class));
            }
        });

        TextView addParking = (TextView) findViewById(R.id.addParking);
        addParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code to go back to the previous screen goes here
                //Toast.makeText(Profile.this, "In progress: add parking", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Profile.this, AddParking.class));
            }
        });

        TextView editProfile = (TextView) findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code to go back to the previous screen goes here
                Toast.makeText(Profile.this, "In progress: edit profile", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(Profile.this, AddParking.class));
            }
        });

        TextView changePassword = (TextView) findViewById(R.id.changePassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code to go back to the previous screen goes here
                Toast.makeText(Profile.this, "In progress: change password", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(Profile.this, AddParking.class));
            }
        });

        ImageView backButton = (ImageView) findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code to go back to the previous screen goes here
                startActivity(new Intent(Profile.this, MainActivity.class));
            }
        });

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference userDocRef = firestore.collection("User").document(user.getUid());

        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // the user document exists, so we can retrieve the data
                        String name = document.getString("name");
                        String email = document.getString("email");
                        String number = document.getString("number");

                        // now we can display the data in the appropriate views
                        TextView nameTextView = findViewById(R.id.name_text_view);
                        nameTextView.setText(name);

                        TextView numberTextView = findViewById(R.id.number_text_view);
                        numberTextView.setText(number);

                        TextView emailTextView = findViewById(R.id.email_text_view);
                        emailTextView.setText(email);
                        // now we can display the data in the appropriate views
                    } else {
                        // the user document doesn't exist, so we should show a Toast message and redirect the user to the login screen
                        Toast.makeText(Profile.this, "User data not found", Toast.LENGTH_SHORT).show();
                        Intent loginIntent = new Intent(Profile.this, SignIn.class);
                        startActivity(loginIntent);
                        finish();
                    }
                } else {
                    // there was an error retrieving the document, so we should show a Toast message
                    Toast.makeText(Profile.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }



}