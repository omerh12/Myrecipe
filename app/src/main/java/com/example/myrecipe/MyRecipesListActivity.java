package com.example.myrecipe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MyRecipesListActivity extends AppCompatActivity {
    private RecyclerView recipesRecyclerView;// תצוגת הרשימה של המתכונים
    private DatabaseReference database;// משתנה שמחזיק הפנייה ל- "recipes" בפיירבייס
    private List<Recipe> recipesList = new ArrayList<>();// רשימה של אובייקטים מסוג Recipe
    private RecipesAdapter adapter;// מקשר בין הרשימה ל- RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes_list);

        recipesRecyclerView = findViewById(R.id.recipesRecyclerView);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));// מסדרת את הרשימה לאורך
        adapter = new RecipesAdapter(this, recipesList);// יוצרת את האדפטר עם רשימת המתכונים
        recipesRecyclerView.setAdapter(adapter);// מחברת בין הרשימה לתצוגה

        database = FirebaseDatabase.getInstance().getReference("recipes");//  הפניה לענף של המתכונים בפיירבייס
        Query query = database.orderByChild("authorUid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.addValueEventListener(new ValueEventListener() {// מאזינה לשינויים בנתונים
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                recipesList.clear();// קודם כל מנקה את הרשימה הקודמת
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {// עוברת על כל מתכון
                    Recipe recipe = childSnapshot.getValue(Recipe.class); // ממירה את הנתונים לעצם מתכון
                    if (recipe != null) {
                        recipesList.add(recipe);// מוסיפה את המתכון לרשימה
                    }
                }
                adapter.notifyDataSetChanged();// מודיעה לאדפטר שהתוכן השתנה וצריך לעדכן את התצוגה
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MyRecipeListActivity", "Failed to read value.", error.toException());
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
        } else if (itemId == R.id.menu_item_myRecipes_list) {
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