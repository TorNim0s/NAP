package com.example.login.Presenter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.Model.ParkingModel;
import com.example.login.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This class is an adapter for the RecyclerView that displays a list of parking spots that are owned by the user.
 * It takes in a context and an ArrayList of ParkingModel objects and uses them to populate the
 * RecyclerView with the parking spot details.
 * The class also has an inner MyViewHolder class that defines the layout for each item in the
 * RecyclerView and sets up OnClickListeners for the "Edit" and "Remove" buttons in the layout.
 * The "Edit" button opens up an activity for editing the parking spot details,
 * and the "Remove" button removes the parking spot from the Firebase Firestore collection "Parkings"
 * and sends a toast message to confirm the deletion.
 */
public class OwnedParkingAdapter extends RecyclerView.Adapter<OwnedParkingAdapter.MyViewHolder> {

    Context context;
    ArrayList<ParkingModel> parkingArrayList;

    // constructor to initialize context and parkingArrayList
    public OwnedParkingAdapter(Context context, ArrayList<ParkingModel> parkingArrayList) {
        this.context = context;
        this.parkingArrayList = parkingArrayList;
    }

    // inflates the layout and returns a new view holder
    @NonNull
    @Override
    public OwnedParkingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.owned_parking, parent, false);
        return new MyViewHolder(v);
    }

    // binds the view holder with the data at the specified position
    @Override
    public void onBindViewHolder(@NonNull OwnedParkingAdapter.MyViewHolder holder, int position) {
        ParkingModel parking = parkingArrayList.get(position);
        holder.city.setText(parking.getCity());
        holder.street.setText(parking.getStreet());
        holder.homeNum.setText(String.valueOf(parking.getHomeNum()));
        holder.parkingNum.setText(String.valueOf(parking.getParkingNum()));
        holder.parkingId.setText(String.valueOf(parking.getParkingId()));
    }

    // returns the number of items in the parkingArrayList
    @Override
    public int getItemCount() {
        return parkingArrayList.size();
    }

    // MyViewHolder class to hold the views in the layout
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView city, street, homeNum, parkingNum, parkingId;
        FirebaseFirestore firebaseFirestore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firebaseFirestore = FirebaseFirestore.getInstance();

            // initialize the views
            city = itemView.findViewById(R.id.city);
            street = itemView.findViewById(R.id.street);
            homeNum = itemView.findViewById(R.id.homeNum);
            parkingNum = itemView.findViewById(R.id.parkingNum);
            parkingId = itemView.findViewById(R.id.parkingId);

            // set onClickListener for the edit button
            itemView.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), AddParking.class);
                    intent.putExtra("City", city.getText());
                    intent.putExtra("Street", street.getText());
                    intent.putExtra("homeNum", homeNum.getText());
                    intent.putExtra("parkingNum", parkingNum.getText());
                    intent.putExtra("parkingId", parkingId.getText());
                    view.getContext().startActivity(intent);
                }
            });

            itemView.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // deletes the document in the "Parkings" collection in Firebase Firestore with the id stored in the parkingId TextView
                    firebaseFirestore.collection("Parkings").document((String)parkingId.getText()).delete();
                    // shows a toast message to confirm that the parking was deleted
                    Toast.makeText(view.getContext(), "Parking deleted successfully", Toast.LENGTH_SHORT).show();
                    // creates an Intent to open the ParkingList activity
                    Intent intent = new Intent(view.getContext(), ParkingList.class);
                    // starts the activity
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
