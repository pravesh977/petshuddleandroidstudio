package android.portfolio.petshuddle.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.portfolio.petshuddle.Entity.Event;
import android.portfolio.petshuddle.Entity.Pet;
import android.portfolio.petshuddle.Helper.MySingletonRequestQueue;
import android.portfolio.petshuddle.Helper.StringToCalendarConverterClass;
import android.portfolio.petshuddle.R;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddNewEventScreen extends AppCompatActivity {

    private EditText editTextEventTitle;
    private EditText editTextEventDetails;
    private EditText editTextEventLocation;
    private TextView textViewEventDate;
    private TextView textViewEventTime;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event_screen);

        editTextEventTitle = findViewById(R.id.editTextEventTitle);
        editTextEventDetails = findViewById(R.id.editTextEventDetails);
        editTextEventLocation = findViewById(R.id.editTextEventLocation);
        textViewEventDate = findViewById(R.id.textViewEventDate);
        textViewEventTime = findViewById(R.id.textViewEventTime);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();

        textViewEventDate.setOnClickListener(openDatePicker);
        textViewEventTime.setOnClickListener(openTimePicker);

//        textViewEventDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar eventCalendar = StringToCalendarConverterClass.stringToCalendar(textViewEventDate);
//                openDatePickerForParty(eventCalendar);
//            }
//        });
    }

    public View.OnClickListener openDatePicker = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            String todaysDate = LocalDate.now().toString();
            Log.i("todays date", todaysDate);

            LocalDate localDate = LocalDate.parse(todaysDate, formatter);
            openDatePickerForEvent(localDate);
        }
    };

    public void openDatePickerForEvent(LocalDate givenLocalDate) {

        DatePickerDialog eventDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                AddNewEventScreen.this.textViewEventDate.setText(year + "-" + (month + 1) + "-" + day);
            }
        }, givenLocalDate.getYear(), givenLocalDate.getMonthValue() - 1, givenLocalDate.getDayOfMonth());
        Log.i("thismonth", String.valueOf(givenLocalDate.getMonthValue()));
        eventDateDialog.show();
    }

//    public void openDatePickerForParty(Calendar givenCalendar){
//
//        DatePickerDialog eventDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                AddNewEventScreen.this.textViewEventDate.setText(year + "-" + (month + 1) + "-" + day);
//            }
//        }, givenCalendar.get(Calendar.YEAR), givenCalendar.get(Calendar.MONTH), givenCalendar.get(Calendar.DAY_OF_MONTH));
//        Log.i("thismonth", String.valueOf(givenCalendar.get(Calendar.MONTH)));
//        eventDateDialog.show();
//    }

    public View.OnClickListener openTimePicker = new View.OnClickListener() {

        LocalTime localTime = LocalTime.parse(LocalTime.now().toString());

        @Override
        public void onClick(View view) {

            TimePickerDialog eventTimeDialog = new TimePickerDialog(AddNewEventScreen.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    AddNewEventScreen.this.textViewEventTime.setText(i + ":" + i1 + ":00");
                }
            }, localTime.getHour(), localTime.getMinute(), true);
            eventTimeDialog.show();

        }
    };

    //handles the save event button and saves the new event in the database
    public void saveNewEvent(View view) {
        String title = editTextEventTitle.getText().toString().trim();
        String details = editTextEventDetails.getText().toString().trim();
        String location = editTextEventLocation.getText().toString().trim();
        String date = textViewEventDate.getText().toString().trim();
        String time = textViewEventTime.getText().toString().trim();
        String dateTime = date + " " + time;

//        Event newEvent = new Event(12, title, details, location, dateTime);
//        Log.i("eventcreated", newEvent.getEventDate());
//        Log.i("eventcre", newEvent.getEventDetails());

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
        if (date.isEmpty()) {
            textViewEventDate.setError("Please check date");
            textViewEventDate.requestFocus();
            return;
        }
        if (time.isEmpty()) {
            textViewEventTime.setError("Please check time");
            textViewEventTime.requestFocus();
            return;
        }

        String url = "http://10.0.2.2:8080/api/events";

        JSONObject eventJson = new JSONObject();
        try {
            eventJson.put("eventTitle", title);
            eventJson.put("eventDetails", details);
            eventJson.put("eventLocation", location);
            eventJson.put("eventDate", dateTime);
            eventJson.put("userId", currentUserId);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, eventJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("petsapiheader977", "petsapikey977");
                return headers;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int statusCode = response.statusCode;
//                Log.i("codeis", String.valueOf(statusCode));
                if (statusCode == 201) {
                    finish();
                } else {
                    Toast.makeText(AddNewEventScreen.this, "Adding failed: ", Toast.LENGTH_LONG).show();
                }
                return super.parseNetworkResponse(response);
            }
        };

        MySingletonRequestQueue.getInstance(this).addToRequestQueue(request);
    }


    //returns users to the previous tab view
    public void cancelEditEvent(View view) {
        this.finish();
    }

}