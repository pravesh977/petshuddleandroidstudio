package android.portfolio.petshuddle.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.portfolio.petshuddle.Adapter.EventsAdapter;
import android.portfolio.petshuddle.Adapter.MyPetsAdapter;
import android.portfolio.petshuddle.Entity.Event;
import android.portfolio.petshuddle.Entity.Pet;
import android.portfolio.petshuddle.Helper.MyEventNotificationReceiver;
import android.portfolio.petshuddle.Helper.MySingletonRequestQueue;
import android.portfolio.petshuddle.Helper.StringToDateTimeConverter;
import android.portfolio.petshuddle.R;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SingleEventScreen extends AppCompatActivity {

    private int eventId;
    private TextView textViewEventId;
    private TextView editTextEventTitle;
    private TextView editTextEventDetails;
    private TextView editTextEventLocation;
    private TextView tViewEventDate;
    private TextView tViewEventTime;
    private RecyclerView petsForEventsRecyclerView;
    private MyPetsAdapter myPetsForEventAdapter;
    List<Pet> petsListForEvent = new ArrayList<>();
    private Button joinEventButton;
    private Button shareEventButton;
    private Button notifyEventButton;
    private FloatingActionButton floatingActionJoinEvent;
    private Button startEditPetButton;
    private Button saveEditPetButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event_screen);

        eventId = getIntent().getIntExtra("eventId", -1);
        String eventTitle = getIntent().getStringExtra("eventTitle");
        String eventDetails = getIntent().getStringExtra("eventDetails");
        String eventLocation = getIntent().getStringExtra("eventLocation");
        String eventDateTimeString = getIntent().getStringExtra("eventDateTime");
        String userId = getIntent().getStringExtra("userId");
//        Log.i("eventDateTimeString : ", eventDateTimeString);

        textViewEventId = findViewById(R.id.textViewEventId);
        editTextEventTitle = findViewById(R.id.editTextEventTitle);
        editTextEventDetails = findViewById(R.id.editTextEventDetails);
        editTextEventLocation = findViewById(R.id.editTextEventLocation);
        tViewEventDate = findViewById(R.id.tViewEventDate);
        tViewEventTime = findViewById(R.id.tViewEventTime);
        petsForEventsRecyclerView = findViewById(R.id.petsForEventsRecyclerView);
        joinEventButton = findViewById(R.id.joinEventButton);
        shareEventButton = findViewById(R.id.shareEventButton);
        notifyEventButton = findViewById(R.id.notifyEventButton);
        floatingActionJoinEvent = findViewById(R.id.floatingActionJoinEvent);
        startEditPetButton = findViewById(R.id.startEditPetButton);
        saveEditPetButton = findViewById(R.id.saveEditPetButton);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();

        //setting up a toggle for the joinevent button on the floating action button
        floatingActionJoinEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (joinEventButton.getVisibility() == View.INVISIBLE) {
                    displayButtonsCollection();
                } else {
                    hideButtonsCollection();
                }
            }
        });

        joinEventButton.setOnClickListener(showMyPetChooserDialog);

        //setting event on a button which sends event details to sms or email
        shareEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi, check out this event!" + "\n" + eventTitle + "\n" + "Details: " + eventDetails + "\n" + "Time: " +eventDateTimeString);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

                hideButtonsCollection();
            }
        });

        notifyEventButton.setOnClickListener(setEventNotification);

//        Calendar calendarDateTime = StringToDateTimeConverter.stringToCalendar(eventDateTime);
//        Log.i("calendar object", calendarDateTime.getTime().toString());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        LocalDateTime localDateTime = LocalDateTime.parse(eventDateTimeString, formatter);
//        Log.i("lcdtime: ", localDateTime.toString());
        LocalDate localDateOnly = localDateTime.toLocalDate();
        LocalTime localTimeOnly = localDateTime.toLocalTime();

//        Log.i("ld is: ", localDateOnly.toString());
        Log.i("lt is: ", localTimeOnly.toString());

        textViewEventId.setText(String.valueOf(eventId));
        editTextEventTitle.setText(eventTitle);
        editTextEventDetails.setText(eventDetails);
        editTextEventLocation.setText(eventLocation);
        //tViewEventDate.setText(calendarDateTime.getTime().toString());
