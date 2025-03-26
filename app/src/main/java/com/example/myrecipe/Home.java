package com.example.myrecipe;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.TextView;
public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView textview11= findViewById(R.id.textView11);
        Button Recipe_List_Main=findViewById(R.id.Search);
        Button Log_out=findViewById(R.id.Log_out);
        Button go_to_favorites=findViewById(R.id.go_to_favorites);

        // Disable the default sound effect when the login button is clicked
        Recipe_List_Main.setSoundEffectsEnabled(false);

        // Set an OnClickListener to the login button
        Recipe_List_Main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start Login activity
                Intent intent = new Intent(Home.this, Recipe_List_Main.class); // Make sure LoginActivity is the correct class
                // Start the activity
                startActivity(intent);
            }
        });

        // Disable the default sound effect when the login button is clicked
        Log_out.setSoundEffectsEnabled(false);

        // Set an OnClickListener to the button
        Log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start Login activity
                Intent intent = new Intent(Home.this, MainActivity.class); // Make sure the correct class
                // Start the activity
                startActivity(intent);
            }
        });

        // Disable the default sound effect when the login button is clicked
        go_to_favorites.setSoundEffectsEnabled(false);

        // Set an OnClickListener to the login button
        go_to_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start Login activity
                Intent intent = new Intent(Home.this, Favorites_list.class); // Make sure LoginActivity is the correct class
                // Start the activity
                startActivity(intent);
            }
        });
    }






}