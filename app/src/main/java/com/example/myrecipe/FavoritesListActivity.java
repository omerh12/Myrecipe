package com.example.myrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoritesListActivity extends AppCompatActivity {

    private RecyclerView favoritesRecyclerView;//  שמציג את המתכונים ברשימה
    private RecipesAdapter adapter;// האדפטר שיציג את המתכונים
    private List<Recipe> favoriteRecipes = new ArrayList<>();// רשימה של מתכונים אהובים
    private SharedPreferences prefs;//מתכון נשמור האם מתכון אהוב
    private String FAVORITES_PREF = "favorite_recipes";// שם הקובץ שבו שומרים את האהובים
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list);

        favoritesRecyclerView = findViewById(R.id.rvFavorites);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));// סידור לינארי של הרשימה
        adapter = new RecipesAdapter(this, favoriteRecipes);// יוצרת את האדפטר עם רשימה ריקה
        favoritesRecyclerView.setAdapter(adapter);// מחברת את האדפטר לרשימה במסך

        uid=FirebaseAuth.getInstance().getUid();

        // טוענת את רשימת שמות המתכונים שסומנו כאהובים מההעדפות
        prefs = getSharedPreferences(FAVORITES_PREF + ""+uid, MODE_PRIVATE);
        Set<String> favoriteNames = prefs.getStringSet("favorites", new HashSet<>());

        // מתחבר ל- Firebase כדי להביא את כל המתכונים
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("recipes");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                favoriteRecipes.clear();// מנקה את הרשימה לפני הטעינה

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Recipe recipe = childSnapshot.getValue(Recipe.class);// ממירה את הנתונים למתכון

                    if (recipe != null && favoriteNames.contains(recipe.getName())) {
                        // מוסיפה לרשימה רק את המתכונים שהשם שלהם נמצא ברשימת האהובים
                        favoriteRecipes.add(recipe);
                    }
                }
                adapter.notifyDataSetChanged();// מעדכנת את התצוגה

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("FavoriteListActivity", "Failed.", error.toException());
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
            return true;
        }else if (itemId == R.id.menu_item_myRecipes_list) {
            Intent intent = new Intent(this, MyRecipesListActivity.class);
            startActivity(intent);
            return true;
        }  else if (itemId == R.id.menu_item_recipe_list) {
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