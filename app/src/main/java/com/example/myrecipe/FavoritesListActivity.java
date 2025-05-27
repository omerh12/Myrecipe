package com.example.myrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoritesListActivity extends AppCompatActivity {

    private RecyclerView favoritesRecyclerView;
    private RecipesAdapter adapter;
    private List<Recipe> allRecipes = new ArrayList<>();
    private List<Recipe> favoriteRecipes = new ArrayList<>();
    private SharedPreferences prefs;
    private static final String FAVORITES_PREF = "favorite_recipes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list);

        favoritesRecyclerView = findViewById(R.id.rvFavorites);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipesAdapter(this, favoriteRecipes);
        favoritesRecyclerView.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences(FAVORITES_PREF, MODE_PRIVATE);
        Map<String, ?> allFavorites = prefs.getAll();


        DatabaseReference database = FirebaseDatabase.getInstance().getReference("recipes");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                allRecipes.clear();
                favoriteRecipes.clear();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Recipe recipe = childSnapshot.getValue(Recipe.class);

                    if (recipe != null && allFavorites.containsKey(recipe.getName())) {
                        Object value = allFavorites.get(recipe.getName());
                        if (value instanceof Boolean && (Boolean) value) {
                            favoriteRecipes.add(recipe);
                        }
                    }
                }
                adapter.notifyDataSetChanged(); // Update RecyclerView

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Optional: handle Firebase read error
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
}