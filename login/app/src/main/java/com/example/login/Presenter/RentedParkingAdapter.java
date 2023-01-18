package com.example.login.Presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.Model.ActiveParking;
import com.example.login.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RentedParkingAdapter extends RecyclerView.Adapter<RentedParkingAdapter.MyViewHolder> {

    FirebaseFirestore firebaseFirestore;
    Context context;
    ArrayList<ActiveParking> list;

    public RentedParkingAdapter(Context context, ArrayList<ActiveParking> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RentedParkingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rented_parking, parent, false);
        return new RentedParkingAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RentedParkingAdapter.MyViewHolder holder, int position) {
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

                        String userId = docPark.getString("ownerId");
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

        holder.availableHoursFrom.setText(activeParking.getStartTime().toString());
        holder.availableHoursTo.setText(activeParking.getEndTime().toString());
        holder.price.setText(activeParking.price);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView owner, parkingAddress, availableHoursFrom, availableHoursTo, price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            owner = itemView.findViewById(R.id.owner);
            parkingAddress = itemView.findViewById(R.id.address);
            availableHoursFrom = itemView.findViewById(R.id.availableHoursFrom);
            availableHoursTo = itemView.findViewById(R.id.availableHoursTo);
            price = itemView.findViewById(R.id.price);

        }
    }
}
