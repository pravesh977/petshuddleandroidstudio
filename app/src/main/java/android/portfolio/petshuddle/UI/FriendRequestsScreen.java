package android.portfolio.petshuddle.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.portfolio.petshuddle.Adapter.MyPetsAdapter;
import android.portfolio.petshuddle.Adapter.RequesterAdapter;
import android.portfolio.petshuddle.Entity.Friend;
import android.portfolio.petshuddle.Entity.Pet;
import android.portfolio.petshuddle.Helper.MySingletonRequestQueue;
import android.portfolio.petshuddle.R;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendRequestsScreen extends AppCompatActivity {

    List<Integer> requestIdList = new ArrayList<>();
    List<Pet> friendRequesterPetsList = new ArrayList<>();
    private TextView textViewFriendsRequest;
    //    private MyPetsAdapter friendRequesterAdapter;
//    private RecyclerView requesterRecyclerView;
    private ListView requesterListView;
    private ProgressBar requestProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests_screen);

        textViewFriendsRequest = findViewById(R.id.textViewFriendsRequest);
//        requesterRecyclerView = findViewById(R.id.requesterRecyclerView);
        requesterListView = findViewById(R.id.requesterListView);
        requestProgressBar = findViewById(R.id.requestProgressBar);

        requestProgressBar.setVisibility(View.VISIBLE);

        int petId = getIntent().getIntExtra("petId", -1);
        //getting a list of friend requests that are not yet accepted for current pet using its id
        String url = "http://petshuddlefinal-env.eba-fzpmwzky.us-east-2.elasticbeanstalk.com/api/friendslist/friendrequests/";
//        Log.i("this pet is : ", String.valueOf(petId));

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url + petId, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                requestProgressBar.setVisibility(View.INVISIBLE);

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject reqobject = response.getJSONObject(i);
                        int petId = reqobject.getInt("petId");
                        int friendId = reqobject.getInt("friendId");
                        Friend friendObject = new Friend(petId, friendId);
                        requestIdList.add(petId);
//                        friendsList.add(friendObject);
//                        friendsIdList.add(friendId);
//                        Log.i("friend object ", friendObject.toString());
//                        Log.i("friend is ", String.valueOf(friendObject.getFriendId()));
//                        Log.i("pet id ", String.valueOf(friendObject.getPetId()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //loads the list of friends in a horizontal recycler view
                getRequestersProfiles();
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
        };

        MySingletonRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    public void getRequestersProfiles() {

        JSONArray requestersIdJsonArray = new JSONArray(requestIdList);

        String url = "http://petshuddlefinal-env.eba-fzpmwzky.us-east-2.elasticbeanstalk.com/api/petshuddle/petfriends/";
//        List<Pet> friendsForPetList = new ArrayList<>();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, requestersIdJsonArray, new Response.Listener<JSONArray>() {
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
                        friendRequesterPetsList.add(jsonPet);
//                        Log.i("petname is: ", jsonPet.getPetName());
//                        Log.i("pets list increment: ", String.valueOf(myPetsList.size()));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (friendRequesterPetsList.size() == 0) {
                    textViewFriendsRequest.setText("No new friend requests!");
                }
//                friendRequesterAdapter = new MyPetsAdapter(friendRequesterPetsList, FriendRequestsScreen.this);
//                requesterRecyclerView.setAdapter(friendRequesterAdapter);
//                requesterRecyclerView.setLayoutManager(new LinearLayoutManager(FriendRequestsScreen.this));

                RequesterAdapter requesterAdapter = new RequesterAdapter(FriendRequestsScreen.this, friendRequesterPetsList, getIntent().getIntExtra("petId", -1));
                requesterListView.setAdapter(requesterAdapter);
//                requesterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Toast.makeText(getApplicationContext(), "Click ListItem Number " + , Toast.LENGTH_LONG)
//                                .show();
//                    }
//                });

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
        };
        MySingletonRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    public void cancelRequestPage(View view) {
        this.finish();
    }
}