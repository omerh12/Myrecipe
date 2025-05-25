package com.example.myrecipe;
import android.content.SharedPreferences;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.widget.ImageView;
import android.widget.TextView;


public class RecipeExample extends AppCompatActivity {

    TextView tvRecipeExampleRecipeName, tvRecipeExampleRecipeIngredients, tvRecipeExampleRecipeInstructions;
    ImageView ivRecipeExampleRecipeImage, ivFavorite;

    SharedPreferences prefs;
    boolean isFavorite = false;
    String FAVORITES_PREF = "favorite_recipes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_example);

        tvRecipeExampleRecipeName = findViewById(R.id.tvRecipeExampleRecipeName);
        tvRecipeExampleRecipeIngredients = findViewById(R.id.tvRecipeExampleRecipeIngredients);
        tvRecipeExampleRecipeInstructions = findViewById(R.id.tvRecipeExampleRecipeInstructions);
        ivRecipeExampleRecipeImage = findViewById(R.id.ivRecipeExampleRecipeImage);
        ivFavorite = findViewById(R.id.ivFavorite);

        prefs = getSharedPreferences(FAVORITES_PREF, MODE_PRIVATE);

        String recipeName = getIntent().getStringExtra("recipeName");
        String recipeIngredients = getIntent().getStringExtra("recipeIngredients");
        String recipeInstructions = getIntent().getStringExtra("recipeInstructions");
        String imagePath = getIntent().getStringExtra("imagePath");

        tvRecipeExampleRecipeName.setText(recipeName);
        tvRecipeExampleRecipeIngredients.setText(recipeIngredients);
        tvRecipeExampleRecipeInstructions.setText(recipeInstructions);

        String[] ingredientsArray = recipeIngredients.split(",");
        StringBuilder formattedIngredients = new StringBuilder();
        for (String ingredient : ingredientsArray) {
            formattedIngredients.append("• ").append(ingredient.trim()).append("\n");
        }

        tvRecipeExampleRecipeIngredients.setText(formattedIngredients.toString());

        isFavorite = prefs.getBoolean(recipeName, false);
        ivFavorite.setImageResource(isFavorite ? R.drawable.baseline_star_24 : R.drawable.baseline_star_border_24);



        if (imagePath != null && !imagePath.isEmpty()) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imagePath);
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                Glide.with(this)
                        .load(uri) // if imagePath is a full URL, or load from storage again if not
                        .centerCrop()
                        .into(ivRecipeExampleRecipeImage);
            });
        }

        }

    }