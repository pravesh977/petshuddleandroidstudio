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

import android.portfolio.petshuddle.Adapter.MyPetsAdapter;
import android.portfolio.petshuddle.Alerter.Alerter;
import android.portfolio.petshuddle.Entity.Pet;
import android.portfolio.petshuddle.Helper.MySingletonRequestQueue;
import android.portfolio.petshuddle.UI.AddNewPetScreen;
import android.portfolio.petshuddle.UI.MainTabbedActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.portfolio.petshuddle.R;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPetsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPetsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //    private TextView myPetsTextView;
    private Boolean isAddButtonVisible;
    private FloatingActionButton floatingActionButtonPet;
    private RecyclerView myPetsRecyclerView;
    private Button addPetButton;
    private MyPetsAdapter myPetsAdapter;
    List<Pet> myPetsList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    public MyPetsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPetsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPetsFragment newInstance(String param1, String param2) {
        MyPetsFragment fragment = new MyPetsFragment();
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
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        isAddButtonVisible = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_pets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //myPetsTextView = view.findViewById(R.id.myPetsTextView);

        myPetsRecyclerView = view.findViewById(R.id.myPetsRecyclerView);
        floatingActionButtonPet = view.findViewById(R.id.floatingActionButtonPet);
        floatingActionButtonPet.setOnClickListener(toggleAddPetButton);

        addPetButton = view.findViewById(R.id.addPetButton);
        addPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddNewPetScreen.class));
            }
        });
//        Transition transition = new Fade();
//        transition.setDuration(600);
//        transition.addTarget(R.id.addPetButton);
//        TransitionManager.beginDelayedTransition(myPetsRecyclerView, transition);

    }

    @Override
    public void onStart() {
        super.onStart();
        myPetsList.clear();
//        List<Pet> myPetsList = new ArrayList<>();
//        final MyPetsAdapter myPetsAdapter;
//        Log.i("activitystarted", "activity has started");


        // Instantiate the RequestQueue.
        //RequestQueue queue = MySingletonRequestQueue.getInstance(this.getActivity()).getRequestQueue();
        String url = "http://10.0.2.2:8080/api/petshuddle/userid/";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url + currentUser.getUid(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                myPetsTextView.setText(response.toString());

                //for loop works to get each name
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
                        myPetsList.add(jsonPet);
//                        Log.i("petname is: ", jsonPet.getPetName());
//                        Log.i("pets list increment: ", String.valueOf(myPetsList.size()));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                myPetsAdapter = new MyPetsAdapter(myPetsList, getContext());
                myPetsRecyclerView.setAdapter(myPetsAdapter);
                myPetsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                Log.i("arraysize : ", String.valueOf(myPetsList.size()));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        MySingletonRequestQueue.getInstance(this.getActivity()).addToRequestQueue(request);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

//            @Override
//            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//                View itemView = viewHolder.itemView;
//                itemView.setBackgroundColor(Color.parseColor("#0B059E"));
//            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Log.i("moved", "it is moving");
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //implement alert and delete from database
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(getActivity());
                aBuilder.setTitle("Delete this pet?");
                aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int position = viewHolder.getAdapterPosition();
//                myPetsList.remove(position);
                        Pet currentPet = myPetsList.get(position);
                        handleDeletePet(currentPet.getPetId());
                        myPetsList.remove(currentPet);
                        myPetsAdapter.notifyItemRemoved(position); //out of bounds
//                        Toast.makeText(getContext(), "Pet Deleted", Toast.LENGTH_LONG).show();
                        Snackbar.make(myPetsRecyclerView,"Deleted Pet " + currentPet.getPetName(), Snackbar.LENGTH_LONG).show();

//                myPetsAdapter.notifyDataSetChanged();
                        //myPetsAdapter.notifyDataSetChanged();
                        //Log.i("swiped", "swiped for start");
//                        Log.i("arraysizeafterdelete : ", String.valueOf(myPetsList.size()));
//                        Log.i("arrayadaptersize", String.valueOf(myPetsAdapter.getItemCount()));
                    }
                });
                aBuilder.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                    //notifies the adapter of the item change which somehow puts the item back if no is clicked
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int position = viewHolder.getAdapterPosition();
                        myPetsAdapter.notifyItemChanged(position);
                    }
                });
                aBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        int position = viewHolder.getAdapterPosition();
                        myPetsAdapter.notifyItemChanged(position);
                    }
                });
                AlertDialog deleteDialog = aBuilder.create();
                //deleteDialog.setCanceledOnTouchOutside(false);
                deleteDialog.show();
            }
        }).attachToRecyclerView(myPetsRecyclerView);
    }

    public void handleDeletePet(int deletePetId) {

        String url = "http://10.0.2.2:8080/api/petshuddle/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url + deletePetId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.i("responsedelete", response.toString());
//                Toast.makeText(getContext(), "Pet Deleted", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });


        MySingletonRequestQueue.getInstance(this.getActivity()).addToRequestQueue(request);

    }

    private View.OnClickListener toggleAddPetButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isAddButtonVisible == false) {
                addPetButton.setVisibility(View.VISIBLE);
                isAddButtonVisible = true;
            }
            else {
                addPetButton.setVisibility(View.GONE);
                isAddButtonVisible = false;
            }
        }
    };

    //removes the button if the user nagivates away from the screen while
    //the button is visible
    @Override
    public void onPause() {
        super.onPause();
        isAddButtonVisible = false;
        addPetButton.setVisibility(View.GONE);
    }

    public void changeList() {
        myPetsAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Snackbar.make(myPetsRecyclerView,"this is pets snackbar", Snackbar.LENGTH_LONG).show();
//    }
}