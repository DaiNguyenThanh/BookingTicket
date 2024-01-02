package com.example.bookingticket.Activities;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingticket.Activities.MainActivity;
import com.example.bookingticket.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*This class purpose is to build the seats viewGroup and for booking the seats.*/
public class SeatActivity extends AppCompatActivity implements View.OnClickListener {

    ViewGroup groupLayout;
    List<Integer> seatArray=new ArrayList<>();
    Button bookTicketsBttn;
    boolean Success = true;
    static String bookingError = "";
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    /*example of seats string to define how the auditorium looks like. We will adjust it properly later.*/
    String seats = "_UUUUUAAAUU_/"
            + "__________/"
            + "UU_AAAUUU_UU/"
            + "UU_UUAAAA_AA/"
            + "AA_AAAAAA_AA/"
            + "AA_AAUUUU_AA/"
            + "UU_UUUUUU_AA/"
            + "__________/";

    List<TextView> seatViewList = new ArrayList<>();
    List<Integer> reservedSeats = new ArrayList<>();
     private Intent intent = getIntent();

    int seatSize = 80, seatGaping = 10, chosenSeats = 0, count = 0;
    int STATUS_AVAILABLE = 1, STATUS_BOOKED = 2, audiID=0, movieID=0, screening_id;
    private String time = "", selectedIds = "", costumerName = "", costumerEmail = "", costumerPhone = "", movieTitle = "";

    /*When creating the instance of this class, we get the intent from the purchaseActivity with all
     * the extra data. This data will be saved on the DB tables after a successful booking.*/
    @SuppressLint({"MissingInflatedId"})
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seat_activity);
        
         Intent intent = getIntent();

        audiID = intent.getIntExtra("audiID", 0);
        movieID = intent.getIntExtra("id", 0);
        time = intent.getStringExtra("time");
        costumerName = intent.getStringExtra("name");
        costumerEmail = intent.getStringExtra("email");
        costumerPhone = intent.getStringExtra("number");
        movieTitle = intent.getStringExtra("title");
        chosenSeats = intent.getIntExtra("number of seats", 0);

        ImageView Back=findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Select seats");
        bookTicketsBttn = findViewById(R.id.bookTicketsBttn);
    }

    @Override
    protected void onStart() {
        super.onStart();

        groupLayout = findViewById(R.id.layoutSeat);
        seats = "/" + seats;

        LinearLayout layoutSeat = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.VERTICAL);
        layoutSeat.setLayoutParams(params);
        layoutSeat.setPadding(5 * seatGaping, 5 * seatGaping, 5 * seatGaping, 5 * seatGaping);
        groupLayout.addView(layoutSeat);

        LinearLayout layout = null;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        buildSeats(reservedSeats);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /*For loop to build the seats groupView from the seats string which contains how the view of the seats
         * looks like, and set the margins, the id's, colors, etc..*/
        for (int index = 0; index < seats.length(); index++) {
            if (seats.charAt(index) == '/') {
                layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layoutSeat.addView(layout);
            } else if (seats.charAt(index) == 'U') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_booked);
                view.setTextColor(Color.WHITE);
                view.setTag(STATUS_BOOKED);
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) == 'A') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_book);
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                view.setTextColor(Color.BLACK);
                view.setTag(STATUS_AVAILABLE);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) == '_') {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.TRANSPARENT);
                view.setText("");
                layout.addView(view);
            }
        }

        bookTicketsBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chosenSeats <= 0){
                    Toast.makeText(SeatActivity.this,"More seats have been selected!!", Toast.LENGTH_LONG).show();
                }
//                else if(chosenSeats > 0){
//                    Toast.makeText(SeatActivity.this,"Fewer seats have been selected!!", Toast.LENGTH_LONG).show();
//                }
                else {
                    //String[] seatsToBook = selectedIds.split(",");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Intent intent1 = new Intent(SeatActivity.this, TicketActivity.class);
                    Intent intent = getIntent();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SeatActivity.this);
                    String userId = preferences.getString("userUid", "DEFAULT_USER_ID");

// Create the ticket data map
                    Map<String, Object> ticketData = new HashMap<>();
                    ticketData.put("film", intent.getStringExtra("title"));
                    ticketData.put("cinema", intent.getStringExtra("cinema"));
                    ticketData.put("seatArray", seatArray); // Add the entire seatArray
                    ticketData.put("userId", userId);
                    ticketData.put("time", intent.getStringExtra("time"));
                    ticketData.put("date", intent.getStringExtra("date"));
                    ticketData.put("image", intent.getStringExtra("image"));
                    ticketData.put("showing", intent.getStringExtra("showingId"));
                    ticketData.put("price", 100000*seatArray.size());


