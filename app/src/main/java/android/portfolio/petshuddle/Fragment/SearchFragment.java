package android.portfolio.petshuddle.Fragment;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.portfolio.petshuddle.Adapter.EventsAdapter;
import android.portfolio.petshuddle.Adapter.MyPetsAdapter;
import android.portfolio.petshuddle.Entity.Event;
import android.portfolio.petshuddle.Entity.Pet;
import android.portfolio.petshuddle.Helper.MySingletonRequestQueue;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.portfolio.petshuddle.R;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private Spinner searchSpinner;
    private String searchString;
    //    private List<Event> searchedEventsList = new ArrayList<>();
//    private MyPetsAdapter myPetsAdapter;
//    private EventsAdapter eventsAdapter;
    private LinearLayout searchLinearLayout;
    private RecyclerView searchRecyclerView;
    private EditText searchTextView;
    private Button searchButton;
    private ProgressBar searchProgressBar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CoordinatorLayout coordinatorLay;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
//        Snackbar.make(coordinatorLay,"this is search", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        coordinatorLay = view.findViewById(R.id.coordinatorLay);
        searchSpinner = view.findViewById(R.id.searchSpinner);
        searchTextView = view.findViewById(R.id.searchTextView);
        searchButton = view.findViewById(R.id.searchButton);
        searchLinearLayout = view.findViewById(R.id.searchLinearLayout);
        searchRecyclerView = view.findViewById(R.id.searchRecyclerView);
        searchProgressBar = view.findViewById(R.id.searchProgressBar);

        String[] searchStringArray = {"Events", "Pets"};
        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, searchStringArray);
        searchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(searchAdapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                querySubmit(searchTextView.getText().toString());
            }
        });
    }

    //    handle search submit
    public void querySubmit(String query) {

        searchProgressBar.setVisibility(View.VISIBLE);
        String spinnerValue = searchSpinner.getSelectedItem().toString();
        if (spinnerValue.equals("Pets")) {

//            RecyclerView searchedPetsRecyclerView = new RecyclerView(getActivity());
//            searchedPetsRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            List<Pet> searchedPetsList = new ArrayList<>();

            if (query.equals("")) {

                String url = "http://petshuddlefinal-env.eba-fzpmwzky.us-east-2.elasticbeanstalk.com/api/petshuddle";

                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        searchProgressBar.setVisibility(View.INVISIBLE);
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
                                searchedPetsList.add(jsonPet);
//                        Log.i("petname is: ", jsonPet.getPetName());
//                        Log.i("pets list increment: ", String.valueOf(myPetsList.size()));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (searchedPetsList.size() == 0) {
                            Snackbar.make(searchRecyclerView, "Could not find any Pet with name " + query, Snackbar.LENGTH_LONG).show();
                        }
                        MyPetsAdapter myPetsAdapter = new MyPetsAdapter(searchedPetsList, getContext());
                        searchRecyclerView.setAdapter(myPetsAdapter);
                        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                MySingletonRequestQueue.getInstance(this.getActivity()).addToRequestQueue(request);

            } else {
                //            searchedPetsList.clear();
                String url = "http://petshuddlefinal-env.eba-fzpmwzky.us-east-2.elasticbeanstalk.com/api/petshuddle/searchpets/";

                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url + query, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        searchProgressBar.setVisibility(View.INVISIBLE);
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
                                searchedPetsList.add(jsonPet);
//                        Log.i("petname is: ", jsonPet.getPetName());
//                        Log.i("pets list increment: ", String.valueOf(myPetsList.size()));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (searchedPetsList.size() == 0) {
                            Snackbar.make(searchRecyclerView, "Could not find any Pet with name " + query, Snackbar.LENGTH_LONG).show();
                        }
                        MyPetsAdapter myPetsAdapter = new MyPetsAdapter(searchedPetsList, getContext());
                        searchRecyclerView.setAdapter(myPetsAdapter);
                        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                MySingletonRequestQueue.getInstance(this.getActivity()).addToRequestQueue(request);
            }
        } else {
//            Toast.makeText(getActivity(), spinnerValue + " " + query, Toast.LENGTH_LONG).show();
            List<Event> searchedEventsList = new ArrayList<>();


            if (query.equals("")) {
                String url = "http://petshuddlefinal-env.eba-fzpmwzky.us-east-2.elasticbeanstalk.com/api/events";
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url + query, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        searchProgressBar.setVisibility(View.INVISIBLE);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject reqobject = response.getJSONObject(i);
                                int eventId = reqobject.getInt("eventId");
                                String eventTitle = reqobject.getString("eventTitle");
                                String eventDetails = reqobject.getString("eventDetails");
                                String eventLocation = reqobject.getString("eventLocation");
                                String eventDate = reqobject.getString("eventDate");
                                String userId = reqobject.getString("userId");
                                JSONArray petJsonArray = reqobject.getJSONArray("petsListForEvent");
                                int numberOfEventAttendees = petJsonArray.length();
                                Event responseEvent = new Event(eventId, eventTitle, eventDetails, eventLocation, eventDate, userId, numberOfEventAttendees);
                                searchedEventsList.add(responseEvent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (searchedEventsList.size() == 0) {
                            Snackbar.make(searchRecyclerView, "Could not find any Event with title " + query, Snackbar.LENGTH_LONG).show();
                        }
                        EventsAdapter eventsAdapter = new EventsAdapter(searchedEventsList, getContext());
                        searchRecyclerView.setAdapter(eventsAdapter);
                        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                MySingletonRequestQueue.getInstance(this.getActivity()).addToRequestQueue(request);
            } else {
                String url = "http://petshuddlefinal-env.eba-fzpmwzky.us-east-2.elasticbeanstalk.com/api/events/searchevents/";
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url + query, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        searchProgressBar.setVisibility(View.INVISIBLE);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject reqobject = response.getJSONObject(i);
                                int eventId = reqobject.getInt("eventId");
                                String eventTitle = reqobject.getString("eventTitle");
                                String eventDetails = reqobject.getString("eventDetails");
                                String eventLocation = reqobject.getString("eventLocation");
                                String eventDate = reqobject.getString("eventDate");
                                String userId = reqobject.getString("userId");
                                JSONArray petJsonArray = reqobject.getJSONArray("petsListForEvent");
                                int numberOfEventAttendees = petJsonArray.length();
                                Event responseEvent = new Event(eventId, eventTitle, eventDetails, eventLocation, eventDate, userId, numberOfEventAttendees);
                                searchedEventsList.add(responseEvent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (searchedEventsList.size() == 0) {
                            Snackbar.make(searchRecyclerView, "Could not find any Event with title " + query, Snackbar.LENGTH_LONG).show();
                        }
                        EventsAdapter eventsAdapter = new EventsAdapter(searchedEventsList, getContext());
                        searchRecyclerView.setAdapter(eventsAdapter);
                        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                MySingletonRequestQueue.getInstance(this.getActivity()).addToRequestQueue(request);
            }

        }
    }

}