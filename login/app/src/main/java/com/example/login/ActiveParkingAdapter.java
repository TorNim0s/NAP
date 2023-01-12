package com.example.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ActiveParkingAdapter extends RecyclerView.Adapter<ActiveParkingAdapter.MyViewHolder>{

    FirebaseFirestore firebaseFirestore;
    Context context;
    ArrayList<ActiveParking> list;

    public ActiveParkingAdapter(Context context, ArrayList<ActiveParking> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ActiveParkingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.active_parking, parent, false);
        return new ActiveParkingAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveParkingAdapter.MyViewHolder holder, int position) {
        ActiveParking activeParking = list.get(position);
        firebaseFirestore = FirebaseFirestore.getInstance();

        String parkingId = activeParking.parkingId;
        DocumentReference parkingRef = firebaseFirestore.collection("Parkings").document(parkingId);
        parkingRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // The document exists
                    DocumentSnapshot docPark = task.getResult();
                    if (docPark.exists()) {
                        String city = docPark.getString("city");
                        String street = docPark.getString("street");
                        String homeNum = String.valueOf(docPark.get("homeNum"));
                        String parkingNum = String.valueOf(docPark.get("parkingNum"));
                        String address = city + ", " + street + " " + homeNum + ", " + parkingNum;
                        holder.parkingAddress.setText(address);
                    }
                }
            }
        });
        holder.status.setText(activeParking.status);
        holder.availableHoursFrom.setText(activeParking.getStartDataAsString());
        holder.availableHoursTo.setText(activeParking.getEndDataAsString());
        holder.price.setText(activeParking.price);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView status, parkingAddress, availableHoursFrom, availableHoursTo, price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            parkingAddress = itemView.findViewById(R.id.address);
            availableHoursFrom = itemView.findViewById(R.id.availableHoursFrom);
            availableHoursTo = itemView.findViewById(R.id.availableHoursTo);
            price = itemView.findViewById(R.id.price);

        }
    }
}
