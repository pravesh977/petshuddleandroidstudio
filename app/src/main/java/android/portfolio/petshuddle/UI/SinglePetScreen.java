package android.portfolio.petshuddle.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.portfolio.petshuddle.Helper.MySingletonRequestQueue;
import android.portfolio.petshuddle.R;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class SinglePetScreen extends AppCompatActivity {

    private TextView editTextPetId;
    private EditText editTextPetName;
    private EditText editTextPetSpecies;
//    private EditText editTextPetSex;
    private EditText editTextPetBreed;
    private EditText editTextPetAge;
    private EditText editTextPetDescription;
    private String petUserId;
    private Spinner editGenderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_pet_screen);


        //getting values from the adapter of the selected pet ViewHolder
        int petId = getIntent().getIntExtra("petId", -1);
        String petName = getIntent().getStringExtra("petName");
        String species = getIntent().getStringExtra("species");
        String sex = getIntent().getStringExtra("sex");
        String breed = getIntent().getStringExtra("breed");
        int age = getIntent().getIntExtra("age", -1);
        String petDescription = getIntent().getStringExtra("description");
        petUserId = getIntent().getStringExtra("userId");


        editTextPetId = findViewById(R.id.editTextPetId);
        editTextPetName = findViewById(R.id.editTextPetName);
        editTextPetSpecies = findViewById(R.id.editTextPetSpecies);
//        editTextPetSex = findViewById(R.id.editTextPetSex);
        editTextPetBreed = findViewById(R.id.editTextPetBreed);
        editTextPetAge = findViewById(R.id.editTextPetAge);
        editTextPetDescription = findViewById(R.id.editTextPetDescription);
        editGenderSpinner = findViewById(R.id.editGenderSpinner);
//        editTextPetUserId = findViewById(R.id.editTextPetUserId);

        String petGenders[] = {"Male", "Female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, R.layout.spinner_text_file, petGenders);
        editGenderSpinner.setAdapter(genderAdapter);

        //setting edit texts to the appropriate passed values
        editTextPetId.setText(String.valueOf(petId));
        editTextPetName.setText(petName);
        editTextPetSpecies.setText(species);
//        editTextPetSex.setText(sex);
        int genderValue = genderAdapter.getPosition(sex);
        editGenderSpinner.setSelection(genderValue);
        editTextPetBreed.setText(breed);
        editTextPetAge.setText(String.valueOf(age));
        editTextPetDescription.setText(petDescription);
//        editTextPetUserId.setText(userId);
    }

    //handles the cancel button which sends the user back to the main tabbed view without making any changes
    public void handleCancelEditPet(View view) {
        this.finish();
    }


    //method that handles the Edit button which enables edit features on the edittext fields
    public void handleStartEditPressed(View view) {

        editTextPetName.setClickable(true);
        editTextPetName.setFocusable(true);
        editTextPetName.setFocusableInTouchMode(true);
        editTextPetName.setCursorVisible(true);

        //requesting focus on the name EditText Field and opening the keyboard
        editTextPetName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextPetName, InputMethodManager.SHOW_IMPLICIT);

        editTextPetSpecies.setClickable(true);
        editTextPetSpecies.setFocusable(true);
        editTextPetSpecies.setFocusableInTouchMode(true);
        editTextPetSpecies.setCursorVisible(true);

        editGenderSpinner.setClickable(true);
        editGenderSpinner.setFocusable(true);
        editGenderSpinner.setFocusableInTouchMode(true);

        editTextPetBreed.setClickable(true);
        editTextPetBreed.setFocusable(true);
        editTextPetBreed.setFocusableInTouchMode(true);
        editTextPetBreed.setCursorVisible(true);

        editTextPetAge.setClickable(true);
        editTextPetAge.setFocusable(true);
        editTextPetAge.setFocusableInTouchMode(true);
        editTextPetAge.setCursorVisible(true);

        editTextPetDescription.setClickable(true);
        editTextPetDescription.setFocusable(true);
        editTextPetDescription.setFocusableInTouchMode(true);
        editTextPetDescription.setCursorVisible(true);

    }

    public void handleSavePetEdit(View view) {

        int petId = Integer.parseInt(editTextPetId.getText().toString());
        String name = editTextPetName.getText().toString().trim();
        String species = editTextPetSpecies.getText().toString().trim();
        String sex = editGenderSpinner.getSelectedItem().toString().trim();
        String breed = editTextPetBreed.getText().toString().trim();
        String age = editTextPetAge.getText().toString().trim();
        String description = editTextPetDescription.getText().toString().trim();
        String userId = petUserId;

        if(name.isEmpty()) {
            editTextPetName.setError("Name can't be empty");
            editTextPetName.requestFocus();
            return;
        }
        if(species.isEmpty()) {
            editTextPetSpecies.setError("Species can't be empty");
            editTextPetSpecies.requestFocus();
            return;
        }
//        if(sex.isEmpty()) {
//            editTextPetSex.setError("Sex can't be empty");
//            editTextPetSex.requestFocus();
//            return;
//        }
        if(breed.isEmpty()) {
            editTextPetBreed.setError("Breed can't be empty");
            editTextPetBreed.requestFocus();
            return;
        }
        if(age.isEmpty()) {
            editTextPetAge.setError("Age can't be empty");
            editTextPetAge.requestFocus();
            return;
        }
        if(description.isEmpty()) {
            editTextPetDescription.setError("Description can't be empty");
            editTextPetDescription.requestFocus();
            return;
        }

        // Instantiate the RequestQueue.
        String url ="http://10.0.2.2:8080/api/petshuddle/";

        JSONObject petJson = new JSONObject();
        try {
            petJson.put("petName", name);
            petJson.put("species", species);
            petJson.put("sex", sex);
            petJson.put("breed", breed);
            petJson.put("age", age);
            petJson.put("petDescription", description);
            petJson.put("userId", userId);
        } catch(JSONException ex) {
            ex.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url + petId, petJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.i("Updated object is : ", response.toString());
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        MySingletonRequestQueue.getInstance(this).addToRequestQueue(request);
    }
}