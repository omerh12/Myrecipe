package com.example.myrecipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences cookingPref;
    SharedPreferences favoritesPref;
    String COOKING_PREF = "cooking_recipes";
    String FAVORITES_PREF = "favorite_recipes";

    TextView tvRecipeCurrentlyCookingRecipeName, tvRecipeNoCurrentlyCookingRecipeName;
    ImageView recipeCurrentlyCookingImageView;
    static Recipe currentCookingRecipe;
    static ArrayList <Recipe>favoriterecipes;

    private ViewPager2 favoriteViewPager;
    private List<Recipe> favoriteRecipes = new ArrayList<>();
    private FavoriteRecipeAdapter favoriteAdapter;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //loadCurrentCookingView();
        //loadFavoriteRecipesView();
    }
private void loadFavoriteRecipesView(){

    favoritesPref = getSharedPreferences(FAVORITES_PREF, MODE_PRIVATE);

    Set<String> favoriteRecipeNames = favoritesPref.getStringSet("favorites", new HashSet<>());
    getFavoriteRecipesByNames(favoriteRecipeNames, new OnRecipesLoadedListener() {
        @Override
        public void onRecipesLoaded(ArrayList<Recipe> recipes) {
            try {
                // Do something with the list of recipes
                // For example, update the adapter data
                favoriteRecipes.clear();
                if (recipes != null) {
                    favoriteRecipes.addAll(recipes);
                }

                favoriteAdapter = new FavoriteRecipeAdapter(favoriteRecipes, HomeActivity.this);

            }
            catch(Exception e)
            {
                Log.e("YourTag", "Error in riskyOperation", e);
                e.printStackTrace();
            }

            favoriteViewPager = findViewById(R.id.favoriteViewPager);
            favoriteViewPager.setAdapter(favoriteAdapter);
        }
    });


}
    @Override
    protected void onResume() {
        super.onResume();
        loadCurrentCookingView();
        loadFavoriteRecipesView();
    }

    private void loadCurrentCookingView() {
        tvRecipeCurrentlyCookingRecipeName = findViewById(R.id.tvRecipeCurrentlyCookingRecipeName);
        tvRecipeNoCurrentlyCookingRecipeName = findViewById(R.id.tvRecipeNoCurrentlyCookingRecipeName);
        recipeCurrentlyCookingImageView = findViewById(R.id.recipeCurrentlyCookingImageView);

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

        // Load currently cooking recipe
        cookingPref = getSharedPreferences(COOKING_PREF, MODE_PRIVATE);
        String currentlyCookingRecipe = cookingPref.getString("cookingRecipe", "");
        getCurrentCookingRecipeByName(currentlyCookingRecipe, new OnRecipeLoadedListener() {
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


//DAL
    public interface OnRecipeLoadedListener {
        void onRecipeLoaded(Recipe recipe);
    }
    public interface OnRecipesLoadedListener {
        void onRecipesLoaded(ArrayList<Recipe> recipes);
    }
    public void getFavoriteRecipesByNames(Set<String>recipesNames, OnRecipesLoadedListener listener){
        DatabaseReference recipesRef = FirebaseDatabase.getInstance().getReference("recipes");
        recipesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favoriterecipes = new ArrayList<>();
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    String recipeName = recipeSnapshot.child("name").getValue(String.class);
                    if (recipesNames.contains(recipeName)) {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        favoriterecipes.add(recipe);
                    }
                }
                listener.onRecipesLoaded(favoriterecipes);
                // matchingRecipes now contains all recipes with names in nameSet
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onRecipesLoaded(null);// Handle error
            }
        });

    }

    public void getCurrentCookingRecipeByName(String recipeName, OnRecipeLoadedListener listener) {
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



    //Menu
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
        }
        else if (itemId == R.id.menu_item_home) {
            Intent intent = new Intent(this, HomeActivity.class);
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
    //Menu
}
//DAL

