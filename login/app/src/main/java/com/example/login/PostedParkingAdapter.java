package com.example.login;

import android.content.Intent;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostedParkingAdapter extends RecyclerView.Adapter<PostedParkingAdapter.MyViewHolder> {

    Context context;
    ArrayList<PostedParking> list;

    public PostedParkingAdapter(Context context, ArrayList<PostedParking> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PostedParkingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.posted_parking,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostedParkingAdapter.MyViewHolder holder, int position) {
        PostedParking postedParking = list.get(position);
        holder.parkOwnerFirstName.setText(postedParking.parkingFirstName);
        holder.parkingAddress.setText(postedParking.parkingAddress);
        holder.availableHours.setText(postedParking.availableHours);
        holder.Price.setText(postedParking.Price);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView parkOwnerFirstName, parkingAddress, availableHours, Price;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            parkOwnerFirstName = itemView.findViewById(R.id.owner);
            parkingAddress = itemView.findViewById(R.id.address);
            availableHours = itemView.findViewById(R.id.availableHours);
            Price = itemView.findViewById(R.id.price);

            itemView.findViewById(R.id.moreInfo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), RentParking.class);
                    intent.putExtra("Owner", parkOwnerFirstName.getText());
                    intent.putExtra("Address", parkingAddress.getText());
                    intent.putExtra("Hours", availableHours.getText());
                    intent.putExtra("Price", Price.getText());
                    view.getContext().startActivity(intent);

                }
            });
        }
    }

}
