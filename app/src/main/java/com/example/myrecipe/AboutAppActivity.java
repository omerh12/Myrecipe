package com.example.myrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class AboutAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about_app);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_about:
                // Navigate to profile screen
                Intent AboutAppIntent = new Intent(this, AboutAppActivity.class);
                startActivity(AboutAppIntent);
                return true;

            case R.id.menu_item_logout:
                // Sign out and return to login
                FirebaseAuth.getInstance().signOut();

                // Optional: Clear SharedPreferences
                new PreferenceManager(this).clear();

                Intent logoutIntent = new Intent(this, LogInActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
                startActivity(logoutIntent);
                finish();
                return true;


            case R.id.menu_item_profile:
                // Navigate to profile screen
                Intent ProfileIntent = new Intent(this, ProfileActivity.class);
                startActivity(ProfileIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }
    }



}