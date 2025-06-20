package com.example.myrecipe;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UploadNewRecipeActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnSelectImage, btnTakeImage, save_recipe;
    ImageView ivRecipeImage;
    ActivityResultLauncher<Intent> galleryLauncher;
    StorageReference storageRef;
    DatabaseReference recipeRef;
    EditText etUploadNewRecipeName;
    EditText etUploadNewRecipeIngredients;
    EditText etUploadNewRecipeInstructions;
    Uri selectedImageUri;
    Uri photoUri;
    ActivityResultLauncher<Intent> cameraLauncher;
    FirebaseDatabase database;
    String authorUid;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new_recipe);

        save_recipe = findViewById(R.id.btnUploadNewRecipeSaveRecipe);
        etUploadNewRecipeName = findViewById(R.id.etUploadNewRecipeName);
        etUploadNewRecipeIngredients = findViewById(R.id.etUploadNewRecipeIngredients);
        etUploadNewRecipeInstructions = findViewById(R.id.etUploadNewRecipeInstructions);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnTakeImage = findViewById(R.id.btnTakeImage);
        ivRecipeImage = findViewById(R.id.ivUploadNewRecipeImage);

        authorUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // הגדרת מאזינים לכפתורים
        btnSelectImage.setOnClickListener(this);
        btnTakeImage.setOnClickListener(this);

        // הרשמה לתוצאה של פתיחת מצלמה
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        ivRecipeImage.setImageURI(photoUri);
                    } else {
                        Toast.makeText(this, "Photo taking cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

        // הרשמה לתוצאה של פתיחת גלריה
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        ivRecipeImage.setImageURI(selectedImageUri);
                    }
                });

        database = FirebaseDatabase.getInstance();  // קישור לפיירבייס
        recipeRef = database.getReference("recipes");// הפניה ל- "recipes"
        storageRef = FirebaseStorage.getInstance().getReference("image");// הפניה לתיקיית "image" בענן

        // לחיצה על כפתור שמירה תפעיל בדיקה ואז תעלה תמונה ותשמור מתכון
        save_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipeName = etUploadNewRecipeName.getText().toString().trim();
                String recipeIngredients = etUploadNewRecipeIngredients.getText().toString().trim();
                String recipeInstructions = etUploadNewRecipeInstructions.getText().toString().trim();

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
            openGallery(); // פתיחת גלריה
        } else if (view == btnTakeImage) {
            launchCamera();// פתיחת מצלמה
        }
    }

    private void launchCamera() {
        // מייצר נתונים בסיסיים לתמונה
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Photo");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");

        // מוסיף מקום לתמונה חדשה בגלריה
        photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // פתיחת מצלמה עם המקום לתמונה החדשה
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

        cameraLauncher.launch(cameraIntent);
    }

    private void openGallery() {
        // פתיחת גלריה כדי לבחור תמונה
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private void uploadImageAndSaveRecipe() {
        String filename = System.currentTimeMillis() + ".jpg";
        StorageReference fileRef = storageRef.child(filename);// קישור לקובץ באחסון

        fileRef.putFile(selectedImageUri)  // העלאת הקובץ ואחר כך שמירת הנתונים בפיירבייס
                .addOnSuccessListener(taskSnapshot ->
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            saveRecipeData("image/" + filename);// שמירה עם הנתיב של התמונה
                        }))
                .addOnFailureListener(e ->
                        Toast.makeText(UploadNewRecipeActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show());
    }

    private void saveRecipeData(String imageUrl) {
        // לוקח את הערכים מהשדות
        String name = etUploadNewRecipeName.getText().toString().trim();
        String ingredients = etUploadNewRecipeIngredients.getText().toString().trim();
        String instructions = etUploadNewRecipeInstructions.getText().toString().trim();

        if (name.isEmpty() || ingredients.isEmpty() || instructions.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String key = recipeRef.push().getKey();// יצירת מפתח ייחודי למתכון
        Recipe recipe = new Recipe(name, ingredients, instructions, imageUrl, authorUid);// יצירת מתכון

        recipeRef.child(key).setValue(recipe)// כאן מכניסים את המתכון לפיירבייס
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UploadNewRecipeActivity.this, "Recipe saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UploadNewRecipeActivity.this, "Error saving recipe", Toast.LENGTH_SHORT).show();
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
        } else if (itemId == R.id.menu_item_home) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_favorites_list) {
            Intent intent = new Intent(this, FavoritesListActivity.class);
            startActivity(intent);
            return true;
        }
        else if (itemId == R.id.menu_item_myRecipes_list) {
            Intent intent = new Intent(this, MyRecipesListActivity.class);
            startActivity(intent);
            return true;
        }
        else if (itemId == R.id.menu_item_recipe_list) {
            Intent intent = new Intent(this, RecipeListActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_upload_new_recipe) {
            return true;
        } else if (itemId == R.id.menu_item_chat) {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_alarm) {
            Intent intent = new Intent(this, AlarmActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_logout) {
            Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("user_id");
            editor.remove("user_email");
            editor.remove("user_token");
            editor.apply();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, FirstScreenActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}