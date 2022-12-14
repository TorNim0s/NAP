package com.example.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class PostParking extends AppCompatActivity {

    Button timeFrom;
    int hourFrom, minuteFrom;

    Button timeUntil;
    int hourUntil, minuteUntil;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    ArrayList<String> parkingList;
    ArrayAdapter<String> adapter;
    HashMap<String, String> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_parking);

        timeFrom = findViewById(R.id.timeFrom);
        timeUntil = findViewById(R.id.timeUntil);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        parkingList = new ArrayList<>();
        map = new HashMap<>();

        EventChangeListener();

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, parkingList);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        EditText cost = (EditText) findViewById(R.id.Cost);
        MaterialButton postBtn = (MaterialButton) findViewById(R.id.postbtn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String availableHours = timeFrom.getText().toString() + " - " + timeUntil.getText().toString();
                String price = cost.getText().toString() + " Shekels";
                String selectedItem = (String) dropdown.getSelectedItem();
                String selectedParkingId = map.get(selectedItem);

//                CollectionReference usersRef = firebaseFirestore.collection("PostedParking");
//                Query query = usersRef.whereEqualTo("parkingId", selectedParkingId);
//                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (DocumentSnapshot document : task.getResult()) {
//                                if (document.exists()) {
//                                    Toast.makeText(PostParking.this, "ERROR: Parking is already posted", Toast.LENGTH_SHORT).show();
//                                } else {
//
//                                }
//                            }
//                        } else {
//                            Log.d("TAG", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
                ActiveParking activeParking = new ActiveParking(availableHours, price, selectedParkingId, firebaseUser.getUid(), "Available");
                firebaseFirestore.collection("PostedParking").add(activeParking);

                Toast.makeText(PostParking.this, "Parking posted successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PostParking.this, MainActivity.class));
            }
        });

        MaterialButton back = (MaterialButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostParking.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectHour, int selectMinute) {
                if (view.getId() == R.id.timeFrom) {
                    hourFrom = selectHour;
                    minuteFrom = selectMinute;
                    timeFrom.setText(String.format(Locale.getDefault(), "%02d:%02d", hourFrom, minuteFrom));
                }
                if (view.getId() == R.id.timeUntil) {
                    hourUntil = selectHour;
                    minuteUntil = selectMinute;
                    timeUntil.setText(String.format(Locale.getDefault(), "%02d:%02d", hourUntil, minuteUntil));
                }
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = null;
        if (view.getId() == R.id.timeFrom) {
            timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hourFrom, minuteFrom, true);
        }
        if (view.getId() == R.id.timeUntil) {
            timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hourUntil, minuteUntil, true);
        }
        timePickerDialog.show();
    }

    private void EventChangeListener() {
        firebaseFirestore.collection("Parkings").whereEqualTo("ownerId", firebaseUser.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                String city = dc.getDocument().getString("city");
                                String street = dc.getDocument().getString("street");
                                String homeNum = String.valueOf(dc.getDocument().get("homeNum"));
                                String parkingNum = String.valueOf(dc.getDocument().get("parkingNum"));
                                String address = city + ", " + street + " " + homeNum + ", " + parkingNum;
                                map.put(address, dc.getDocument().getId());
                                parkingList.add(address);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}