//        tViewEventTime.setText(calendarDateTime.get(Calendar.));
        tViewEventDate.setText(localDateOnly.toString());
        tViewEventTime.setText(localTimeOnly.toString() + ":00");

        tViewEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerForEvent(localDateOnly);
            }
        });

        tViewEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker(localTimeOnly);
            }
        });

        //setting up the horizontal recyclerview containing the pets for the event
        petsListForEvent.clear();
        Log.i("started", " app started");
        String url = "http://10.0.2.2:8080/api/events/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url + eventId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray petJsonArray = response.getJSONArray("petsListForEvent");
//                        Log.i("jsonsizeis : ", String.valueOf(petJsonArray.length()));
                    for (int i = 0; i < petJsonArray.length(); i++) {
                        JSONObject reqobject = petJsonArray.getJSONObject(i);
                        int petId = reqobject.getInt("petId");
                        String petName = reqobject.getString("petName");
                        String species = reqobject.getString("species");
                        String sex = reqobject.getString("sex");
                        String breed = reqobject.getString("breed");
                        int age = reqobject.getInt("age");
                        String petDescription = reqobject.getString("petDescription");
                        String userId = reqobject.getString("userId");
                        Pet jsonPet = new Pet(petId, petName, species, sex, breed, age, petDescription, userId);
                        petsListForEvent.add(jsonPet);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                myPetsForEventAdapter = new MyPetsAdapter(petsListForEvent, SingleEventScreen.this);
                petsForEventsRecyclerView.setAdapter(myPetsForEventAdapter);
//                petsForEventsRecyclerView.setLayoutManager(new LinearLayoutManager(SingleEventScreen.this));
                //this layout manager sets the view horizontally

//                LinearLayoutManager layoutManager = new LinearLayoutManager(SingleEventScreen.this, LinearLayoutManager.HORIZONTAL, false) {
//                    @Override
//                    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
//                        lp.width = getWidth() / 2;
////                        return super.checkLayoutParams(lp);
//                        return true;
//                    }
//                };
//                petsForEventsRecyclerView.setLayoutManager(layoutManager);

                //same as above function. this changes the child/viewholder's width to half of the xml file's width
                petsForEventsRecyclerView.setLayoutManager(new LinearLayoutManager(SingleEventScreen.this, LinearLayoutManager.HORIZONTAL, false) {
                    @Override
                    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                        lp.width = getWidth() / 2;
//                        return super.checkLayoutParams(lp);
                        return true;
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        MySingletonRequestQueue.getInstance(this).addToRequestQueue(request);

        if(currentUserId.equals(userId)) {
            startEditPetButton.setVisibility(View.VISIBLE);
            saveEditPetButton.setVisibility(View.VISIBLE);
        } else {
            startEditPetButton.setVisibility(View.INVISIBLE);
            saveEditPetButton.setVisibility(View.INVISIBLE);
        }
    }

    public void openDatePickerForEvent(LocalDate givenLocalDate) {

        DatePickerDialog eventDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                SingleEventScreen.this.tViewEventDate.setText(year + "-" + (month + 1) + "-" + day);
            }
        }, givenLocalDate.getYear(), givenLocalDate.getMonthValue() - 1, givenLocalDate.getDayOfMonth());
        Log.i("thismonth", String.valueOf(givenLocalDate.getMonthValue()));
        eventDateDialog.show();
    }

    public void openTimePicker(LocalTime givenTime) {

        TimePickerDialog eventTimeDialog = new TimePickerDialog(SingleEventScreen.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                SingleEventScreen.this.tViewEventTime.setText(i + ":" + i1 + ":00");
            }
        }, givenTime.getHour(), givenTime.getMinute(), true);
        eventTimeDialog.show();
    }

    ;

    //handles the save button and saves the event to the database
    public void handleSaveEventEdit(View view) {

        int eventId = Integer.parseInt(textViewEventId.getText().toString().trim());
        String title = editTextEventTitle.getText().toString().trim();
        String details = editTextEventDetails.getText().toString().trim();
        String location = editTextEventLocation.getText().toString().trim();
        String date = tViewEventDate.getText().toString().trim();
        String time = tViewEventTime.getText().toString().trim();
        String dateTime = date + " " + time;

        if (title.isEmpty()) {
            editTextEventTitle.setError("Title can't be empty");
            editTextEventTitle.requestFocus();
            return;
        }
        if (details.isEmpty()) {
            editTextEventDetails.setError("Details can't be empty");
            editTextEventDetails.requestFocus();
            return;
        }
        if (location.isEmpty()) {
            editTextEventLocation.setError("Location can't be empty");
            editTextEventLocation.requestFocus();
            return;
        }
//        if(date.isEmpty()) {
//            editTextEventDate.setError("Date can't be empty");
//            editTextEventDate.requestFocus();
//            return;
//        }
        // Instantiate the RequestQueue.
        String url = "http://10.0.2.2:8080/api/events/";

        JSONObject petJson = new JSONObject();
        try {
            petJson.put("eventTitle", title);
            petJson.put("eventDetails", details);
            petJson.put("eventLocation", location);
            petJson.put("eventDate", dateTime);
            petJson.put("userId", getIntent().getStringExtra("userId"));
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url + eventId, petJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.i("Updated object is : ", response.toString());
//                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(SingleEventScreen.this, "Something went wrong: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int statusCode = response.statusCode;
//                Log.i("statuscode", String.valueOf(statusCode));
                if (statusCode == 200) {
                    finish();
                } else {
//                    Log.i("failedaddpet", "cant add this");
                    Toast.makeText(SingleEventScreen.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                return super.parseNetworkResponse(response);
            }
        };

        MySingletonRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    //handles the edit button which enables edit features on the fields
    public void handleStartEditEventPressed(View view) {

        editTextEventTitle.setClickable(true);
        editTextEventTitle.setFocusable(true);
        editTextEventTitle.setFocusableInTouchMode(true);
        editTextEventTitle.setCursorVisible(true);

        //requesting focus on the title EditText Field and opening the keyboard
        editTextEventTitle.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextEventTitle, InputMethodManager.SHOW_IMPLICIT);

        editTextEventDetails.setClickable(true);
        editTextEventDetails.setFocusable(true);
        editTextEventDetails.setFocusableInTouchMode(true);
        editTextEventDetails.setCursorVisible(true);

        editTextEventLocation.setClickable(true);
        editTextEventLocation.setFocusable(true);
        editTextEventLocation.setFocusableInTouchMode(true);
        editTextEventLocation.setCursorVisible(true);

//        editTextEventDate.setClickable(true);
//        editTextEventDate.setFocusable(true);
//        editTextEventDate.setFocusableInTouchMode(true);
//        editTextEventDate.setCursorVisible(true);
    }

    public View.OnClickListener showMyPetChooserDialog = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Dialog myPetChooserDialog = new Dialog(SingleEventScreen.this);
            myPetChooserDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            myPetChooserDialog.setCancelable(true);
            myPetChooserDialog.setContentView(R.layout.dialog_pet_for_event);

            //get views from the layout. it has to be done here and not on the class initializer because it belongs
            //to another layout
            Button choosePetButton = myPetChooserDialog.findViewById(R.id.choosePetButton);
            Button cancelChoosePetButton = myPetChooserDialog.findViewById(R.id.cancelChoosePetButton);
            Spinner spinnerMyPets = myPetChooserDialog.findViewById(R.id.spinnerMyPets);

            List<Pet> myPetsList = new ArrayList<>();

            String url = "http://10.0.2.2:8080/api/petshuddle/userid/";

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url + currentUser.getUid(), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject reqobject = response.getJSONObject(i);
                            int petId = reqobject.getInt("petId");
                            String petName = reqobject.getString("petName");
                            String species = reqobject.getString("species");
                            String sex = reqobject.getString("sex");
                            String breed = reqobject.getString("breed");
                            int age = reqobject.getInt("age");
                            String petDescription = reqobject.getString("petDescription");
                            String userId = reqobject.getString("userId");
                            Pet jsonPet = new Pet(petId, petName, species, sex, breed, age, petDescription, userId);
                            myPetsList.add(jsonPet);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ArrayAdapter<Pet> myPetsAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_text_file, myPetsList);
                    myPetsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMyPets.setAdapter(myPetsAdapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            MySingletonRequestQueue.getInstance(SingleEventScreen.this).addToRequestQueue(request);

            //handles the cancel button of the dialog and closes it
            cancelChoosePetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myPetChooserDialog.dismiss();
                }
            });

            //handles the choosepet button and saves it to the current event
            choosePetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Pet selectedPet = (Pet) spinnerMyPets.getSelectedItem();
                    int position = selectedPet.getPetId();
                    addPetToEvent(selectedPet);
                    myPetChooserDialog.dismiss();
                }
            });
            myPetChooserDialog.show();
            hideButtonsCollection();
        }
    };

