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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class PostedParkingAdapter extends RecyclerView.Adapter<PostedParkingAdapter.MyViewHolder> {

    FirebaseFirestore firebaseFirestore;
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

            itemView.findViewById(R.id.moreInfo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), RentParking.class);
                    intent.putExtra("Owner", owner.getText());
                    intent.putExtra("Address", parkingAddress.getText());
                    intent.putExtra("Hours", availableHours.getText());
                    intent.putExtra("Price", price.getText());
                    view.getContext().startActivity(intent);

                }
            });
        }
    }

}
