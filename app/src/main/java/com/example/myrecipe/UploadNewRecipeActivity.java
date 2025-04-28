package com.example.myrecipe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UploadNewRecipeActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnSelectImage;
    ImageView ivRecipeImage;
    ActivityResultLauncher<Intent> galleryLauncher;
    StorageReference storageRef;
    DatabaseReference recipeRef;
    EditText etUploadNewRecipeName;
    EditText etUploadNewRecipeIngredients;
    EditText etUploadNewRecipeInstructions;
    Uri selectedImageUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new_recipe);

        Button save_recipe = findViewById(R.id.btnUploadNewRecipeSaveRecipe);
        etUploadNewRecipeName = findViewById(R.id.etUploadNewRecipeName);
        etUploadNewRecipeIngredients = findViewById(R.id.etUploadNewRecipeIngredients);
        etUploadNewRecipeInstructions = findViewById(R.id.etUploadNewRecipeInstructions);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        ivRecipeImage = findViewById(R.id.ivRecipeImage);
        btnSelectImage.setOnClickListener(this);
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        ivRecipeImage.setImageURI(selectedImageUri);
                    }
                });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        recipeRef = database.getReference("recipes");
        storageRef = FirebaseStorage.getInstance().getReference("image");


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
                    uploadImageAndSaveRecipe();
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnSelectImage) {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }
    private void uploadImageAndSaveRecipe() {
        String filename = System.currentTimeMillis() + ".jpg";
        StorageReference fileRef = storageRef.child(filename);

        fileRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot ->
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            saveRecipeData("image/"+filename);
                        }))
                .addOnFailureListener(e ->
                        Toast.makeText(UploadNewRecipeActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show());
    }
    private void saveRecipeData(String imageUrl) {
        String name = etUploadNewRecipeName.getText().toString().trim();
        String ingredients = etUploadNewRecipeIngredients.getText().toString().trim();
        String instructions = etUploadNewRecipeInstructions.getText().toString().trim();

        if (name.isEmpty() || ingredients.isEmpty() || instructions.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String key = recipeRef.push().getKey();
        Recipe recipe = new Recipe(name, ingredients, instructions, imageUrl);

        recipeRef.child(key).setValue(recipe)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UploadNewRecipeActivity.this, "Recipe saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UploadNewRecipeActivity.this, "Error saving recipe", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}