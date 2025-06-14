package com.example.myrecipe;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;

public class FirstScreenActivity extends AppCompatActivity {
    // משתנה לניהול העדפות משתמש
    PreferenceManager preferenceManager;
    // שני כפתורים למסך הראשי - התחברות והרשמה
    Button btnFirstScreenSignUp, btnFirstScreenSignIn;
    // אובייקט שמייצג את המשתמש הנוכחי
    FirebaseUser user;
    // משתנה בוליאני שבודק אם המשתמש כבר מחובר
    boolean isLoggedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        preferenceManager = new PreferenceManager(this);
        user = FirebaseAuth.getInstance().getCurrentUser();        // מקבלת את המשתמש הנוכחי מ-Firebase (אם קיים)
        isLoggedIn = preferenceManager.isLoggedIn();     // בודקת אם המשתמש מסומן כ"מחובר" בהעדפות


        if (user != null && isLoggedIn) {
            Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

        btnFirstScreenSignUp = findViewById(R.id.btnFirstScreenSignUp);
        btnFirstScreenSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreenActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnFirstScreenSignIn = findViewById(R.id.btnFirstScreenSignIn);
        btnFirstScreenSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreenActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

    }
    }
