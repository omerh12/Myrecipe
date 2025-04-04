package com.example.myrecipe;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class UploadNewRecipeActivity extends AppCompatActivity {


    ArrayList<Recipe> Recipe_List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new_recipe);

        Button save_recipe=findViewById(R.id.btnUploadNewRecipeSaveRecipe);
        EditText etUploadNewRecipeName=findViewById(R.id.etUploadNewRecipeName);
        EditText etUploadNewRecipeIngredients=findViewById(R.id.etUploadNewRecipeIngredients);
        EditText etUploadNewRecipeInstructions=findViewById(R.id.etUploadNewRecipeInstructions);

        Recipe_List=new ArrayList<Recipe>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference RecipeRef=database.getReference("recipes");

                          save_recipe.setOnClickListener(new View.OnClickListener() {
                            @Override
                             public void onClick(View v) {
                                  // Get the values entered by the user
                               String recipeName = etUploadNewRecipeName.getText().toString().trim();
                                 String recipeIngredients = etUploadNewRecipeIngredients.getText().toString().trim();
                                  String recipeInstructions = etUploadNewRecipeInstructions.getText().toString().trim();

                                     // Check if any field is empty
                                   if (recipeName.isEmpty() || recipeIngredients.isEmpty() || recipeInstructions.isEmpty()) {
                                       Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();

                                   } else {
                                     // Create a new recipe object
                                       Recipe newRecipe = new Recipe(recipeName, recipeIngredients, recipeInstructions);

                                           // Generate a unique ID for the new recipe
                                        String recipeId = RecipeRef.push().getKey();
                                        Recipe_List.add(newRecipe);

                                           if (recipeId != null) {
                                             // Save the recipe to Firebase
                                            RecipeRef.child(recipeId).setValue(newRecipe)
                                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override

                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                 // Successfully saved
                                                Toast.makeText(getApplicationContext(), "Recipe saved!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(UploadNewRecipeActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                 } else {
                                                // Error occurred
                                                Toast.makeText(getApplicationContext(), "Failed to save recipe", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }

            }
        });
}

}