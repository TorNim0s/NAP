package com.example.login.Presenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.Model.ApiClient;
import com.example.login.Model.ApiInterface;
import com.example.login.Model.RentParkingModel;
import com.example.login.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RentParking extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_parking);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        RentParkingModel rentParkingModel = new RentParkingModel();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        TextView address = (TextView) findViewById(R.id.address);
        TextView owner = (TextView) findViewById(R.id.owner);
        TextView cost = (TextView) findViewById(R.id.cost);
        TextView available = (TextView) findViewById(R.id.available);
        MaterialButton rentBtn = (MaterialButton) findViewById(R.id.rent);

        Bundle bundle = getIntent().getExtras();
        String[] ids = bundle.getString("ids").split(",");
        String postedId = ids[0];
        String parkingId = ids[1];
        String ownerId = ids[2];

        DocumentReference docRef = firebaseFirestore.collection("PostedParking").document(postedId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    address.setText(document.getString("address"));
                    cost.setText("Hourly price: " + document.getString("price"));
                    String from = document.getTimestamp("startTime").toDate().toString().substring(0, document.getTimestamp("startTime").toDate().toString().indexOf(" GMT"));
                    String to = document.getTimestamp("endTime").toDate().toString().substring(0, document.getTimestamp("endTime").toDate().toString().indexOf(" GMT"));
                    available.setText("Available: \nFrom " + from + "\nTo " + to);
                } else {
                    Toast.makeText(RentParking.this, "PostedParking data not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RentParking.this, "Error retrieving PostedParking data", Toast.LENGTH_SHORT).show();
            }
        });
        DocumentReference userRef = firebaseFirestore.collection("User").document(ownerId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        owner.setText("Owned by: " + document.getString("name"));
                    } else {
                        Toast.makeText(RentParking.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RentParking.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseUser == null) {
                    Intent intent = new Intent(RentParking.this, SignIn.class);
                    startActivity(intent);
                    return;
                }

                DocumentReference docRef = firebaseFirestore.collection("PostedParking").document(postedId);
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if (!ownerId.equals(firebaseUser.getUid())) {
                                rentParkingModel.setParkingId(document.getId());
                                rentParkingModel.setRenterId(firebaseUser.getUid());

                                Call<RentParkingModel> call = apiInterface.rentParking(rentParkingModel);
                                call.enqueue(new Callback<RentParkingModel>() {
                                    @Override
                                    public void onResponse(Call<RentParkingModel> call, Response<RentParkingModel> response) {
                                        RentParkingModel data = response.body();
                                        String message = data.getMessage();
                                        String status = data.getStatus();
                                        Toast.makeText(RentParking.this, message, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RentParking.this, MainActivity.class));
                                    }

                                    @Override
                                    public void onFailure(Call<RentParkingModel> call, Throwable t) {
                                        // handle failure
                                        Toast.makeText(RentParking.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RentParking.this, MainActivity.class));
                                    }
                                });

                            } else {
                                Toast.makeText(RentParking.this, "You can't rent your own parking", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RentParking.this, MainActivity.class));
                            }
                        }  else {
                            Toast.makeText(RentParking.this, "Error retrieving parking data", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RentParking.this, MainActivity.class));
                        }
                    }
                });
            }
        });

        MaterialButton back = (MaterialButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RentParking.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}