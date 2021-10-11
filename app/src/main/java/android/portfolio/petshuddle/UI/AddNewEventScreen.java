package android.portfolio.petshuddle.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.portfolio.petshuddle.Entity.Event;
import android.portfolio.petshuddle.Helper.StringToCalendarConverterClass;
import android.portfolio.petshuddle.R;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class AddNewEventScreen extends AppCompatActivity {

    private EditText editTextEventTitle;
    private EditText editTextEventDetails;
    private EditText editTextEventLocation;
    private TextView textViewEventDate;
    private TextView textViewEventTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event_screen);

        editTextEventTitle = findViewById(R.id.editTextEventTitle);
        editTextEventDetails = findViewById(R.id.editTextEventDetails);
        editTextEventLocation = findViewById(R.id.editTextEventLocation);
        textViewEventDate = findViewById(R.id.textViewEventDate);
        textViewEventTime = findViewById(R.id.textViewEventTime);

//        textViewEventDate.setOnClickListener(openDatePicker);
        textViewEventTime.setOnClickListener(openTimePicker);

        textViewEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar eventCalendar = StringToCalendarConverterClass.stringToCalendar(textViewEventDate);
                openDatePickerForParty(eventCalendar);
            }
        });
    }

//    public View.OnClickListener openDatePicker = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
//            String todaysDate = LocalDate.now().toString();
//            Log.i("todays date", todaysDate);
//
//            LocalDate localDate = LocalDate.parse(todaysDate, formatter);
//            openDatePickerForParty(localDate);
//        }
//    };

//    public void openDatePickerForParty(LocalDate givenLocalDT){
//
//        DatePickerDialog eventDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                AddNewEventScreen.this.textViewEventDate.setText(year + "-"+ (month) + "-" + day);
//            }
//        }, givenLocalDT.getYear(), givenLocalDT.getMonthValue(), givenLocalDT.getDayOfMonth());
//        Log.i("thismonth", String.valueOf(givenLocalDT.getMonthValue()));
//        eventDateDialog.show();
//    }

    public void openDatePickerForParty(Calendar givenCalendar){

        DatePickerDialog eventDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                AddNewEventScreen.this.textViewEventDate.setText(year + "-" + (month + 1) + "-" + day);
            }
        }, givenCalendar.get(Calendar.YEAR), givenCalendar.get(Calendar.MONTH), givenCalendar.get(Calendar.DAY_OF_MONTH));
        Log.i("thismonth", String.valueOf(givenCalendar.get(Calendar.MONTH)));
        eventDateDialog.show();
    }

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

        Event newEvent = new Event(12, title, details, location, dateTime);
        Log.i("eventcreated", newEvent.getEventDate());
        Log.i("eventcre", newEvent.getEventDetails());
    }


    //returns users to the previous tab view
    public void cancelEditEvent(View view) {
    }

}