package com.example.login;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class OwnedParkingAdapter extends RecyclerView.Adapter<OwnedParkingAdapter.MyViewHolder> {

    Context context;
    ArrayList<ParkingModel> parkingArrayList;

    public OwnedParkingAdapter(Context context, ArrayList<ParkingModel> parkingArrayList) {
        this.context = context;
        this.parkingArrayList = parkingArrayList;
    }

    @NonNull
    @Override
    public OwnedParkingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.owned_parking, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnedParkingAdapter.MyViewHolder holder, int position) {
        ParkingModel parking = parkingArrayList.get(position);
        holder.city.setText(parking.getCity());
        holder.street.setText(parking.getStreet());
        holder.homeNum.setText(String.valueOf(parking.getHomeNum()));
        holder.parkingNum.setText(String.valueOf(parking.getParkingNum()));
        holder.parkingId.setText(String.valueOf(parking.getParkingId()));
    }

    @Override
    public int getItemCount() {
        return parkingArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView city, street, homeNum, parkingNum, parkingId;
        FirebaseFirestore firebaseFirestore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firebaseFirestore = FirebaseFirestore.getInstance();

            city = itemView.findViewById(R.id.city);
            street = itemView.findViewById(R.id.street);
            homeNum = itemView.findViewById(R.id.homeNum);
            parkingNum = itemView.findViewById(R.id.parkingNum);
            parkingId = itemView.findViewById(R.id.parkingId);

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
                    firebaseFirestore.collection("Parkings").document((String)parkingId.getText()).delete();
                    Toast.makeText(view.getContext(), "Parking deleted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), ParkingList.class);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
