package com.example.login.Presenter;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.login.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);

        EditText name = (EditText) findViewById(R.id.edit_fullname);
        EditText phone = (EditText) findViewById(R.id.edit_phone);

        Bundle bundle = getIntent().getExtras();
        name.setText(bundle.getString("Name"));
        phone.setText(bundle.getString("Phone"));

        MaterialButton backButton = (MaterialButton) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code to go back to the previous screen goes here
                startActivity(new Intent(EditProfile.this, Profile.class));
            }
        });

        MaterialButton Save = (MaterialButton) findViewById(R.id.saveBtn);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(EditProfile.this, Profile.class));
                DocumentReference docRef = firebaseFirestore.collection("User").document(firebaseAuth.getUid());
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            Map<String, Object> updates = new HashMap<>();
                            updates.put("name", name.getText().toString());
                            updates.put("number", phone.getText().toString());
                            document.getReference().update(updates);
                            Toast.makeText(EditProfile.this, "Edited successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditProfile.this, Profile.class));

                        } else {
                            Toast.makeText(EditProfile.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}