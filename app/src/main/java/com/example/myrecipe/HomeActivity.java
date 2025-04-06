package com.example.myrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    Button btnHomeRecipeList, btnHomeUploadNewRecipe, btnHomeFavoriteRecipies, btnHomeAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnHomeRecipeList = findViewById(R.id.btnHomeRecipeList);
        btnHomeAlarm = findViewById(R.id.btnHomeAlarm);
        btnHomeFavoriteRecipies = findViewById(R.id.btnHomeFavoriteRecipies);
        btnHomeUploadNewRecipe = findViewById(R.id.btnHomeUploadNewRecipe);


        btnHomeRecipeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RecipeListActivity.class);
                startActivity(intent);
            }
        });


        btnHomeFavoriteRecipies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RecipeFavoriteListActivity.class); // Make sure LoginActivity is the correct class
                startActivity(intent);
            }
        });

        btnHomeAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AlarmActivity.class); // Make sure LoginActivity is the correct class
                startActivity(intent);
            }
        });

        btnHomeUploadNewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UploadNewRecipeActivity.class); // Make sure LoginActivity is the correct class
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_item_settings) {
            Intent intent = new Intent(this, AlarmActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_profile) {// Handle profile click
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_item_logout) {// Handle logout click
            Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}