package android.portfolio.petshuddle.Adapter;

import android.content.Context;
import android.portfolio.petshuddle.Entity.Pet;
import android.portfolio.petshuddle.Entity.User;
import android.portfolio.petshuddle.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyPetsAdapter extends RecyclerView.Adapter<MyPetsAdapter.ViewHolder>{

    //viewholder that defines what the recyclerview holds. each viewholder will hold each item in the recyclerview
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView petNameList;
        private TextView petAgeList;
        private TextView petSexList;

        public ViewHolder(View itemView) {
            super(itemView);
            petNameList = itemView.findViewById(R.id.petNameList);
            petAgeList = itemView.findViewById(R.id.petAgeList);
            petSexList = itemView.findViewById(R.id.petSexList);
        }
    }

    private List<Pet> myPetsList;

    public MyPetsAdapter(List<Pet> pets) {
        this.myPetsList = pets;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View petView = inflater.inflate(R.layout.pet_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(petView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Pet pet = myPetsList.get(position);

        TextView petName = holder.petNameList;
        petName.setText(pet.getPetName());
        TextView petAge = holder.petAgeList;
        petAge.setText(String.valueOf(pet.getAge()));
        TextView petSex = holder.petSexList;
        petSex.setText(pet.getSex());

    }

    @Override
    public int getItemCount() {
        return myPetsList.size();
    }

}
