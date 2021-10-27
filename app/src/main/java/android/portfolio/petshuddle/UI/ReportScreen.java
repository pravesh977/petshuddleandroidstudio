package android.portfolio.petshuddle.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.portfolio.petshuddle.Helper.MySingletonRequestQueue;
import android.portfolio.petshuddle.R;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ReportScreen extends AppCompatActivity {

    private Button generateReportButton;
    private Spinner monthlySpinner;
    private TextView textViewNumberOfEvents;
    private TextView textViewNumberOfPets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_screen);

        generateReportButton = findViewById(R.id.generateReportButton);
        monthlySpinner = findViewById(R.id.monthlySpinner);
        textViewNumberOfEvents = findViewById(R.id.textViewNumberOfEvents);
        textViewNumberOfPets = findViewById(R.id.textViewNumberOfPets);

        String monthsArray[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<>(this, R.layout.spinner_text_file, monthsArray);
        monthlySpinner.setAdapter(monthsAdapter);

        generateReportButton.setOnClickListener(handleGenerateReport);
    }

    public View.OnClickListener handleGenerateReport = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String selectedMonth = monthlySpinner.getSelectedItem().toString();
            String url = "http://10.0.2.2:8080/api/events/eventsbymonth/";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url + selectedMonth, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
//                Log.i("responseReport", response.toString());
//                Toast.makeText(getContext(), "Pet Deleted", Toast.LENGTH_LONG).show();
                    try {
                        textViewNumberOfEvents.setText(response.getString("numOfEventsInMonth"));
                        textViewNumberOfPets.setText(response.getString("numOfPetsInMonthlyEvent"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            MySingletonRequestQueue.getInstance(ReportScreen.this).addToRequestQueue(request);
        }
    };

    public void backButtonPressed(View view) {
        this.finish();
    }
}