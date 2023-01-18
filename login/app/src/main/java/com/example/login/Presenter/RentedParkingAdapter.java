package com.example.login.Presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.Model.ActiveParking;
import com.example.login.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        holder.parkingAddress.setText(activeParking.address);
        DocumentReference userRef = firebaseFirestore.collection("User").document(activeParking.ownerId);
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

        holder.availableHoursFrom.setText(activeParking.startTime.toString().substring(0, activeParking.startTime.toString().indexOf(" GMT")));
        holder.availableHoursTo.setText(activeParking.endTime.toString().substring(0, activeParking.endTime.toString().indexOf(" GMT")));
        holder.price.setText(activeParking.price);
        holder.postedId.setText(activeParking.getPostedId());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        FirebaseFirestore firebaseFirestore;
        TextView owner, parkingAddress, availableHoursFrom, availableHoursTo, price, postedId;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firebaseFirestore = FirebaseFirestore.getInstance();

            owner = itemView.findViewById(R.id.owner);
            parkingAddress = itemView.findViewById(R.id.address);
            availableHoursFrom = itemView.findViewById(R.id.availableHoursFrom);
            availableHoursTo = itemView.findViewById(R.id.availableHoursTo);
            price = itemView.findViewById(R.id.price);
            postedId = itemView.findViewById(R.id.postedId);

            itemView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String Id = postedId.getText().toString();
                    DocumentReference docRef = firebaseFirestore.collection("PostedParking").document(Id);
                    docRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("status", "Available");
                                updates.put("renterId", "");
                                document.getReference().update(updates);
                                Toast.makeText(view.getContext(), "Lease canceled successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(view.getContext(), RentedList.class);
                                view.getContext().startActivity(intent);
                            }
                        }
                    });

                }
            });
        }
    }
}
