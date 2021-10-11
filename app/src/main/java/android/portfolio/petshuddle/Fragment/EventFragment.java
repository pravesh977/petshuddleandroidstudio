package android.portfolio.petshuddle.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.portfolio.petshuddle.Adapter.EventsAdapter;
import android.portfolio.petshuddle.Adapter.MyPetsAdapter;
import android.portfolio.petshuddle.Entity.Event;
import android.portfolio.petshuddle.Entity.Pet;
import android.portfolio.petshuddle.Helper.MySingletonRequestQueue;
import android.portfolio.petshuddle.UI.AddNewEventScreen;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.portfolio.petshuddle.R;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {

    private RecyclerView eventsRecyclerView;
    private EventsAdapter eventsAdapter;
    private List<Event> eventList = new ArrayList<>();
    private FloatingActionButton floatingActionButtonEvent;
    private Button newEventScreenButton;
    private Boolean isButtonVisible;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
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

        isButtonVisible = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView);
        floatingActionButtonEvent = view.findViewById(R.id.floatingActionButtonEvent);
        newEventScreenButton = view.findViewById(R.id.newEventScreenButton);
        floatingActionButtonEvent.setOnClickListener(toggleAddEventDisplay);
        newEventScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddNewEventScreen.class));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        eventList.clear();

        String url = "http://10.0.2.2:8080/api/events/";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject reqobject = response.getJSONObject(i);
                        int eventId = reqobject.getInt("eventId");
                        String eventTitle = reqobject.getString("eventTitle");
                        String eventDetails = reqobject.getString("eventDetails");
                        String eventLocation = reqobject.getString("eventLocation");
                        String eventDate = reqobject.getString("eventDate");

                        Event responseEvent = new Event(eventId, eventTitle, eventDetails, eventLocation, eventDate);
                        eventList.add(responseEvent);
//                        Log.i("petname is: ", jsonPet.getPetName());
//                        Log.i("pets list increment: ", String.valueOf(myPetsList.size()));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                eventsAdapter = new EventsAdapter(eventList, getContext());
                eventsRecyclerView.setAdapter(eventsAdapter);
                eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                Log.i("arraysize : ", String.valueOf(myPetsList.size()));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        MySingletonRequestQueue.getInstance(this.getActivity()).addToRequestQueue(request);


    }

    public View.OnClickListener toggleAddEventDisplay = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isButtonVisible == false) {
                newEventScreenButton.setVisibility(View.VISIBLE);
//                Log.i("buttonvisibility", "button isi visible");
                isButtonVisible = true;
            }
            else {
                newEventScreenButton.setVisibility(View.GONE);
//                Log.i("buttonvisibility", "now invisible");
                isButtonVisible = false;
            }

        }
    };

    //removes the button if the user nagivates away from the screen while
    //the button is visible
    @Override
    public void onPause() {
        super.onPause();
        isButtonVisible = false;
        newEventScreenButton.setVisibility(View.GONE);
    }

}