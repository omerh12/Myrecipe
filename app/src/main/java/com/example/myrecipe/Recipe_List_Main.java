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

public class Recipe_List_Main extends AppCompatActivity {


    ArrayList<Recipe> Recipe_List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list_main);

        Button save_recipe=findViewById(R.id.save_recipe);
        EditText name=findViewById(R.id.recipe_name);
        EditText ingredients=findViewById(R.id.recipe_ingredients);
        EditText instructions=findViewById(R.id.recipe_instructions);

        Recipe_List=new ArrayList<Recipe>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference RecipeRef=database.getReference("recipes");

                          save_recipe.setOnClickListener(new View.OnClickListener() {
                            @Override
                             public void onClick(View v) {
                                  // Get the values entered by the user
                               String recipeName = name.getText().toString().trim();
                                 String recipeIngredients = ingredients.getText().toString().trim();
                                  String recipeInstructions = instructions.getText().toString().trim();

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
                                                Intent intent = new Intent(Recipe_List_Main.this, Home.class);
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