//    private View.OnClickListener handleShareEvent = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Intent sendIntent = new Intent();
//            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//            sendIntent.setType("text/plain");
//
//            Intent shareIntent = Intent.createChooser(sendIntent, null);
//            startActivity(shareIntent);
//        }
//    };

    //gets adds my pet to an event and sends it ot the database
    public void addPetToEvent(Pet myPet) {
        // Instantiate the RequestQueue.
        String url = "http://10.0.2.2:8080/api/events/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url + eventId + "/pet/" + myPet.getPetId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.i("Updated object is : ", response.toString());
//                finish();
                //this is for the animation, inserts new object at the front of the list and scrolls to it
                petsListForEvent.add(0, myPet);
                myPetsForEventAdapter.notifyItemInserted(0);
                petsForEventsRecyclerView.scrollToPosition(0);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(SingleEventScreen.this, "Something went wrong: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int statusCode = response.statusCode;
                if (statusCode == 200) {
//                    finish();
                } else {
                    Toast.makeText(SingleEventScreen.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                return super.parseNetworkResponse(response);
            }
        };

        MySingletonRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    private View.OnClickListener setEventNotification = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(SingleEventScreen.this, MyEventNotificationReceiver.class);
            intent.putExtra("eventTitle", editTextEventTitle.getText().toString());
            intent.putExtra("eventDetails", editTextEventDetails.getText().toString());

//            Log.i("sender title ", editTextEventTitle.getText().toString());
//            Log.i("sender details ", editTextEventDetails.getText().toString());

            String eventDateTimeString = getIntent().getStringExtra("eventDateTime");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            LocalDateTime localDateTime = LocalDateTime.parse(eventDateTimeString, formatter);
            //Long milliseconds = localDateTime.toEpochSecond()

            ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
            long millis = zdt.toInstant().toEpochMilli();
            long beforeThreeHours = millis - 10800000;
//            Log.i("millis ", String.valueOf(millis));
//            Log.i("3 hours before ", String.valueOf(millis - 10800000));
//            Log.i("zdt ", zdt.toString());

            PendingIntent sender = PendingIntent.getBroadcast(SingleEventScreen.this, 0, intent, 0);
            AlarmManager eventAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            eventAlarmManager.set(AlarmManager.RTC_WAKEUP, beforeThreeHours, sender);
//            Log.i("myzone ", ZoneId.systemDefault().toString());
//            Log.i("mytime ", ZonedDateTime.now().toString());
//            Log.i("localdatetime ", LocalDateTime.now().toString());
//            Log.i("utc ", Instant.now().toString());
            hideButtonsCollection();
            Snackbar.make(petsForEventsRecyclerView, "You will be notified 3 hours before the event begins", Snackbar.LENGTH_LONG).show();
        }
    };

    public void displayButtonsCollection() {
        joinEventButton.setVisibility(View.VISIBLE);
        joinEventButton.animate().alpha(1f).setDuration(500);
        shareEventButton.setVisibility(View.VISIBLE);
        shareEventButton.animate().alpha(1f).setDuration(500);
        notifyEventButton.setVisibility(View.VISIBLE);
        notifyEventButton.animate().alpha(1f).setDuration(500);
    }

    public void hideButtonsCollection() {
        joinEventButton.animate().alpha(0f).setDuration(500);
        joinEventButton.setVisibility(View.INVISIBLE);
        shareEventButton.animate().alpha(0f).setDuration(500);
        shareEventButton.setVisibility(View.INVISIBLE);
        notifyEventButton.animate().alpha(0f).setDuration(500);
        notifyEventButton.setVisibility(View.INVISIBLE);
        //notifyEventButton.animate().alpha(0.0f).setDuration(2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //handles the cancel button and returns to main tabbed screen
    public void handleCancelEditEvent(View view) {
        this.finish();
    }
}