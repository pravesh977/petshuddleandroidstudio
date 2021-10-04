package android.portfolio.petshuddle.UI;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginScreen extends AppCompatActivity {

    private TextView registerTextView;
    private EditText loginEmailEditText;
    private EditText loginPasswordEditText;
    private ProgressBar loginProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        registerTextView = findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(openRegisterScreen);

        loginEmailEditText = findViewById(R.id.loginEmailEditText);
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText);

        loginProgressBar = findViewById(R.id.loginProgressBar);

        mAuth = FirebaseAuth.getInstance();

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
        //if all form validations pass
        loginProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            //Log.i("signinpassed", "user signed in " + currentUser.getEmail());
                            Toast.makeText(LoginScreen.this, "Sign In Successful for " + currentUser.getEmail(), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginScreen.this, MainTabbedActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.i("", "signInfailed", task.getException());
                            Toast.makeText(LoginScreen.this, "Authentication failed." + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            loginProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
