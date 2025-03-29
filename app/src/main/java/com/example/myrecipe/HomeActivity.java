package com.example.myrecipe;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
public class HomeActivity extends AppCompatActivity {

    Button btnHomeRecipeList, btnHomeUploadNewRecipe, btnHomeFavoriteRecipies, btnHomeAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);




        // Disable the default sound effect when the login button is clicked
        Recipe_List_Main.setSoundEffectsEnabled(false);

        // Set an OnClickListener to the login button
        Recipe_List_Main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start Login activity
                Intent intent = new Intent(HomeActivity.this, Recipe_List_Main.class); // Make sure LoginActivity is the correct class
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
                Intent intent = new Intent(HomeActivity.this, MainActivity.class); // Make sure the correct class
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
                Intent intent = new Intent(HomeActivity.this, Favorites_list.class); // Make sure LoginActivity is the correct class
                // Start the activity
                startActivity(intent);
            }
        });
    }






}