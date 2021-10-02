package android.portfolio.petshuddle.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.portfolio.petshuddle.R;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.regex.Pattern;

public class LoginScreen extends AppCompatActivity {

    private TextView registerTextView;
    private EditText loginEmailEditText;
    private EditText loginPasswordEditText;
    private ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        registerTextView = findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(openRegisterScreen);

        loginEmailEditText = findViewById(R.id.loginEmailEditText);
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText);

        loginProgressBar = findViewById(R.id.loginProgressBar);

    }

    //returns user to the main welcome screen if the user does not want to login
    public void backButtonPressed(View view) {
        this.finish();
    }

    //takes users to the registration screen if they click on register TextView
    public View.OnClickListener openRegisterScreen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(LoginScreen.this, RegistrationScreen.class));
        }
    };

    //method that handles the login button press
    public void loginButtonPressed(View view) {
        String email = loginEmailEditText.getText().toString().trim();
        String password = loginPasswordEditText.getText().toString().trim();
        //Log.i("emailpass", email + " " + password);

        if(email.isEmpty()) {
            loginEmailEditText.setError("Email can't be empty");
            loginEmailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmailEditText.setError("Enter Valid Email");
            loginEmailEditText.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            loginPasswordEditText.setError("Password can't be empty");
            loginPasswordEditText.requestFocus();
            return;
        }
        if(password.length() < 6) {
            loginPasswordEditText.setError("Password needs to be longer than 6 letters");
            loginPasswordEditText.requestFocus();
            return;
        }
        loginProgressBar.setVisibility(View.VISIBLE);
    }
}
