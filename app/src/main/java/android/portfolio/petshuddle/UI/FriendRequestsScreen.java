package android.portfolio.petshuddle.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.portfolio.petshuddle.Adapter.MyPetsAdapter;
import android.portfolio.petshuddle.Entity.Friend;
import android.portfolio.petshuddle.Entity.Pet;
import android.portfolio.petshuddle.Helper.MySingletonRequestQueue;
import android.portfolio.petshuddle.R;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestsScreen extends AppCompatActivity {

    List<Integer> requestIdList = new ArrayList<>();
    List<Pet> friendRequesterPetsList = new ArrayList<>();
    private TextView textViewFriendsRequest;
    private MyPetsAdapter friendRequesterAdapter;
    private RecyclerView requesterRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests_screen);

        textViewFriendsRequest = findViewById(R.id.textViewFriendsRequest);
        requesterRecyclerView = findViewById(R.id.requesterRecyclerView);

        int petId = getIntent().getIntExtra("petId", -1);
        //getting a list of friend requests that are not yet accepted for current pet using its id
        String url = "http://10.0.2.2:8080/api/friendslist/friendrequests/";
        Log.i("this pet is : ", String.valueOf(petId));

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url + petId, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
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
        });

        MySingletonRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    public void getRequestersProfiles() {

        JSONArray requestersIdJsonArray = new JSONArray(requestIdList);

        String url = "http://10.0.2.2:8080/api/petshuddle/petfriends/";
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
                if(friendRequesterPetsList.size() == 0) {
                    textViewFriendsRequest.setText("No friend requests yet!");
                }
                friendRequesterAdapter = new MyPetsAdapter(friendRequesterPetsList, FriendRequestsScreen.this);
                requesterRecyclerView.setAdapter(friendRequesterAdapter);
                requesterRecyclerView.setLayoutManager(new LinearLayoutManager(FriendRequestsScreen.this) {
                    Button button = new Button(FriendRequestsScreen.this);
//                    @Override
//                    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
//                        lp.width = getWidth() / 2;
////                        return super.checkLayoutParams(lp);
//                        return true;
//                    }
                });
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