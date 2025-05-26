package com.example.myrecipe;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.widget.Button;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class RecipeView extends AppCompatActivity {

    TextView tvRecipeExampleRecipeName, tvRecipeExampleRecipeIngredients, tvRecipeExampleRecipeInstructions;
    ImageView ivRecipeExampleRecipeImage, ivFavorite;
    Button btnRecipeExampleStartCooking;

    SharedPreferences favoritePrefs;
    SharedPreferences cookingPref;
    boolean isFavorite = false;
    String FAVORITES_PREF = "favorite_recipes";
    String COOKING_PREF = "cooking_recipes";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_example);

        tvRecipeExampleRecipeName = findViewById(R.id.tvRecipeExampleRecipeName);
        tvRecipeExampleRecipeIngredients = findViewById(R.id.tvRecipeExampleRecipeIngredients);
        tvRecipeExampleRecipeInstructions = findViewById(R.id.tvRecipeExampleRecipeInstructions);
        ivRecipeExampleRecipeImage = findViewById(R.id.ivRecipeExampleRecipeImage);
        ivFavorite = findViewById(R.id.ivFavorite);
        btnRecipeExampleStartCooking = findViewById(R.id.btnRecipeExampleStartCooking);

        favoritePrefs = getSharedPreferences(FAVORITES_PREF, MODE_PRIVATE);
        cookingPref = getSharedPreferences(COOKING_PREF, MODE_PRIVATE);

        String recipeName = getIntent().getStringExtra("recipeName");
        String recipeIngredients = getIntent().getStringExtra("recipeIngredients");
        String recipeInstructions = getIntent().getStringExtra("recipeInstructions");
        String imagePath = getIntent().getStringExtra("imagePath");
        btnRecipeExampleStartCooking.setText("Start Cooking");
        if(cookingPref.getString("cookingRecipe", "").equals(recipeName))
        {
            btnRecipeExampleStartCooking.setText("Cookin' it!!!");
        }

        tvRecipeExampleRecipeName.setText(recipeName);
        tvRecipeExampleRecipeIngredients.setText(recipeIngredients);
        tvRecipeExampleRecipeInstructions.setText(recipeInstructions);

        String[] ingredientsArray = recipeIngredients.split(",");
        StringBuilder formattedIngredients = new StringBuilder();
        for (String ingredient : ingredientsArray) {
            formattedIngredients.append("• ").append(ingredient.trim()).append("\n");
        }

        tvRecipeExampleRecipeIngredients.setText(formattedIngredients.toString());

        isFavorite = favoritePrefs.getBoolean(recipeName, false);
        ivFavorite.setImageResource(isFavorite ? R.drawable.baseline_star_24 : R.drawable.baseline_star_border_24);


        if (imagePath != null && !imagePath.isEmpty()) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imagePath);
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(this)
                    .load(uri) // if imagePath is a full URL, or load from storage again if not
                    .centerCrop()
                    .into(ivRecipeExampleRecipeImage);
        });

        ivFavorite.setOnClickListener(v -> {
            isFavorite = !isFavorite; // Toggle state
            // Save favorite state
            SharedPreferences.Editor editor = favoritePrefs.edit();
            editor.putBoolean(recipeName, isFavorite);
            editor.commit();


            // Update star icon
            if (isFavorite) {
                ivFavorite.setImageResource(R.drawable.baseline_star_24);
            } else {
                ivFavorite.setImageResource(R.drawable.baseline_star_border_24);
            }
        });
            btnRecipeExampleStartCooking.setOnClickListener(v->{
                if(btnRecipeExampleStartCooking.getText().equals("Cookin' it!!!")){
                    SharedPreferences.Editor editor = cookingPref.edit();
                    editor.clear();
                    editor.commit();
                    btnRecipeExampleStartCooking.setText("Start Cooking");
                    Toast.makeText(this, "Cook cancelled", Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences.Editor editor = cookingPref.edit();
                    editor.clear();
                    editor.putString("cookingRecipe", recipeName);
                    editor.commit();
                    btnRecipeExampleStartCooking.setText("Cookin' it!!!");
                }
            });

            }

        }

    }