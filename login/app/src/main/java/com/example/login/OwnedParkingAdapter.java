package com.example.login;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    }

    @Override
    public int getItemCount() {
        return parkingArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView city, street, homeNum, parkingNum;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.city);
            street = itemView.findViewById(R.id.street);
            homeNum = itemView.findViewById(R.id.homeNum);
            parkingNum = itemView.findViewById(R.id.parkingNum);
        }
    }
}
