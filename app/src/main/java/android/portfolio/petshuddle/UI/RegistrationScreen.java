package android.portfolio.petshuddle.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.portfolio.petshuddle.Entity.User;
import android.portfolio.petshuddle.R;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationScreen extends AppCompatActivity {

    private EditText registerNameEditText;
    private EditText registerEmailEditText;
    private EditText registerPasswordEditText;
    private ProgressBar registerProgressBar;
    private FirebaseDatabase mDatabase;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);

        registerNameEditText = findViewById(R.id.registerNameEditText);
        registerEmailEditText = findViewById(R.id.registerEmailEditText);
        registerPasswordEditText = findViewById(R.id.registerPasswordEditText);
        registerProgressBar = findViewById(R.id.registerProgressBar);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    //returns users back to the login screen if they do not want to create new user
    public void backToLoginScreen(View view) {
        this.finish();
    }

    //method that handles the register button
    public void registerButtonPressed(View view) {
        String name = registerNameEditText.getText().toString().trim();
        String email = registerEmailEditText.getText().toString().trim();
        String password = registerPasswordEditText.getText().toString().trim();

        if (name.isEmpty()) {
            registerNameEditText.setError("Name can't be empty");
            registerNameEditText.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            registerEmailEditText.setError("Email can't be empty");
            registerEmailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registerEmailEditText.setError("Enter Valid Email");
            registerEmailEditText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            registerPasswordEditText.setError("Password can't be empty");
            registerPasswordEditText.requestFocus();
            return;
        }
        if (password.length() < 6) {
            registerPasswordEditText.setError("Password needs to be longer than 6 letters");
            registerPasswordEditText.requestFocus();
            return;
        }
        //condition if all field validations pass
        registerProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Log.i("registerpass", "Created a new user");

                            //getting the current user which has just been created
                            FirebaseUser currentUser = mAuth.getCurrentUser();

                            String currentUserId = currentUser.getUid();

                            //creating a new User object with the textfield values
                            User newUser = new User(name, email);
                            mDatabase.getReference().child(currentUserId).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //Log.i("savedindatabase", "user with email is saved: " + newUser.getEmail());
                                    } else {
                                        //Log.i("usernotsaved", "could not save user to database " + task.getException());
                                        registerProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                            Toast.makeText(RegistrationScreen.this, "New User Created for " + currentUser.getEmail(), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegistrationScreen.this, MainTabbedActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.i("registerfailed", "Creation of new user failed", task.getException());
                            Toast.makeText(RegistrationScreen.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            registerProgressBar.setVisibility(View.GONE);
                        }

                    }
                });
    }
}