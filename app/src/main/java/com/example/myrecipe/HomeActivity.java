package com.example.myrecipe;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Map;
import java.util.LinkedHashMap;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences cookingPref;
    String FAVORITES_PREF = "favorite_recipes";
    String COOKING_PREF = "cooking_recipes";

    TextView tvRecipeCurrentlyCookingRecipeName, tvRecipeNoCurrentlyCookingRecipeName, tvHomeFavoriteRecipes;
    ImageView recipeCurrentlyCookingImageView, ivHomeCurrentlyFavoriteRecipesImage1, ivHomeCurrentlyFavoriteRecipesImage2;

    Map<String, String> lastTwoFavorites = new LinkedHashMap<>();
    static Recipe currentCookingRecipe;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvHomeFavoriteRecipes = findViewById(R.id.tvHomeFavoriteRecipes);
        tvRecipeCurrentlyCookingRecipeName = findViewById(R.id.tvRecipeCurrentlyCookingRecipeName);
        tvRecipeNoCurrentlyCookingRecipeName = findViewById(R.id.tvRecipeNoCurrentlyCookingRecipeName);
        recipeCurrentlyCookingImageView = findViewById(R.id.recipeCurrentlyCookingImageView);
        ivHomeCurrentlyFavoriteRecipesImage1 = findViewById(R.id.ivHomeCurrentlyFavoriteRecipesImage1);
        ivHomeCurrentlyFavoriteRecipesImage2 = findViewById(R.id.ivHomeCurrentlyFavoriteRecipesImage2);

        prefs = getSharedPreferences(FAVORITES_PREF, MODE_PRIVATE);
        cookingPref = getSharedPreferences(COOKING_PREF, MODE_PRIVATE);

        tvRecipeCurrentlyCookingRecipeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, RecipeView.class)
                        .putExtra("recipeName", currentCookingRecipe.getName())
                        .putExtra("recipeIngredients", currentCookingRecipe.getIngredients())
                        .putExtra("recipeInstructions", currentCookingRecipe.getInstructions())
                        .putExtra("imagePath", currentCookingRecipe.getImage());

                startActivity(intent);
                finish();
            }
        });
        recipeCurrentlyCookingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, RecipeView.class)
                        .putExtra("recipeName", currentCookingRecipe.getName())
                        .putExtra("recipeIngredients", currentCookingRecipe.getIngredients())
                        .putExtra("recipeInstructions", currentCookingRecipe.getInstructions())
                        .putExtra("imagePath", currentCookingRecipe.getImage());

                startActivity(intent);
                finish();
            }
        });

        loadHomeScreenData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHomeScreenData();
    }

    private void loadHomeScreenData() {
        // Clear previous data
        lastTwoFavorites.clear();

        // Load the last two favorite recipes (names and image paths stored in prefs)
        for (Map.Entry<String, ?> entry : prefs.getAll().entrySet()) {
            if ((boolean) entry.getValue()) {
                String imagePath = prefs.getString(entry.getKey() + "_image", "");
                if (!imagePath.isEmpty()) {
                    lastTwoFavorites.put(entry.getKey(), imagePath);
                    if (lastTwoFavorites.size() == 2) break;
                }
            }
        }

        // Clear images first (optional, for visual effect)
        ivHomeCurrentlyFavoriteRecipesImage1.setImageDrawable(null);
        ivHomeCurrentlyFavoriteRecipesImage2.setImageDrawable(null);

        // Load images into the home screen
        int index = 0;
        ImageView[] favoriteImages = {ivHomeCurrentlyFavoriteRecipesImage1, ivHomeCurrentlyFavoriteRecipesImage2};
        for (Map.Entry<String, String> favorite : lastTwoFavorites.entrySet()) {
            ImageView targetImage = favoriteImages[index];
            Glide.with(this).load(favorite.getValue()).into(targetImage);

            String recipeName = favorite.getKey();
            String imagePath = favorite.getValue();
            targetImage.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, RecipeView.class);
                intent.putExtra("recipeName", recipeName);
                intent.putExtra("imagePath", imagePath);
                startActivity(intent);
            });
            index++;
        }

        // Optionally remove click listeners if less than 2 favorites
        if (lastTwoFavorites.size() < 2) {
            if (lastTwoFavorites.size() < 1) {
                ivHomeCurrentlyFavoriteRecipesImage1.setOnClickListener(null);
            }
            ivHomeCurrentlyFavoriteRecipesImage2.setOnClickListener(null);
        }

        // Load currently cooking recipe
        String currentlyCookingRecipe = cookingPref.getString("cookingRecipe", "");
        getRecipeByName(currentlyCookingRecipe, new OnRecipeLoadedListener() {
            @Override
            public void onRecipeLoaded(Recipe recipe) {
                if (recipe != null) {
                    String imageUrl = recipe.getImage();
                    String recipeName = recipe.getName();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imageUrl);
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        Glide.with(HomeActivity.this).load(uri).centerCrop().into(recipeCurrentlyCookingImageView);
                    });
                    tvRecipeCurrentlyCookingRecipeName.setText(recipeName);
                    tvRecipeNoCurrentlyCookingRecipeName.setVisibility(View.INVISIBLE);
                } else {
                    tvRecipeNoCurrentlyCookingRecipeName.setVisibility(View.VISIBLE);
                    recipeCurrentlyCookingImageView.setImageDrawable(null);
                    tvRecipeCurrentlyCookingRecipeName.setText("");
                }
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
        } else if (itemId == R.id.menu_item_recipe_list) {
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

    public interface OnRecipeLoadedListener {
        void onRecipeLoaded(Recipe recipe);
    }

    public void getRecipeByName(String recipeName, OnRecipeLoadedListener listener) {
        DatabaseReference recipesRef = FirebaseDatabase.getInstance().getReference("recipes");
        recipesRef.orderByChild("name").equalTo(recipeName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            currentCookingRecipe = snapshot.getValue(Recipe.class);
                            listener.onRecipeLoaded(currentCookingRecipe);
                            return;
                        }
                        listener.onRecipeLoaded(null);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onRecipeLoaded(null);
                    }
                });
    }
}
