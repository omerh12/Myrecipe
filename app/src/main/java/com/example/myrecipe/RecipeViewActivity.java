package com.example.myrecipe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashSet;
import java.util.Set;


public class RecipeViewActivity extends AppCompatActivity {

    TextView tvRecipeExampleRecipeName, tvRecipeExampleRecipeIngredients, tvRecipeExampleRecipeInstructions;
    ImageView ivRecipeExampleRecipeImage, ivFavorite;
    Button btnRecipeExampleStartCooking, btnRecipeExampleChat;

    // שמירת העדפות למתכונים אהובים ומתכון שמבשלים כרגע
    SharedPreferences favoritePrefs;
    SharedPreferences cookingPref;

    boolean isFavorite = false;// כדי לבדוק אם המתכון מועדף
    String FAVORITES_PREF = "favorite_recipes";// מפתח לשמירת מתכונים אהובים
    String COOKING_PREF = "cooking_recipes";// מפתח לשמירת מתכון שמבשלים כרגע
    String[] ingredientsArray;//מערך שמפרק את המרכיבים מרכיב מרכיב
    String uid;


    public boolean isCurrentUser(String recipeAuthorUid) {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return recipeAuthorUid.equals(currentUserUid);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        tvRecipeExampleRecipeName = findViewById(R.id.tvRecipeExampleRecipeName);
        tvRecipeExampleRecipeIngredients = findViewById(R.id.tvRecipeExampleRecipeIngredients);
        tvRecipeExampleRecipeInstructions = findViewById(R.id.tvRecipeExampleRecipeInstructions);
        ivRecipeExampleRecipeImage = findViewById(R.id.ivRecipeExampleRecipeImage);
        ivFavorite = findViewById(R.id.ivFavorite);
        btnRecipeExampleStartCooking = findViewById(R.id.btnRecipeExampleStartCooking);

        uid=FirebaseAuth.getInstance().getUid();

        // מאחסנת את רשימת המתכונים המועדפים והמבושלים בזיכרון של הטלפון
        favoritePrefs = getSharedPreferences(FAVORITES_PREF,MODE_PRIVATE);
        cookingPref = getSharedPreferences(COOKING_PREF, MODE_PRIVATE);

        // קבלת הנתונים שנשלחו מהמסך קודם
        String recipeName = getIntent().getStringExtra("recipeName");
        String recipeIngredients = getIntent().getStringExtra("recipeIngredients");
        String recipeInstructions = getIntent().getStringExtra("recipeInstructions");
        String imagePath = getIntent().getStringExtra("imagePath");
        String recipeAuthorUid = getIntent().getStringExtra("recipeAuthorUid");
        String recipeId=getIntent().getStringExtra("recipeId");

        // אם המתכון הזה מבושל כרגע - משנים את שם הכפתור
        btnRecipeExampleStartCooking.setText("Start Cooking");
        if (cookingPref.getString("cookingRecipe", "").equals(recipeName)) {
            btnRecipeExampleStartCooking.setText("Cooking it!");
        }
        // הצגת שם, מרכיבים והוראות במסך
        tvRecipeExampleRecipeName.setText(recipeName);
        tvRecipeExampleRecipeIngredients.setText(recipeIngredients);
        tvRecipeExampleRecipeInstructions.setText(recipeInstructions);

        // פורמט יפה לרשימת המרכיבים
        ingredientsArray = recipeIngredients.split(",");
        StringBuilder formattedIngredients = new StringBuilder();
        for (String ingredient : ingredientsArray) {
            formattedIngredients.append("• ").append(ingredient.trim()).append("\n");
        }

        tvRecipeExampleRecipeIngredients.setText(formattedIngredients.toString());

        // בדיקה אם המתכון כבר קיים ברשימת המועדפים
        Set<String> existingPreferences = favoritePrefs.getStringSet("favorites", new HashSet<>());
        if (existingPreferences.contains(recipeName)) {
            isFavorite = true;
        }
        ivFavorite.setImageResource(isFavorite ? R.drawable.baseline_star_24 : R.drawable.baseline_star_border_24);

        // אם יש לתמונה פאת', אז מורידים ומציגים אותה
        if (imagePath != null && !imagePath.isEmpty()) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imagePath);
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                Glide.with(this)
                        .load(uri)
                        .centerCrop()
                        .into(ivRecipeExampleRecipeImage);
            });

            // לחיצה על הכוכב תשנה אם המתכון מועדף או לא
            ivFavorite.setOnClickListener(v -> {
                isFavorite = !isFavorite;

                Set<String> originalSet = favoritePrefs.getStringSet("favorites", new HashSet<>());
                Set<String> updatedSet = new HashSet<>(originalSet);

                if (isFavorite) {
                    updatedSet.add(recipeName);
                } else {
                    updatedSet.remove(recipeName);
                }

                SharedPreferences.Editor editor = favoritePrefs.edit();
                editor.putStringSet("favorites", updatedSet);
                editor.apply(); // or commit()

                if (isFavorite) {
                    ivFavorite.setImageResource(R.drawable.baseline_star_24);
                } else {
                    ivFavorite.setImageResource(R.drawable.baseline_star_border_24);
                }
            });

            // לחיצה על כפתור "בשל" תשנה את הסטטוס של מתכון בבישול
            btnRecipeExampleStartCooking.setOnClickListener(v -> {
                if (btnRecipeExampleStartCooking.getText().equals("Cooking it!")) {
                    SharedPreferences.Editor editor = cookingPref.edit();
                    editor.clear();
                    editor.commit();
                    btnRecipeExampleStartCooking.setText("Start Cooking");
                    Toast.makeText(this, "Cook cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = cookingPref.edit();
                    editor.clear();
                    editor.putString("cookingRecipe", recipeName);
                    editor.commit();
                    btnRecipeExampleStartCooking.setText("Cooking it!");
                }
            });

        }
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
            Intent intent = new Intent(this, UploadNewRecipeActivity.class);
            startActivity(intent);
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