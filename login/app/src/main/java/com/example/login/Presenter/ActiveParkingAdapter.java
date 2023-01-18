/**  The class ActiveParkingAdapter.java is an adapter class that is used to populate a RecyclerView
    with a list of ActiveParking objects.
    The class extends the RecyclerView.Adapter class and overrides its three main methods:
    onCreateViewHolder, onBindViewHolder, and getItemCount.
    The onCreateViewHolder method inflates the layout for each item in the RecyclerView and returns
    an instance of the MyViewHolder class.
    The onBindViewHolder method binds the data from each ActiveParking object to the corresponding
    views in the layout for each item.
    Finally, the getItemCount method returns the size of the list of ActiveParking objects that is
    being displayed in the RecyclerView.
    The class also contains an inner class MyViewHolder which is used to hold the views in the
    layout and to make them accessible in the onBindViewHolder method. */

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This class is responsible for displaying a list of ActiveParking objects in a recyclerview
 * It uses the FirebaseFirestore to get the address of the parking from the firebase.
 */

public class ActiveParkingAdapter extends RecyclerView.Adapter<ActiveParkingAdapter.MyViewHolder>{

    Context context;
    ArrayList<ActiveParking> list;

    /**
     * constructor to initialize the class
     * @param context the context of the activity
     * @param list the list of ActiveParking objects to be displayed
     */
    public ActiveParkingAdapter(Context context, ArrayList<ActiveParking> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ActiveParkingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the active_parking layout to be used as a row in the recyclerview
        View v = LayoutInflater.from(context).inflate(R.layout.active_parking, parent, false);
        // return a new viewholder with the inflated view
        return new ActiveParkingAdapter.MyViewHolder(v);
    }

    /** The onBindViewHolder method is called for each item in the RecyclerView,
        * and it is responsible for updating the contents of the item's view with the data from the model.
        * In this case, the method is setting the values of the TextViews in the active_parking layout
        * to the values of the corresponding fields in the ActiveParking object at the given position in the list.
        * It also uses the FirebaseFirestore to retrieve the address of the parking space and sets it to
        * the parkingAddress TextView */
    @Override
    public void onBindViewHolder(@NonNull ActiveParkingAdapter.MyViewHolder holder, int position) {
        ActiveParking activeParking = list.get(position);

        //Setting the text of the TextViews in the layout to
        //the corresponding values of the ActiveParking object
        holder.parkingAddress.setText(activeParking.address);
        holder.status.setText(activeParking.status);
        holder.availableHoursFrom.setText(activeParking.startTime.toString().substring(0, activeParking.startTime.toString().indexOf(" GMT")));
        holder.availableHoursTo.setText(activeParking.endTime.toString().substring(0, activeParking.endTime.toString().indexOf(" GMT")));
        holder.price.setText(activeParking.price);
        //holder.postedId.setText();

    }

    /**
     * This function is used to get the number of items in the list
     * of active parking that is passed to the adapter.
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // TextViews to display the active parking details in the recycler view
        TextView status, parkingAddress, availableHoursFrom, availableHoursTo, price, postedId;
        FirebaseFirestore firebaseFirestore;
        Boolean delFree;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firebaseFirestore = FirebaseFirestore.getInstance();
            // Initializing the TextViews by finding their respective id's from the layout file
            status = itemView.findViewById(R.id.status);
            parkingAddress = itemView.findViewById(R.id.address);
            availableHoursFrom = itemView.findViewById(R.id.availableHoursFrom);
            availableHoursTo = itemView.findViewById(R.id.availableHoursTo);
            price = itemView.findViewById(R.id.price);
            postedId = itemView.findViewById(R.id.postedId);

            //need to fix: find a way to get the id of the document in postedParking
            //fixes also in rentParking class
            itemView.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = (String) postedId.getText();
                    DocumentReference docRef = firebaseFirestore.collection("PostedParking").document(id);
                    docRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                delFree = document.getString("status").equals("Available");
                            }
                        }
                    });
                    if (delFree){
                        firebaseFirestore.collection("PostedParking").document((String)postedId.getText()).delete();
                        Toast.makeText(view.getContext(), "Posting deleted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(view.getContext(), PostedList.class);
                        view.getContext().startActivity(intent);
                    } else {
                        Toast.makeText(view.getContext(), "Unable to remove, parking is currently being rented", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
