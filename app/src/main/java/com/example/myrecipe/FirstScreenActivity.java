package com.example.myrecipe;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class FirstScreenActivity extends AppCompatActivity {

    // Declare buttons
    Button btnFirstScreenSignUp,btnFirstScreenLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        // Initialize the registration button
        btnFirstScreenSignUp = findViewById(R.id.btnFirstScreenSignUp);


        // Set an OnClickListener to the registration button
        btnFirstScreenSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start Registration activity
                Intent intent = new Intent(FirstScreenActivity.this, Registration.class);
                // Start the activity
                startActivity(intent);
            }
        });

        // Initialize the login button
        btnFirstScreenLogIn = findViewById(R.id.btnFirstScreenLogIn); // Make sure the ID matches the button in your XML

        // Set an OnClickListener to the login button
        btnFirstScreenLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start Login activity
                Intent intent = new Intent(FirstScreenActivity.this, LogInActivity.class); // Make sure LoginActivity is the correct class
                // Start the activity
                startActivity(intent);
            }
        });
    }
}
