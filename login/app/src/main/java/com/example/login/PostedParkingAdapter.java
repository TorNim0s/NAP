package com.example.login;

import android.content.Intent;
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

/**
 * The PostedParkingAdapter class is an adapter for a RecyclerView, which is used to display a list of ActiveParking objects.
 * The adapter is passed a context and a list of ActiveParking objects when it is created.
 * The adapter uses the FirebaseFirestore to retrieve additional information from the
 * "Parkings" and "User" collections to display in the RecyclerView.
 * In the onBindViewHolder method, it sets the values of the TextViews in the layout for each item
 * of the RecyclerView using data from the ActiveParking objects and the additional information retrieved from the Firestore.
 * The getItemCount method returns the number of items in the list of ActiveParking objects.
 * The MyViewHolder class is a nested static class within the PostedParkingAdapter class,
 * which holds the references to the TextViews in the layout.
 */
public class PostedParkingAdapter extends RecyclerView.Adapter<PostedParkingAdapter.MyViewHolder> {

    FirebaseFirestore firebaseFirestore;
    Context context;
    ArrayList<ActiveParking> list;

    // constructor of the adapter that accepts context and arraylist
    public PostedParkingAdapter(Context context, ArrayList<ActiveParking> list) {
        this.context = context;
        this.list = list;
    }

    // creating view holder for the adapter
    @NonNull
    @Override
    public PostedParkingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.posted_parking, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostedParkingAdapter.MyViewHolder holder, int position) {
        ActiveParking activeParking = list.get(position);

        // create instance of firebase firestore
        firebaseFirestore = FirebaseFirestore.getInstance();
        // get the parking id
        String parkingId = activeParking.parkingId;
        // create reference to the parking document
        DocumentReference parkingRef = firebaseFirestore.collection("Parkings").document(parkingId);
        holder.parkingId.setText(parkingId);
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
                        // set the address to the view holder
                        holder.parkingAddress.setText(address);

                        String userId = docPark.getString("ownerId");
                        DocumentReference userRef = firebaseFirestore.collection("User").document(userId);
                        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                if (task1.isSuccessful()) {
                                    DocumentSnapshot docUser = task1.getResult();
                                    if (docUser.exists()) {
                                        // set the owner name to the view holder
                                        holder.owner.setText(docUser.getString("name"));
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

        holder.availableHoursFrom.setText(activeParking.getStartDataAsString());
        holder.availableHoursTo.setText(activeParking.getEndDataAsString());
        holder.price.setText(activeParking.price);
    }

    //returns the size of the list that the adapter is using.
    @Override
    public int getItemCount() {
        return list.size();
    }

    //inner static class called MyViewHolder which is used to hold the inflated views of the recycler view items.
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        //initializes the variables owner, parkingAddress, availableHoursFrom, availableHoursTo, price, parkingId
        //by finding the views by their respective ids.
        TextView owner, parkingAddress, availableHoursFrom, availableHoursTo, price, parkingId;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            owner = itemView.findViewById(R.id.owner);
            parkingAddress = itemView.findViewById(R.id.address);
            availableHoursFrom = itemView.findViewById(R.id.availableHoursFrom);
            availableHoursTo = itemView.findViewById(R.id.availableHoursTo);
            price = itemView.findViewById(R.id.price);
            parkingId = itemView.findViewById(R.id.parkingId);

            //set an onClickListener on the view with id moreInfo where on click it opens the
            //RentParking class and passing the values of the views as extras.
            itemView.findViewById(R.id.moreInfo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), RentParking.class);
                    intent.putExtra("Owner", owner.getText());
                    intent.putExtra("Address", parkingAddress.getText());
                    intent.putExtra("AvailableFrom", availableHoursFrom.getText());
                    intent.putExtra("AvailableTo", availableHoursTo.getText());
                    intent.putExtra("Price", price.getText());
                    intent.putExtra("parkingId", parkingId.getText());
                    view.getContext().startActivity(intent);

                }
            });
        }
    }

}
