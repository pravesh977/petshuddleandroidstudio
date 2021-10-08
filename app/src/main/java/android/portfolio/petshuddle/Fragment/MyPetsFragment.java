package android.portfolio.petshuddle.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.portfolio.petshuddle.Adapter.MyPetsAdapter;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

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
    private RecyclerView myPetsRecyclerView;
    private Button addPetButton;
    private MyPetsAdapter myPetsAdapter;

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
        List<Pet> myPetsList = new ArrayList<>();
        Log.i("activitycreated", "this is being created again");

        // Instantiate the RequestQueue.
        //RequestQueue queue = MySingletonRequestQueue.getInstance(this.getActivity()).getRequestQueue();
        String url ="http://10.0.2.2:8080/api/petshuddle";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                myPetsTextView.setText(response.toString());

                //for loop works to get each name
                for(int i = 0; i < response.length(); i++) {
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
//                for(Pet element:myPetsList) {
//                    Log.i("petobjectnameis :", element.getPetName());
//                    Log.i("petobjectsexis :", element.getSex());
//
//                }
//                Log.i("size of array is :", String.valueOf(myPetsList.size()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        MySingletonRequestQueue.getInstance(this.getActivity()).addToRequestQueue(request);

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

        addPetButton = view.findViewById(R.id.addPetButton);
        addPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddNewPetScreen.class));
            }
        });

//        String petNamepassed = getActivity().getIntent().getStringExtra("petName");
//        Log.i("passedpetis: ", "petNamepassed");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("activitystarted", "activity has started");


        List<Pet> myPetsList = new ArrayList<>();

        // Instantiate the RequestQueue.
        //RequestQueue queue = MySingletonRequestQueue.getInstance(this.getActivity()).getRequestQueue();
        String url ="http://10.0.2.2:8080/api/petshuddle";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                myPetsTextView.setText(response.toString());

                //for loop works to get each name
                for(int i = 0; i < response.length(); i++) {
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
//                for(Pet element:myPetsList) {
//                    Log.i("petobjectnameis :", element.getPetName());
//                    Log.i("petobjectsexis :", element.getSex());
//
//                }
//                Log.i("size of array is :", String.valueOf(myPetsList.size()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        MySingletonRequestQueue.getInstance(this.getActivity()).addToRequestQueue(request);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("activityresumed", "activity is resumed");
    }

//    public void addNewPetToList(Pet passedNewPet) {
//        myPetsList.add(passedNewPet);
//        myPetsAdapter.notifyItemInserted(0);
//    }

    public void changeList() {
        myPetsAdapter.notifyDataSetChanged();
    }

}