package com.example.myrecipe;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;

public class FirstScreenActivity extends AppCompatActivity {

    SignInPreferenceHandler preferenceHandler;
    Button btnFirstScreenSignUp,btnFirstScreenSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        preferenceHandler = new SignInPreferenceHandler(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        preferenceHandler.handleSuccessfulLogin(user);


        btnFirstScreenSignUp = findViewById(R.id.btnFirstScreenSignUp);


        btnFirstScreenSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreenActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnFirstScreenSignIn = findViewById(R.id.btnFirstScreenSignIn); // Make sure the ID matches the button in your XML

        btnFirstScreenSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreenActivity.this, SignInActivity.class); // Make sure LoginActivity is the correct class
                startActivity(intent);
            }
        });
        preferenceHandler.handleSuccessfulLogin(user);
        boolean isLoggedIn = preferenceHandler.isPreviouslyLoggedIn();

        if (user != null && isLoggedIn) {
            Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }



    }

    public class SignInPreferenceHandler {

        private final PreferenceManager preferenceManager;

        public SignInPreferenceHandler(Context context) {
            this.preferenceManager = new PreferenceManager(context);
        }

        public void handleSuccessfulLogin(FirebaseUser user) {
            if (user != null) {
                preferenceManager.setUserEmail(user.getEmail());
                preferenceManager.setLoggedIn(true);
            }
        }

        public boolean isPreviouslyLoggedIn() {
            return preferenceManager.isLoggedIn();
        }
    }

}
