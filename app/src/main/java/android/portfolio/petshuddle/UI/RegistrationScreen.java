package android.portfolio.petshuddle.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.portfolio.petshuddle.R;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

public class RegistrationScreen extends AppCompatActivity {

    private EditText registerNameEditText;
    private EditText registerEmailEditText;
    private EditText registerPasswordEditText;
    private ProgressBar registerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);

        registerNameEditText = findViewById(R.id.registerNameEditText);
        registerEmailEditText = findViewById(R.id.registerEmailEditText);
        registerPasswordEditText = findViewById(R.id.registerPasswordEditText);
        registerProgressBar = findViewById(R.id.registerProgressBar);

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

        if(name.isEmpty()) {
            registerNameEditText.setError("Name can't be empty");
            registerNameEditText.requestFocus();
            return;
        }
        if(email.isEmpty()) {
            registerEmailEditText.setError("Email can't be empty");
            registerEmailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registerEmailEditText.setError("Enter Valid Email");
            registerEmailEditText.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            registerPasswordEditText.setError("Password can't be empty");
            registerPasswordEditText.requestFocus();
            return;
        }
        if(password.length() < 6) {
            registerPasswordEditText.setError("Password needs to be longer than 6 letters");
            registerPasswordEditText.requestFocus();
            return;
        }
        registerProgressBar.setVisibility(View.VISIBLE);
    }
}