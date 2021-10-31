package android.portfolio.petshuddle.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.portfolio.petshuddle.Entity.Pet;
import android.portfolio.petshuddle.Helper.MySingletonRequestQueue;
import android.portfolio.petshuddle.R;
import android.portfolio.petshuddle.UI.SinglePetScreen;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RequesterAdapter extends ArrayAdapter<Pet> {

    private List<Pet> requestersList;
    private Context context;
    private int petId;

    public RequesterAdapter(Context context, List<Pet> requestersList, int petId) {
        super(context, -1, requestersList);
        this.context = context;
        this.requestersList = requestersList;
        this.petId = petId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View currentItemView, @NonNull ViewGroup parent) {
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.requester_item, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Pet currentPetPosition = getItem(position);

//        // then according to the position of the view assign the desired image for the same
//        ImageView numbersImage = currentItemView.findViewById(R.id.imageView);
//        assert currentNumberPosition != null;
//        numbersImage.setImageResource(currentNumberPosition.getNumbersImageId());

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView requesterNameTextView = currentItemView.findViewById(R.id.requesterNameTextView);
        requesterNameTextView.setText(currentPetPosition.getPetName() + ", " +currentPetPosition.getAge());

        Button requesterProfileButton= currentItemView.findViewById(R.id.requesterProfileButton);
        requesterProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), currentPetPosition.getPetName().toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, SinglePetScreen.class);
                intent.putExtra("petId", currentPetPosition.getPetId());
                intent.putExtra("petName", currentPetPosition.getPetName());
                intent.putExtra("species", currentPetPosition.getSpecies());
                intent.putExtra("sex", currentPetPosition.getSex());
                intent.putExtra("breed", currentPetPosition.getBreed());
                intent.putExtra("age", currentPetPosition.getAge());
                intent.putExtra("description", currentPetPosition.getPetDescription());
                intent.putExtra("userId", currentPetPosition.getUserId());
                context.startActivity(intent);
            }
        });

        Button requestAcceptButton = currentItemView.findViewById(R.id.requestAcceptButton);


        requestAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url ="http://10.0.2.2:8080/api/friendslist";

                JSONObject friendJson = new JSONObject();
                try {
                    friendJson.put("petId", petId);
                    friendJson.put("friendId", currentPetPosition.getPetId());
                    friendJson.put("requestStatus", 'a');
                } catch(JSONException ex) {
                    ex.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, friendJson, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requestAcceptButton.setEnabled(false);
                        requestAcceptButton.setBackgroundColor(Color.parseColor("#D3D9E4"));
                        Snackbar.make(requesterNameTextView, currentPetPosition.getPetName() + " has been added as your friend!", Snackbar.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
                {
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        int statusCode = response.statusCode;
//                Log.i("codeis", String.valueOf(statusCode));
                        if(statusCode == 201) {
                        }
                        else {
                        }
                        return super.parseNetworkResponse(response);
                    }
                };

                MySingletonRequestQueue.getInstance(getContext()).addToRequestQueue(request);
            }
        });


        // then return the recyclable view
        return currentItemView;
    }

}
