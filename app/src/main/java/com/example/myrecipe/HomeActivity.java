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

    SharedPreferences cookingPref;// עדיפויות לאחסון מתכון בבישול כרגע
    SharedPreferences favoritesPref;// עדיפויות לאחסון מועדפים
    String COOKING_PREF = "cooking_recipes";// שם הקובץ לבישולים
    String FAVORITES_PREF = "favorite_recipes";// שם הקובץ למועדפים

    TextView tvHomeRecipeCurrentlyCookingRecipeName, tvHomeRecipeNoCurrentlyCookingRecipeName ;
    ImageView ivHomeRecipeCurrentlyCooking;
    static Recipe currentCookingRecipe;
    static ArrayList<Recipe> favoriteRecipe;

    private ViewPager2 favoriteViewPager;
    private List<Recipe> favoriteRecipes = new ArrayList<>();
    private FavoriteRecipeAdapter favoriteAdapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView tvHomeFavoriteRecipes = findViewById(R.id.tvHomeFavoriteRecipes);

        tvHomeFavoriteRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                // מעבר למסך שמציג את כל המועדפים
                Intent intent = new Intent(HomeActivity.this, FavoritesListActivity.class);
                startActivity(intent);
            }
        });

        loadCurrentCookingView();// טוען את המתכון שמתבשל כרגע
        loadFavoriteRecipesView();// טוען את הקרוסלת המועדפים
    }


    private void loadFavoriteRecipesView() {
        String uid=FirebaseAuth.getInstance().getUid();
        favoritesPref = getSharedPreferences(FAVORITES_PREF+ "" +uid, MODE_PRIVATE);// טוען את ההעדפות של מועדפים

        Set<String> favoriteRecipeNames = favoritesPref.getStringSet("favorites", new HashSet<>()); // מביא את שמות המתכונים ששמורים כמועדפים
        getFavoriteRecipesByNames(favoriteRecipeNames, new OnRecipesLoadedListener() {
            @Override
            public void onRecipesLoaded(ArrayList<Recipe> recipes) {
                try {
                    favoriteRecipes.clear();// מנקה את הרשימה הקודמת
                    if (recipes != null) {
                        favoriteRecipes.addAll(recipes);// מוסיף את המתכונים שהתקבלו
                    }
                    // יוצר את האדפטר עם האזנה ללחיצה על מתכון
                    favoriteAdapter = new FavoriteRecipeAdapter(favoriteRecipes, HomeActivity.this, recipe -> {
                        Intent intent = new Intent(HomeActivity.this, RecipeViewActivity.class);
                        intent.putExtra("recipeName", recipe.getName());
                        intent.putExtra("recipeIngredients", recipe.getIngredients());
                        intent.putExtra("recipeInstructions", recipe.getInstructions());
                        intent.putExtra("imagePath", recipe.getImage());
                        intent.putExtra("recipeAuthorUid", recipe.getAuthorUid());

                        startActivity(intent);

                    });



                } catch (Exception e) {
                    Log.e("YourTag", "Error ", e);
                    e.printStackTrace();
                }

                favoriteViewPager = findViewById(R.id.pagerHomeFavoriteView);
                favoriteViewPager.setAdapter(favoriteAdapter); // מחבר את האדפטר לתצוגה

                ImageView ivArrowLeft = findViewById(R.id.ivHomeArrowLeft);
                ImageView ivArrowRight = findViewById(R.id.ivHomeArrowRight);

                // לוחץ שמאלה — עובר מתכון אחורה
                ivArrowLeft.setOnClickListener(v -> {
                    int currentItem = favoriteViewPager.getCurrentItem();
                    if (currentItem > 0) {
                        favoriteViewPager.setCurrentItem(currentItem - 1, true);
                    }
                });

                // לוחץ ימינה — עובר מתכון קדימה
                ivArrowRight.setOnClickListener(v -> {
                    int currentItem = favoriteViewPager.getCurrentItem();
                    if (currentItem < favoriteAdapter.getItemCount() - 1) {
                        favoriteViewPager.setCurrentItem(currentItem + 1, true);
                    }
                });

            }
        });
    }


    private void loadCurrentCookingView() {
        tvHomeRecipeCurrentlyCookingRecipeName = findViewById(R.id.tvHomeRecipeCurrentlyCookingRecipeName);
        tvHomeRecipeNoCurrentlyCookingRecipeName = findViewById(R.id.tvHomeNoRecipeCurrentlyCooking);
        ivHomeRecipeCurrentlyCooking = findViewById(R.id.ivHomeRecipeCurrentlyCooking);

        tvHomeRecipeCurrentlyCookingRecipeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // לחיצה על שם המתכון תוביל לתצוגת מתכון
                Intent intent = new Intent(HomeActivity.this, RecipeViewActivity.class)
                        .putExtra("recipeName", currentCookingRecipe.getName())
                        .putExtra("recipeIngredients", currentCookingRecipe.getIngredients())
                        .putExtra("recipeInstructions", currentCookingRecipe.getInstructions())
                        .putExtra("imagePath", currentCookingRecipe.getImage());

                startActivity(intent);
                finish();
            }
        });
        ivHomeRecipeCurrentlyCooking.setOnClickListener(new View.OnClickListener() {        // גם לחיצה על התמונה תוביל לתצוגת מתכון
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RecipeViewActivity.class)
                        .putExtra("recipeName", currentCookingRecipe.getName())
                        .putExtra("recipeIngredients", currentCookingRecipe.getIngredients())
                        .putExtra("recipeInstructions", currentCookingRecipe.getInstructions())
                        .putExtra("imagePath", currentCookingRecipe.getImage());

                startActivity(intent);
                finish();
            }
        });

        cookingPref = getSharedPreferences(COOKING_PREF , MODE_PRIVATE);// טוען את ההעדפות של מתכון בבישול
        String currentlyCookingRecipe = cookingPref.getString("cookingRecipe", "");// מביא את שם המתכון שמתבשל כרגע
        getCurrentCookingRecipeByName(currentlyCookingRecipe, new OnRecipeLoadedListener() { // מביא את האובייקט המלא של המתכון לפי השם
            @Override
            public void onRecipeLoaded(Recipe recipe) {
                if (recipe != null) {
                    String imageUrl = recipe.getImage();
                    String recipeName = recipe.getName();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imageUrl);
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        Glide.with(HomeActivity.this).load(uri).centerCrop().into(ivHomeRecipeCurrentlyCooking);
                    });
                    tvHomeRecipeCurrentlyCookingRecipeName.setText(recipeName);
                    tvHomeRecipeNoCurrentlyCookingRecipeName.setVisibility(View.INVISIBLE);
                } else {
                    tvHomeRecipeNoCurrentlyCookingRecipeName.setVisibility(View.VISIBLE);
                    ivHomeRecipeCurrentlyCooking.setImageDrawable(null);
                    tvHomeRecipeCurrentlyCookingRecipeName.setText("");

                    tvHomeRecipeCurrentlyCookingRecipeName.setOnClickListener(null);
                    ivHomeRecipeCurrentlyCooking.setOnClickListener(null);
                }
            }
        });
    }

    //עבור מתכון אחד תצוגה
    public interface OnRecipeLoadedListener {
        void onRecipeLoaded(Recipe recipe);
    }

    //עבור כמה מתכונים תצוגה
    public interface OnRecipesLoadedListener {
        void onRecipesLoaded(ArrayList<Recipe> recipes);
    }

    // פונקציה שמביאה את המתכונים המועדפים כרגע לפי שם
    public void getFavoriteRecipesByNames(Set<String> recipesNames, OnRecipesLoadedListener listener) {
        DatabaseReference recipesRef = FirebaseDatabase.getInstance().getReference("recipes");
        recipesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favoriteRecipe = new ArrayList<>();
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    String recipeName = recipeSnapshot.child("name").getValue(String.class);
                    if (recipesNames.contains(recipeName)) {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        favoriteRecipe.add(recipe);
                    }
                }
                listener.onRecipesLoaded(favoriteRecipe);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onRecipesLoaded(null);
            }
        });

    }
    // פעולה שמביאה את המתכון שמתבשל כרגע לפי שם
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
            return true;
        } else if (itemId == R.id.menu_item_favorites_list) {
            Intent intent = new Intent(this, FavoritesListActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_myRecipes_list) {
            Intent intent = new Intent(this, MyRecipesListActivity.class);
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

}


