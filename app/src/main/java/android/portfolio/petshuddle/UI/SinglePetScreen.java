package android.portfolio.petshuddle.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.portfolio.petshuddle.Adapter.MyPetsAdapter;
import android.portfolio.petshuddle.Entity.Friend;
import android.portfolio.petshuddle.Entity.Pet;
import android.portfolio.petshuddle.Helper.MySingletonRequestQueue;
import android.portfolio.petshuddle.R;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SinglePetScreen extends AppCompatActivity {

    private TextView editTextPetId;
    private EditText editTextPetName;
    //    private EditText editTextPetSpecies;
//    private EditText editTextPetSex;
    private EditText editTextPetBreed;
    private EditText editTextPetAge;
    private EditText editTextPetDescription;
    private Button startEditPetButton;
    private Button saveEditPetButton;
    private String petUserId;
    private Spinner editGenderSpinner;
    private Spinner editSpeciesSpinner;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserId;
    private FloatingActionButton floatingActionAddFriend;
    private Button addFriendButton;
    private RecyclerView petsFriendsRecyclerView;
    List<Friend> friendsList = new ArrayList<>();
    List<Integer> friendsIdList = new ArrayList<>();
    private TextView textViewNumberOfFriends;
    List<Pet> friendsForPetList = new ArrayList<>();
    private MyPetsAdapter petFriendsAdapter;
    private LinearLayout petFloatingLinearLayout;
    private Button friendRequestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_pet_screen);

        Log.i("created", "single pet screen created");

        //getting values from the adapter of the selected pet ViewHolder
        int petId = getIntent().getIntExtra("petId", -1);
        String petName = getIntent().getStringExtra("petName");
        String species = getIntent().getStringExtra("species");
        String sex = getIntent().getStringExtra("sex");
        String breed = getIntent().getStringExtra("breed");
        int age = getIntent().getIntExtra("age", -1);
        String petDescription = getIntent().getStringExtra("description");
        petUserId = getIntent().getStringExtra("userId");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();

        editTextPetId = findViewById(R.id.editTextPetId);
        editTextPetName = findViewById(R.id.editTextPetName);
//        editTextPetSpecies = findViewById(R.id.editTextPetSpecies);
//        editTextPetSex = findViewById(R.id.editTextPetSex);
        editSpeciesSpinner = findViewById(R.id.editSpeciesSpinner);
        editTextPetBreed = findViewById(R.id.editTextPetBreed);
        editTextPetAge = findViewById(R.id.editTextPetAge);
        editTextPetDescription = findViewById(R.id.editTextPetDescription);
        editGenderSpinner = findViewById(R.id.editGenderSpinner);
        startEditPetButton = findViewById(R.id.startEditPetButton);
        saveEditPetButton = findViewById(R.id.saveEditPetButton);
//        editTextPetUserId = findViewById(R.id.editTextPetUserId);
        floatingActionAddFriend = findViewById(R.id.floatingActionAddFriend);
        addFriendButton = findViewById(R.id.addFriendButton);
        petsFriendsRecyclerView = findViewById(R.id.petsFriendsRecyclerView);
        textViewNumberOfFriends = findViewById(R.id.textViewNumberOfFriends);
        petFloatingLinearLayout = findViewById(R.id.petFloatingLinearLayout);
        friendRequestButton = findViewById(R.id.friendRequestButton);

