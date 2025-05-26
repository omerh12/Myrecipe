package com.example.myrecipe;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import androidx.appcompat.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    Button btnHomeRecipeList, btnHomeUploadNewRecipe, btnHomeFavoriteRecipies, btnHomeAlarm, btnChatWithAI;
    Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.homeToolbar);
        btnHomeRecipeList = findViewById(R.id.btnHomeRecipeList);
        btnHomeAlarm = findViewById(R.id.btnHomeAlarm);
        btnHomeFavoriteRecipies = findViewById(R.id.btnHomeFavoriteRecipies);
        btnHomeUploadNewRecipe = findViewById(R.id.btnHomeUploadNewRecipe);
        btnChatWithAI = findViewById(R.id.btnChatWithAI);


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
                Intent intent = new Intent(HomeActivity.this, FavoritesListActivity.class);
                startActivity(intent);
            }
        });

        btnHomeAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AlarmActivity.class);
                startActivity(intent);
            }
        });

        btnHomeUploadNewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UploadNewRecipeActivity.class);
                startActivity(intent);
            }
        });
        btnChatWithAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
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
        if (itemId == R.id.menu_item_about) {
            Intent intent = new Intent(this, AboutAppActivity.class);
            startActivity(intent);
            return true;

        } else if (itemId == R.id.menu_item_profile) {// Handle profile click
            Intent intent=new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;

        } else if (itemId == R.id.menu_item_logout) {// Handle logout click
            Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("user_id");         // Example key storing user ID
            editor.remove("user_email");      // Example key storing user email
            editor.remove("user_token");      // Example key storing session/token
            editor.apply();
            FirebaseAuth.getInstance().signOut();


            Intent intent= new Intent(this, FirstScreenActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}