package android.portfolio.petshuddle.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.portfolio.petshuddle.R;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class SingleEventScreen extends AppCompatActivity {

    private TextView textViewEventId;
    private TextView editTextEventTitle;
    private TextView editTextEventDetails;
    private TextView editTextEventLocation;
    private TextView editTextEventDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event_screen);

        int eventId = getIntent().getIntExtra("eventId", -1);
        String eventTitle = getIntent().getStringExtra("eventTitle");
        String eventDetails = getIntent().getStringExtra("eventTitle");
        String eventLocation = getIntent().getStringExtra("eventLocation");
        String eventDate = getIntent().getStringExtra("eventDate");

        textViewEventId = findViewById(R.id.textViewEventId);
        editTextEventTitle = findViewById(R.id.editTextEventTitle);
        editTextEventDetails = findViewById(R.id.editTextEventDetails);
        editTextEventLocation = findViewById(R.id.editTextEventLocation);
        editTextEventDate = findViewById(R.id.editTextEventDate);

        textViewEventId.setText(String.valueOf(eventId));
        editTextEventTitle.setText(eventTitle);
        editTextEventDetails.setText(eventDetails);
        editTextEventLocation.setText(eventLocation);
        editTextEventDate.setText(eventDate);
    }

    //handles the save button and saves the event to the database
    public void handleSaveEventEdit(View view) {

        int eventId = Integer.parseInt(textViewEventId.getText().toString().trim());
        String title = editTextEventTitle.getText().toString().trim();
        String details = editTextEventDetails.getText().toString().trim();
        String location = editTextEventLocation.getText().toString().trim();
        String date = editTextEventDate.getText().toString().trim();

        if(title.isEmpty()) {
            editTextEventTitle.setError("Title can't be empty");
            editTextEventTitle.requestFocus();
            return;
        }
        if(details.isEmpty()) {
            editTextEventDetails.setError("Details can't be empty");
            editTextEventDetails.requestFocus();
            return;
        }
        if(location.isEmpty()) {
            editTextEventLocation.setError("Location can't be empty");
            editTextEventLocation.requestFocus();
            return;
        }
        if(date.isEmpty()) {
            editTextEventDate.setError("Date can't be empty");
            editTextEventDate.requestFocus();
            return;
        }
        //
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

        editTextEventDate.setClickable(true);
        editTextEventDate.setFocusable(true);
        editTextEventDate.setFocusableInTouchMode(true);
        editTextEventDate.setCursorVisible(true);
    }

    //handles the cancel button and returns to main tabbed screen
    public void handleCancelEditEvent(View view) {
        this.finish();
    }
}