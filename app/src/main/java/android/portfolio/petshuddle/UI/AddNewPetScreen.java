package android.portfolio.petshuddle.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.portfolio.petshuddle.Adapter.MyPetsAdapter;
import android.portfolio.petshuddle.Entity.Pet;
import android.portfolio.petshuddle.Fragment.MyPetsFragment;
import android.portfolio.petshuddle.Helper.MySingletonRequestQueue;
import android.portfolio.petshuddle.R;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddNewPetScreen extends AppCompatActivity {

    private EditText addTextPetName;
    private EditText addTextPetSpecies;
    private EditText addTextPetBreed;
    private EditText addTextPetAge;
    private EditText addTextPetDescription;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private Spinner petGenderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pet_screen);

        addTextPetName = findViewById(R.id.addTextPetName);
        addTextPetSpecies = findViewById(R.id.addTextPetSpecies);
        addTextPetBreed = findViewById(R.id.addTextPetBreed);
        addTextPetAge = findViewById(R.id.addTextPetAge);
        addTextPetDescription = findViewById(R.id.addTextPetDescription);
        petGenderSpinner = findViewById(R.id.petGenderSpinner);

        String petGenders[] = {"Male", "Female"};
//        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, petGenders);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, R.layout.spinner_text_file, petGenders);

        petGenderSpinner.setAdapter(genderAdapter);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();
//        Log.i("startedscreen", "started this screen");
//        petGenderArrayList.
    }

    public void handleCancelAddPet(View view) {
        this.finish();
    }

    public void handleNewPetAdd(View view) {

        String petName = addTextPetName.getText().toString().trim();
        String species = addTextPetSpecies.getText().toString().trim();
        String sex = petGenderSpinner.getSelectedItem().toString().trim();
        String breed = addTextPetBreed.getText().toString().trim();
        String age = addTextPetAge.getText().toString().trim();
        String description = addTextPetDescription.getText().toString().trim();

        //Pet newPet = new Pet(0, petName, species, sex, breed, Integer.parseInt(age), description, currentUserId);

        if(petName.isEmpty()) {
            addTextPetName.setError("Name can't be empty");
            addTextPetName.requestFocus();
            return;
        }
        if(species.isEmpty()) {
            addTextPetSpecies.setError("Species can't be empty");
            addTextPetSpecies.requestFocus();
            return;
        }
//        if(sex.isEmpty()) {
//            petGenderSpinner.setError("Sex can't be empty");
//            petGenderSpinner.requestFocus();
//            return;
//        }
        if(breed.isEmpty()) {
            addTextPetBreed.setError("Breed can't be empty");
            addTextPetBreed.requestFocus();
            return;
        }
        if(age.isEmpty()) {
            addTextPetAge.setError("Age can't be empty");
            addTextPetAge.requestFocus();
            return;
        }
        if(description.isEmpty()) {
            addTextPetDescription.setError("Description can't be empty");
            addTextPetDescription.requestFocus();
            return;
        }

        // Instantiate the RequestQueue.
        //RequestQueue queue = MySingletonRequestQueue.getInstance(this).getRequestQueue();
        String url ="http://10.0.2.2:8080/api/petshuddle";

        JSONObject petJson = new JSONObject();
        try {
            petJson.put("petName", petName);
            petJson.put("species", species);
            petJson.put("sex", sex);
            petJson.put("breed", breed);
            petJson.put("age", age);
            petJson.put("petDescription", description);
            petJson.put("userId", currentUserId);
        } catch(JSONException ex) {
            ex.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, petJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //update UI here
//                Intent intent = new Intent(AddNewPetScreen.this, MainTabbedActivity.class);
//                startActivity(intent);
//                Log.i("added object is : ", response.toString());
//                MyPetsFragment petsFragment = new MyPetsFragment();
//                petsFragment.changeList();
//                finish();
//                if(status)
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddNewPetScreen.this, "Something went wrong: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int statusCode = response.statusCode;
                Log.i("statuscode", String.valueOf(statusCode));
                if(statusCode == 201) {
                    finish();
                }
                else {
                    Log.i("failedaddpet", "cant add this");
                    Toast.makeText(AddNewPetScreen.this, "Adding failed: ", Toast.LENGTH_LONG).show();
                }
                return super.parseNetworkResponse(response);
            }
        };

        MySingletonRequestQueue.getInstance(this).addToRequestQueue(request);
    }
}