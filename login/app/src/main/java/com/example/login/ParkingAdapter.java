package com.example.login;

import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.MyViewHolder> {

    Context context;

    ArrayList<Park> list;

    public ParkingAdapter(Context context, ArrayList<Park> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.parking_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Park park = list.get(position);
        holder.parkOwnerFirstName.setText(park.parkingFirstName);
        holder.parkingAddress.setText(park.parkingAddress);
        holder.availableHours.setText(park.availableHours);
        holder.Price.setText(park.Price);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView parkOwnerFirstName, parkingAddress, availableHours, Price;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            parkOwnerFirstName = itemView.findViewById(R.id.parkingFirstName);
            parkingAddress = itemView.findViewById(R.id.parkingAddress);
            availableHours = itemView.findViewById(R.id.parkingAvailableHours);
            Price = itemView.findViewById(R.id.parkingPrice);

            itemView.findViewById(R.id.parkingMoreInfo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), RentParking.class);
                    intent.putExtra("Owner", parkOwnerFirstName.getText());
                    intent.putExtra("Address", parkingAddress.getText());
                    intent.putExtra("Hours", availableHours.getText());
                    intent.putExtra("Price", Price.getText());
                    view.getContext().startActivity(intent);



//                    Log.d("demo", "onClick: " + parkOwnerFirstName.getText() +
//                            " - " + parkingAddress.getText() + " - " + availableHours.getText() +
//                            " - " + Price.getText());
                }
            });
        }
    }

}