// Add the document to the "tickets" collection
                    db.collection("tickets")
                            .add(ticketData)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    // Document added successfully
                                    intent1.putExtra("ticketId", documentReference.getId());
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle errors
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });

                        /*After failing to book the tickets and displaying the error message
                         * We finish this activity and back to the main activity.*/

                    intent1.putExtra("seat",seatArray.toString());
                    intent1.putExtra("cinema", intent.getStringExtra("cinema"));
                    intent1.putExtra("time", intent.getStringExtra("time"));
                    intent1.putExtra("date", intent.getStringExtra("date"));
                    intent1.putExtra("title", intent.getStringExtra("title"));
                    intent1.putExtra("image", intent.getStringExtra("image"));

                        startActivity(intent1);
                        finish();

                }
            }
        });
    }

    /*---------------------------------------------------------------------------------------------------------------*/
    /*This method is called when a seat from the seat viewGroup is clicked.
     * It will display a message when a booked seat is clicked.
     * If available seat is clicked, it will add it to the selectedIds String and change its color to selected seat color.
     * If selected seat is clicked, it will remove it from the selectedIds String and change its color to empty seat.*/
    @Override
    public void onClick(View view) {
        if ((int) view.getTag() == STATUS_AVAILABLE) {
            if (selectedIds.contains(view.getId() + ",")) {
                selectedIds = selectedIds.replace(+view.getId() + ",", "");
                view.setBackgroundResource(R.drawable.ic_seats_book);
                chosenSeats--;
               for (int i=0;i<seatArray.size();i++){
                   if(seatArray.get(i)==view.getId()){
                       seatArray.remove(i);
                       break;
                   }
               }
            } else {
                selectedIds = selectedIds + view.getId() + ",";
                view.setBackgroundResource(R.drawable.ic_seats_selected);
                chosenSeats++;
                seatArray.add(view.getId());

            }
        } else if ((int) view.getTag() == STATUS_BOOKED) {
            Toast.makeText(this, "Seat " + view.getId() + " is Booked", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /*This method iterates on seats String, for each seat it checks if the seat is on reservedSeat list or not,
     * if yes we define the seat as a booked seat, otherwise we define the seat as a available seat.*/
    public void buildSeats(List<Integer> tmp) {
        int fac = audiID == 1 ? 0 : (audiID-1)*60;
        int cnt = 0;

        for(int i = 0; i < seats.length(); i++){
            if(seats.charAt(i) == 'U' || seats.charAt(i) == 'A'){
                cnt++;
                if(tmp.contains(cnt+fac)){
                    seats = seats.substring(0, i) + 'U' + seats.substring(i+1);
                }
                else{
                    seats = seats.substring(0, i) + 'A' + seats.substring(i+1);
                }
            }
        }
    }

    /*This procedure sends a message to the costumer after a successful booking.
     * And displays a message after successful booking and sending message.*/
    public void sendMessage() {
        String error = "";
        String message = "Dear " + costumerName + ",\nWe have booked your seats!." +
                " Please arrive before the screening time in" +
                " order to pay and issue the tickets.\nBest regards, EA6Cinema.";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(costumerPhone, null, message, null, null);
        } catch (Exception e) {
            Toast.makeText(SeatActivity.this, "There is an error" +
                    " messaging you, Please try again later!", Toast.LENGTH_LONG).show();
            error = e.toString();
            e.printStackTrace();
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (error.isEmpty()) {
            Success = true;
            SeatActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    alertDialogBuilder = new AlertDialog.Builder(SeatActivity.this);
                    alertDialogBuilder.setMessage("Hi " + costumerName + ".\nThank you for booking a movie tickets" +
                            ". We look forward to seeing you and your group!:)");
                    alertDialogBuilder.setTitle("Order status");
                    alertDialogBuilder.setIcon(R.drawable.lol);
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
        }
    }
    private void getSeatId(String filmId, List<Integer> seatArray) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("showing")
                .whereEqualTo("filmId", filmId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Assuming "seatBooked" is the array field you want to retrieve
                        List<Long> seatBookedList = (List<Long>) document.get("seatBooked");

                        if (seatBookedList != null) {
                            // Check if any seatId in seatBookedList is present in the seatArray
                            for (Long seatId : seatBookedList) {
                                if (seatArray.contains(seatId.intValue())) {
                                    // SeatId is present in the array
                                    // You can perform further actions here
                                    // For example, you might want to update UI or perform some logic
                                    // based on the seatId
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors while fetching data
                });
    }
    /*This inner class have three modes:
     * mode == 1, To retrieve the appropriate screening id.
     * mode == 2, To retrieve the reserved seats of the specific screening id.
     * mode == 3, To update the reservation and seats_reserved tables after a successful booking.*/



    }

