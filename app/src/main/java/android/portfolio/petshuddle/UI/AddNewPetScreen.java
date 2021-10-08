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
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddNewPetScreen extends AppCompatActivity {

    private EditText addTextPetName;
    private EditText addTextPetSpecies;
    private EditText addTextPetSex;
    private EditText addTextPetBreed;
    private EditText addTextPetAge;
    private EditText addTextPetDescription;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pet_screen);

        addTextPetName = findViewById(R.id.addTextPetName);
        addTextPetSpecies = findViewById(R.id.addTextPetSpecies);
        addTextPetSex = findViewById(R.id.addTextPetSex);
        addTextPetBreed = findViewById(R.id.addTextPetBreed);
        addTextPetAge = findViewById(R.id.addTextPetAge);
        addTextPetDescription = findViewById(R.id.addTextPetDescription);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();
    }

    public void handleCancelAddPet(View view) {
        this.finish();
    }

    public void handleNewPetAdd(View view) {

        String petName = addTextPetName.getText().toString().trim();
        String species = addTextPetSpecies.getText().toString().trim();
        String sex = addTextPetSex.getText().toString().trim();
        String breed = addTextPetBreed.getText().toString().trim();
        String age = addTextPetAge.getText().toString().trim();
        String description = addTextPetDescription.getText().toString().trim();

        //Pet newPet = new Pet(0, petName, species, sex, breed, Integer.parseInt(age), description, currentUserId);

        // Instantiate the RequestQueue.
        RequestQueue queue = MySingletonRequestQueue.getInstance(this).getRequestQueue();
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