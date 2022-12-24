package com.example.login;

import android.content.Context;
import android.content.Intent;
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

public class RentedParkingAdapter extends RecyclerView.Adapter<RentedParkingAdapter.MyViewHolder>{

    FirebaseFirestore firebaseFirestore;
    Context context;
    ArrayList<PostedParking> list;

    public RentedParkingAdapter(Context context, ArrayList<PostedParking> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RentedParkingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rented_parking,parent,false);
        return new RentedParkingAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RentedParkingAdapter.MyViewHolder holder, int position) {
        PostedParking postedParking = list.get(position);
        firebaseFirestore = FirebaseFirestore.getInstance();

        String parkingId = postedParking.parkingId;
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

                        String userId = docPark.getString("id");
                        DocumentReference userRef = firebaseFirestore.collection("User").document(userId);
                        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                if (task1.isSuccessful()) {
                                    DocumentSnapshot docUser = task1.getResult();
                                    if (docUser.exists()) {
                                        holder.owner.setText(docUser.getString("name"));
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

        holder.availableHours.setText(postedParking.availableHours);
        holder.price.setText(postedParking.price);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView owner, parkingAddress, availableHours, price;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            owner = itemView.findViewById(R.id.owner);
            parkingAddress = itemView.findViewById(R.id.address);
            availableHours = itemView.findViewById(R.id.availableHours);
            price = itemView.findViewById(R.id.price);

        }
    }
}
