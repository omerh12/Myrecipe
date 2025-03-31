package com.example.myrecipe;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button btnHomeRecipeList, btnHomeUploadNewRecipe, btnHomeFavoriteRecipies, btnHomeAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

     btnHomeRecipeList=findViewById(R.id.btnHomeRecipeList);
     btnHomeAlarm=findViewById(R.id.btnHomeAlarm);
     btnHomeFavoriteRecipies=findViewById(R.id.btnHomeFavoriteRecipies);
     btnHomeUploadNewRecipe=findViewById(R.id.btnHomeUploadNewRecipe);


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
                Intent intent = new Intent(HomeActivity.this,UploadNewRecipeActivity .class); // Make sure LoginActivity is the correct class
                startActivity(intent);
            }
        });

    }






}