//        Button requestButton = new Button(new ContextThemeWrapper(this, R.style.MainButtons), null, 0);
//        requestButton.setText("Friend Requests");
//        requestButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        requestButton.setVisibility(View.INVISIBLE);
//        requestButton.setBackgroundColor(Color.parseColor("#71E798"));

        floatingActionAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addFriendButton.getVisibility() == View.INVISIBLE) {
                    addFriendButton.setVisibility(View.VISIBLE);
                } else {
                    addFriendButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        addFriendButton.setOnClickListener(openPetChooserDialog);

        String petGenders[] = {"Male", "Female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, R.layout.spinner_text_file, petGenders);
        editGenderSpinner.setAdapter(genderAdapter);

        String petSpecies[] = {"Cat", "Dog", "Hamster", "Bird", "Rabbit", "Horse", "Turtle", "Fish", "Reptile", "Other"};
        ArrayAdapter<String> speciesAdapter = new ArrayAdapter<>(this, R.layout.spinner_text_file, petSpecies);
        editSpeciesSpinner.setAdapter(speciesAdapter);

        //setting edit texts to the appropriate passed values
        editTextPetId.setText(String.valueOf(petId));
        editTextPetName.setText(petName);
        int genderValue = genderAdapter.getPosition(sex);
        editGenderSpinner.setSelection(genderValue);
//        editTextPetSpecies.setText(species);
//        editTextPetSex.setText(sex);
        editTextPetAge.setText(String.valueOf(age));
        int speciesValue = speciesAdapter.getPosition(species);
        editSpeciesSpinner.setSelection(speciesValue);
        editTextPetBreed.setText(breed);
        editTextPetDescription.setText(petDescription);
//        editTextPetUserId.setText(userId);

        if (currentUserId.equals(petUserId)) {
            startEditPetButton.setVisibility(View.VISIBLE);
            saveEditPetButton.setVisibility(View.VISIBLE);
//            petFloatingLinearLayout.addView(requestButton);
            friendRequestButton.setVisibility(View.VISIBLE);
        } else {
            startEditPetButton.setVisibility(View.INVISIBLE);
            saveEditPetButton.setVisibility(View.INVISIBLE);
            friendRequestButton.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i("started", "single pet screen started");
        friendsIdList.clear();
        //getting a list of friends for current pet using its id
        String url = "http://10.0.2.2:8080/api/friendslist/friendsbypetid/";
//        Log.i("current pet id is : ", String.valueOf(petId));

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url + getIntent().getIntExtra("petId", -1), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject reqobject = response.getJSONObject(i);
                        int petId = reqobject.getInt("petId");
                        int friendId = reqobject.getInt("friendId");
                        Friend friendObject = new Friend(petId, friendId);
                        friendsList.add(friendObject);
                        friendsIdList.add(friendId);
//                        Log.i("friend object ", friendObject.toString());
//                        Log.i("friend is ", String.valueOf(friendObject.getFriendId()));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //loads the list of friends in a horizontal recycler view
                getFriendsProfile();
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
//                String credentials = "petsapiheader977:petsapikey977";
//                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("petsapiheader977", "petsapikey977");
//                headers.put("petsapiheader977", "petsapikey977");
                return headers;

            }
        };

        MySingletonRequestQueue.getInstance(this).addToRequestQueue(request);
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

        editSpeciesSpinner.setClickable(true);
        editSpeciesSpinner.setFocusable(true);
        editSpeciesSpinner.setFocusableInTouchMode(true);

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
        String species = editSpeciesSpinner.getSelectedItem().toString().trim();
        String sex = editGenderSpinner.getSelectedItem().toString().trim();
        String breed = editTextPetBreed.getText().toString().trim();
        String age = editTextPetAge.getText().toString().trim();
        String description = editTextPetDescription.getText().toString().trim();
        String userId = petUserId;

        if (name.isEmpty()) {
            editTextPetName.setError("Name can't be empty");
            editTextPetName.requestFocus();
            return;
        }
//        if(species.isEmpty()) {
//            editTextPetSpecies.setError("Species can't be empty");
//            editTextPetSpecies.requestFocus();
//            return;
//        }
//        if(sex.isEmpty()) {
//            editTextPetSex.setError("Sex can't be empty");
//            editTextPetSex.requestFocus();
//            return;
//        }
        if (breed.isEmpty()) {
            editTextPetBreed.setError("Breed can't be empty");
            editTextPetBreed.requestFocus();
            return;
        }
        if (age.isEmpty()) {
            editTextPetAge.setError("Age can't be empty");
            editTextPetAge.requestFocus();
            return;
        }
        if (description.isEmpty()) {
            editTextPetDescription.setError("Description can't be empty");
            editTextPetDescription.requestFocus();
            return;
        }

        // Instantiate the RequestQueue.
        String url = "http://10.0.2.2:8080/api/petshuddle/";

        JSONObject petJson = new JSONObject();
        try {
            petJson.put("petName", name);
            petJson.put("species", species);
            petJson.put("sex", sex);
            petJson.put("breed", breed);
            petJson.put("age", age);
            petJson.put("petDescription", description);
            petJson.put("userId", userId);
        } catch (JSONException ex) {
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();
//                String credentials = "petsapiheader977:petsapikey977";
//                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("petsapiheader977", "petsapikey977");
//                headers.put("petsapiheader977", "petsapikey977");
                return headers;

            }
        };

        MySingletonRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    public View.OnClickListener openPetChooserDialog = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Dialog myPetChooserDialog = new Dialog(SinglePetScreen.this);
            myPetChooserDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            myPetChooserDialog.setCancelable(true);
            myPetChooserDialog.setContentView(R.layout.dialog_pet_for_event);

            //get views from the layout. it has to be done here and not on the class initializer because it belongs
            //to another layout
            Button choosePetButton = myPetChooserDialog.findViewById(R.id.choosePetButton);
            Button cancelChoosePetButton = myPetChooserDialog.findViewById(R.id.cancelChoosePetButton);
            Spinner spinnerMyPets = myPetChooserDialog.findViewById(R.id.spinnerMyPets);
            TextView petChooserTitle = myPetChooserDialog.findViewById(R.id.petChooserTitle);
            petChooserTitle.setText("Choose which of your pets will make friends with " + editTextPetName.getText());

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
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> headers = new HashMap<>();
//                String credentials = "petsapiheader977:petsapikey977";
//                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    headers.put("Content-Type", "application/json");
                    headers.put("petsapiheader977", "petsapikey977");
