package com.example.myrecipe;

import android.os.Bundle;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
}