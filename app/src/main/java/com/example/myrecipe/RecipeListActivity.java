package com.example.myrecipe;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference database;
    private List<Recipe> recipesList = new ArrayList<>();
    private RecipiesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recipesRecyclerView); // Replace with your RecyclerView ID
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipiesAdapter(this, recipesList);
        recyclerView.setAdapter(adapter);

        // Get a reference to the "recipes" node in your Firebase database
        database = FirebaseDatabase.getInstance().getReference("recipes");

        // Read from the database
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                recipesList.clear(); // Clear the list before adding new data
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = childSnapshot.getValue(Recipe.class);
                    if (recipe != null) {
                        recipesList.add(recipe);
                    }
                }
                adapter.notifyDataSetChanged(); // Update the RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("RecipeListActivity", "Failed to read value.", error.toException());
                // Handle the error appropriately, e.g., display an error message to the user
            }
        });
    }
}