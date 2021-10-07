package android.portfolio.petshuddle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.portfolio.petshuddle.Entity.Pet;
import android.portfolio.petshuddle.R;
import android.portfolio.petshuddle.UI.SinglePetScreen;
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Pet currentPet = myPetsList.get(position);
                    Intent intent = new Intent(context, SinglePetScreen.class);
                    intent.putExtra("petId", currentPet.getPetId());
                    intent.putExtra("petName", currentPet.getPetName());
                    intent.putExtra("species", currentPet.getSpecies());
                    intent.putExtra("sex", currentPet.getSex());
                    intent.putExtra("breed", currentPet.getBreed());
                    intent.putExtra("age", currentPet.getAge());
                    intent.putExtra("description", currentPet.getPetDescription());
                    intent.putExtra("userId", currentPet.getUserId());
                    context.startActivity(intent);
//                    Log.i("clicked pet", currentPet.getPetName());

                }
            });
        }
    }

//    termItemView = itemView.findViewById(R.id.termTextView);
//            itemView.setOnClickListener(new View.OnClickListener(){
//        @Override
//        public void onClick(View v) {
//            int position = getAdapterPosition();
//            final TermEntity current = mTerms.get(position);
//            Intent intent = new Intent(context, CoursesActivity.class);
//            intent.putExtra("termId", current.getTermId());
//            intent.putExtra("termTitle", current.getTermTitle());
//            intent.putExtra("termStart", current.getTermStart());
//            intent.putExtra("termEnd", current.getTermEnd());
//            context.startActivity(intent);
////                    Log.i("termclicked", "term is being clicked");
////                    Log.i("thisterm",String.valueOf(current.getTermId()));
//        }
//    });

    private List<Pet> myPetsList;
    private Context context;

    public MyPetsAdapter(List<Pet> pets, Context context) {
        this.myPetsList = pets;
        this.context = context;
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
