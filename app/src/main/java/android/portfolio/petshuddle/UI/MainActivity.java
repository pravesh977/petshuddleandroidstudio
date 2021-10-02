package android.portfolio.petshuddle.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.portfolio.petshuddle.R;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToLoginOrCreateScreen(View view) {
        startActivity(new Intent(MainActivity.this, LoginScreen.class));
    }
}