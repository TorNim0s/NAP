package com.example.login.Presenter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.Model.ActiveParking;
import com.example.login.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * The class PostParking extends AppCompatActivity and is responsible for creating a new activity
 * where the user can post a parking spot for rent.
 * It has two buttons for time selection, one for the start time and one for the end time.
 * It uses FirebaseFirestore to store data, the FirebaseUser to get the current user and ArrayList,
 * ArrayAdapter and HashMap for the parking list.
 * It has a spinner to select the parking spot and an EditText for cost input.
 * It has a button to post the parking spot and on click,
 * it gets the selected parking spot, cost, start and end time and stores it in the firebase.
 * It has an event listener that listens for changes in the firebase and updates the parking spot list accordingly.
 * It also has some error handling for when the parking spot is already posted by the user.
 */
public class PostParking extends AppCompatActivity {

    //Button variables to hold the timeFrom and timeUntil buttons
    Button timeFrom;
    int hourFrom, minuteFrom;

    Button timeUntil;
    int hourUntil, minuteUntil;

    //Firebase variables to hold the firebaseFirestore and firebaseUser instances
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    //ArrayList and ArrayAdapter to hold the parking list data and map to store the parking id and name
    ArrayList<String> parkingList;
    ArrayAdapter<String> adapter;
    HashMap<String, String> map;

    //Calendar instances to hold the selected from and to time
    final Calendar FromCalender = Calendar.getInstance();
    final Calendar ToCalender = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_parking);

        //initialize timeFrom button
        timeFrom = findViewById(R.id.timeFrom);
        //initialize timeUntil button
        timeUntil = findViewById(R.id.timeTo);
        //initialize Firebase Firestore
        firebaseFirestore = FirebaseFirestore.getInstance();
        //initialize Firebase User
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //initialize parkingList ArrayList
        parkingList = new ArrayList<>();
        //initialize HashMap for storing parking id and name
        map = new HashMap<>();

        //call EventChangeListener
        EventChangeListener();

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, parkingList);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        // Get the cost input field by its ID
        EditText cost = (EditText) findViewById(R.id.Cost);
        // Get the post button by its ID
        MaterialButton postBtn = (MaterialButton) findViewById(R.id.postbtn);
        // Set an on click listener for the post button
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the cost value and append " Shekels" to the end
                String price = cost.getText().toString() + " Shekels";
                // Get the selected parking from the spinner
                String selectedItem = (String) dropdown.getSelectedItem();
                // Get the parking id for the selected parking
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


                // Create a new ActiveParking object with the selected parking information
                ActiveParking activeParking = new ActiveParking(FromCalender.getTimeInMillis(), ToCalender.getTimeInMillis(), price, selectedParkingId, firebaseUser.getUid(), "Available", selectedItem);
                // Add the active parking object to the "PostedParking" collection in Firebase Firestore
                firebaseFirestore.collection("PostedParking").add(activeParking);
                // Show a toast message to confirm that the parking was posted successfully
                Toast.makeText(PostParking.this, "Parking posted successfully", Toast.LENGTH_SHORT).show();
                // Start the MainActivity
                startActivity(new Intent(PostParking.this, MainActivity.class));
            }
        });

        //back button to navigate to the main activity
        MaterialButton back = (MaterialButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create an intent and start the activity
                Intent intent = new Intent(PostParking.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**This function is responsible for creating a TimePickerDialog and a DatePickerDialog, where the user can select
    the desired time and date when the parking spot is available.
    The function is called when the user clicks on the "timeFrom" or "timeUntil" buttons */
    public void popTimeDatePicker(View view) {

        if (view.getId() == R.id.timeFrom) {
            //If the view is the "timeFrom" button,
            //the TimePickerDialog and DatePickerDialog will be created for the FromCalender variable
            popTimeDataPickerPerCalender(FromCalender, timeFrom);
        } else {
            //If the view is the "timeUntil" button,
            //the TimePickerDialog and DatePickerDialog will be created for the ToCalender variable
            popTimeDataPickerPerCalender(ToCalender, timeUntil);
        }


//        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int selectHour, int selectMinute) {
//                if (view.getId() == R.id.timeFrom) {
//                    hourFrom = selectHour;
//                    minuteFrom = selectMinute;
//                    timeFrom.setText(String.format(Locale.getDefault(), "%02d:%02d", hourFrom, minuteFrom));
//                }
//                if (view.getId() == R.id.timeUntil) {
//                    hourUntil = selectHour;
//                    minuteUntil = selectMinute;
//                    timeUntil.setText(String.format(Locale.getDefault(), "%02d:%02d", hourUntil, minuteUntil));
//                }
//            }
//        };
//
//        int style = AlertDialog.THEME_HOLO_DARK;
//        TimePickerDialog timePickerDialog = null;
//        if (view.getId() == R.id.timeFrom) {
//            timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hourFrom, minuteFrom, true);
//        }
//        if (view.getId() == R.id.timeUntil) {
//            timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hourUntil, minuteUntil, true);
//        }
//        timePickerDialog.show();
    }

    private void popTimeDataPickerPerCalender(Calendar c, TextView t){
        // Creates a new DatePickerDialog, with the current date as the default value
        new DatePickerDialog(PostParking.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                // Set the date in the calender object
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // Creates a new TimePickerDialog, with the current time as the default value
                new TimePickerDialog(PostParking.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        // Set the time in the calender object
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        // Set the selected date and time in the text view
                        t.setText(year + "-" + (month + 1) + "-" + dayOfMonth + " " + hourOfDay + ":" + minute);
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
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
                                // Get parking address data from the document
                                String city = dc.getDocument().getString("city");
                                String street = dc.getDocument().getString("street");
                                String homeNum = String.valueOf(dc.getDocument().get("homeNum"));
                                String parkingNum = String.valueOf(dc.getDocument().get("parkingNum"));
                                String address = city + ", " + street + " " + homeNum + ", " + parkingNum;
                                // Adding the parking address and its id to the map
                                map.put(address, dc.getDocument().getId());
                                // Adding the parking address to the list
                                parkingList.add(address);
                            }
                        }
                        // Notifying the adapter that the data set has changed
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}