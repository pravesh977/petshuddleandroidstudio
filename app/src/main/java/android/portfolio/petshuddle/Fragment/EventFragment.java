package android.portfolio.petshuddle.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.portfolio.petshuddle.Adapter.EventsAdapter;
import android.portfolio.petshuddle.Adapter.MyPetsAdapter;
import android.portfolio.petshuddle.Entity.Event;
import android.portfolio.petshuddle.Entity.Pet;
import android.portfolio.petshuddle.Helper.MySingletonRequestQueue;
import android.portfolio.petshuddle.UI.AddNewEventScreen;
import android.portfolio.petshuddle.UI.SingleEventScreen;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.portfolio.petshuddle.R;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        Log.i("fragcrae", "created fragment");
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

//            @Override
//            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//                View itemView = viewHolder.itemView;
//                itemView.setBackgroundColor(Color.parseColor("#0B059E"));
//            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                Log.i("moved", "it is moving");
                return true;
            }

            //setting swipe functions on the recyclerview
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //implement alert and delete from database
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(getActivity());
                aBuilder.setTitle("Delete this Event?");
                aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int position = viewHolder.getAdapterPosition();
                        Event currentEvent = eventList.get(position);
                        handleDeleteEvent(currentEvent.getEventId());
                        eventList.remove(currentEvent);
                        eventsAdapter.notifyItemRemoved(position);
                        Snackbar.make(eventsRecyclerView,"Deleted Event " + currentEvent.getEventTitle(), Snackbar.LENGTH_LONG).show();
                    }
                });
                aBuilder.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                    //notifies the adapter of the item change which somehow puts the item back if no is clicked
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int position = viewHolder.getAdapterPosition();
                        eventsAdapter.notifyItemChanged(position);
                    }
                });
                aBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        int position = viewHolder.getAdapterPosition();
                        eventsAdapter.notifyItemChanged(position);
                    }
                });
                AlertDialog deleteDialog = aBuilder.create();
                //deleteDialog.setCanceledOnTouchOutside(false);
                deleteDialog.show();
            }
        }).attachToRecyclerView(eventsRecyclerView);
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
                        JSONArray petJsonArray = reqobject.getJSONArray("petsListForEvent");
//                        Log.i("lengthopets: ", String.valueOf(petJsonArray.length()));
                        Event responseEvent = new Event(eventId, eventTitle, eventDetails, eventLocation, eventDate);
                        eventList.add(responseEvent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                eventsAdapter = new EventsAdapter(eventList, getContext());
                eventsRecyclerView.setAdapter(eventsAdapter);
                eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        MySingletonRequestQueue.getInstance(this.getActivity()).addToRequestQueue(request);

    }

    public void handleDeleteEvent(int deleteEventId) {

        String url = "http://10.0.2.2:8080/api/events/";

//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url + deleteEventId, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        })
        //this is a string request because api sends a string only instead of a jsonobject or jsonarray
        StringRequest request = new StringRequest(Request.Method.DELETE, url + deleteEventId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.i("deletedis ", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
//            @Override
//            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                int statusCode = response.statusCode;
////                Log.i("statuscode", String.valueOf(statusCode));
//                if(statusCode == 200) {
//                    Log.i("deletedevent", "event delete successful");
//                }
//                else {
//                    Log.i("failedaddpet", response.toString());
//                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
//                }
//                return super.parseNetworkResponse(response);
//            }
        };


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