package android.portfolio.petshuddle.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.portfolio.petshuddle.R;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SinglePetScreen extends AppCompatActivity {

    private int petId;
    private String petName;
    private String species;
    private String sex;
    private String breed;
    private int age;
    private String petDescription;
    private String userId;

    private TextView editTextPetId;
    private EditText editTextPetName;
    private EditText editTextPetSpecies;
    private EditText editTextPetSex;
    private EditText editTextPetBreed;
    private EditText editTextPetAge;
    private EditText editTextPetDescription;
    private EditText editTextPetUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_pet_screen);


        //getting values from the adapter of the selected pet viewholder
        int petId = getIntent().getIntExtra("petId", -1);
        String petName = getIntent().getStringExtra("petName");
        String species = getIntent().getStringExtra("species");
        String sex = getIntent().getStringExtra("sex");
        String breed = getIntent().getStringExtra("breed");
        int age = getIntent().getIntExtra("age", -1);
        String petDescription = getIntent().getStringExtra("description");
        String userId = getIntent().getStringExtra("userId");


        editTextPetId = findViewById(R.id.editTextPetId);
        editTextPetName = findViewById(R.id.editTextPetName);
        editTextPetSpecies = findViewById(R.id.editTextPetSpecies);
        editTextPetSex = findViewById(R.id.editTextPetSex);
        editTextPetBreed = findViewById(R.id.editTextPetBreed);
        editTextPetAge = findViewById(R.id.editTextPetAge);
        editTextPetDescription = findViewById(R.id.editTextPetDescription);
        editTextPetUserId = findViewById(R.id.editTextPetUserId);

        //setting edit texts to the appropriate passed values
        editTextPetId.setText(String.valueOf(petId));
        editTextPetName.setText(petName);
        editTextPetSpecies.setText(species);
        editTextPetSex.setText(sex);
        editTextPetBreed.setText(breed);
        editTextPetAge.setText(String.valueOf(age));
        editTextPetDescription.setText(petDescription);
        editTextPetUserId.setText(userId);
    }

    public void handleCancelEditPet(View view) {
        this.finish();
    }


}