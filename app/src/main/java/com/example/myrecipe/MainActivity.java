package com.example.myrecipe;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Declare buttons
    Button reg_button;
    Button LogIn_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the registration button
        reg_button = findViewById(R.id.reg_button);

        // Disable the default sound effect when the registration button is clicked
        reg_button.setSoundEffectsEnabled(false);

        // Set an OnClickListener to the registration button
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start Registration activity
                Intent intent = new Intent(MainActivity.this, Registration.class);
                // Start the activity
                startActivity(intent);
            }
        });

        // Initialize the login button
        LogIn_button = findViewById(R.id.LogIn_button); // Make sure the ID matches the button in your XML

        // Disable the default sound effect when the login button is clicked
        LogIn_button.setSoundEffectsEnabled(false);

        // Set an OnClickListener to the login button
        LogIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start Login activity
                Intent intent = new Intent(MainActivity.this, SignIn.class); // Make sure LoginActivity is the correct class
                // Start the activity
                startActivity(intent);
            }
        });
    }
}
