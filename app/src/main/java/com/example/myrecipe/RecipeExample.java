package com.example.myrecipe;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import android.widget.ImageView;
import android.widget.TextView;


public class RecipeExample extends AppCompatActivity {

    TextView tvRecipeExampleRecipeName, tvRecipeExampleRecipeIngredients, tvRecipeExampleRecipeInstructions;
    ImageView ivRecipeExampleRecipeImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_example);

        tvRecipeExampleRecipeName = findViewById(R.id.tvRecipeExampleRecipeName);
        tvRecipeExampleRecipeIngredients = findViewById(R.id.tvRecipeExampleRecipeIngredients);
        tvRecipeExampleRecipeInstructions = findViewById(R.id.tvRecipeExampleRecipeInstructions);
        ivRecipeExampleRecipeImage = findViewById(R.id.ivRecipeExampleRecipeImage);

        String recipeName = getIntent().getStringExtra("recipeName");
        String recipeIngredients = getIntent().getStringExtra("recipeIngredients");
        String recipeInstructions = getIntent().getStringExtra("recipeInstructions");
        String imagePath = getIntent().getStringExtra("imagePath");

        tvRecipeExampleRecipeName.setText(recipeName);
        tvRecipeExampleRecipeIngredients.setText(recipeIngredients);
        tvRecipeExampleRecipeInstructions.setText(recipeInstructions);


        if (imagePath != null && !imagePath.isEmpty()) {
            Glide.with(this)
                    .load(imagePath) // if imagePath is a full URL, or load from storage again if not
                    .centerCrop()
                    .into(ivRecipeExampleRecipeImage);
        }



    }
}