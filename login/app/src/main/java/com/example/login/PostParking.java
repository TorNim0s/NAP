package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Locale;

public class PostParking extends AppCompatActivity {

    Button timeFrom;
    int hourFrom, minuteFrom;

    Button timeUntil;
    int hourUntil, minuteUntil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_parking);

        timeFrom = findViewById(R.id.timeFrom);
        timeUntil = findViewById(R.id.timeUntil);


        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] items = new String[]{"List of parking", "1", "2"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectHour, int selectMinute) {
                if (view.getId() == R.id.timeFrom){
                    hourFrom = selectHour;
                    minuteFrom = selectMinute;
                    timeFrom.setText(String.format(Locale.getDefault(), "%02d:%02d", hourFrom, minuteFrom));
                }
                if (view.getId() == R.id.timeUntil){
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
}