//                headers.put("petsapiheader977", "petsapikey977");
                    return headers;

                }
            };

            MySingletonRequestQueue.getInstance(SinglePetScreen.this).addToRequestQueue(request);

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
                    addFriendToMyPet(selectedPet);
                    myPetChooserDialog.dismiss();
                }
            });
            myPetChooserDialog.show();
            addFriendButton.setVisibility(View.INVISIBLE);
        }
    };

    //handles the choose pet button from the dialog and saves the current pet as friend to the chosen pet from the drop down
    public void addFriendToMyPet(Pet myPet) {

        int myChosenPetId = myPet.getPetId();
        int friendId = getIntent().getIntExtra("petId", -1);
        char requestStatus = 'p';

        String url = "http://10.0.2.2:8080/api/friendslist";

        JSONObject friendJson = new JSONObject();
        try {
            friendJson.put("petId", myChosenPetId);
            friendJson.put("friendId", friendId);
            friendJson.put("requestStatus", requestStatus);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, friendJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                friendsForPetList.add(0, myPet);
                petFriendsAdapter.notifyItemInserted(0);
                petsFriendsRecyclerView.scrollToPosition(0);

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
//                String credentials = "petsapiheader977:petsapikey977";
//                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("petsapiheader977", "petsapikey977");
//                headers.put("petsapiheader977", "petsapikey977");
                return headers;

            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int statusCode = response.statusCode;
//                Log.i("codeis", String.valueOf(statusCode));
                if (statusCode == 201) {

                } else {
                    Toast.makeText(SinglePetScreen.this, "Adding Friend Failed: ", Toast.LENGTH_LONG).show();
                }
                return super.parseNetworkResponse(response);
            }
        };

        MySingletonRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    public void getFriendsProfile() {

        friendsForPetList.clear();
        JSONArray friendsIdJsonArray = new JSONArray(friendsIdList);
//        Log.i("array lenght is : ", String.valueOf(friendsIdJsonArray.length()));
//        Log.i("array is : ", String.valueOf(friendsIdJsonArray));
//        for(int i = 0; i< friendsIdJsonArray.length(); i++) {
//            try {
//                Log.i("passed array is : ", String.valueOf(friendsIdJsonArray.get(i)));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }


        String url = "http://10.0.2.2:8080/api/petshuddle/petfriends/";
//        List<Pet> friendsForPetList = new ArrayList<>();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, friendsIdJsonArray, new Response.Listener<JSONArray>() {
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
//                        Log.i("petName : ", petName);
//                        Log.i("Description", petDescription);
                        Pet jsonPet = new Pet(petId, petName, species, sex, breed, age, petDescription, userId);
                        friendsForPetList.add(jsonPet);
//                        Log.i("petname is: ", jsonPet.getPetName());
//                        Log.i("pets list increment: ", String.valueOf(myPetsList.size()));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (friendsForPetList.size() == 0) {
                    textViewNumberOfFriends.setText("This pet has no friends yet!");
                }
                petFriendsAdapter = new MyPetsAdapter(friendsForPetList, SinglePetScreen.this);
                petsFriendsRecyclerView.setAdapter(petFriendsAdapter);
                petsFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(SinglePetScreen.this, LinearLayoutManager.HORIZONTAL, false) {
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();
//                String credentials = "petsapiheader977:petsapikey977";
//                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("petsapiheader977", "petsapikey977");
//                headers.put("petsapiheader977", "petsapikey977");
                return headers;

            }
        };
        MySingletonRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    public void viewFriendRequests(View view) {
        int petId = getIntent().getIntExtra("petId", -1);
        Intent intent = new Intent(SinglePetScreen.this, FriendRequestsScreen.class);
        intent.putExtra("petId", petId);
        startActivity(intent);
    }

};