package com.example.myrecipe;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {


    EditText etSignUpEmail, etSignUpPass;// שדות להזנת אימייל וסיסמה
    Button btnSignUp;// כפתור ההרשמה
    FirebaseAuth mAuth;// משתנה שמחזיק את מנגנון האימות של Firebase
    TextView tvSignInLink;// טקסט שהוא יהיה קישור לדף ההתחברות

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();//  FirebaseAuth (שירות ההרשמה/כניסה)

        etSignUpEmail = findViewById(R.id.etSignUpEmail);
        etSignUpPass = findViewById(R.id.etSignUpPass);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(v -> {
            String email = etSignUpEmail.getText().toString().trim();
            String pass = etSignUpPass.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
            } else {
                createAccount(email, pass);
            }
        });

        tvSignInLink = findViewById(R.id.tvSignInLink);
        tvSignInLink.setPaintFlags(tvSignInLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tvSignInLink.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    // פעולה שיוצרת חשבון חדש עם האימייל והסיסמה שהמשתמש נתן
    public void createAccount(String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass)// יוצרת משתמש חדש ב-Firebase
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();// מקבלת את המשתמש שנוצר
                        saveUserToPreferences(user);// שומרת את המשתמש בהעדפות
                        updateUI(user);// פעולה שמעבירה למסך הבית
                    } else {
                        Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    // פעולה ששומרת את פרטי המשתמש ב-SharedPreferences
    private void saveUserToPreferences(FirebaseUser user) {
        if (user == null) return;

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_logged_in", true);// שומר שהמשתמש מחובר
        editor.putString("user_email", user.getEmail());// שומר את כתובת האימייל
        editor.apply();
    }

    // פעולה שעוברת מסך לפי אם המשתמש מחובר או לא
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
        }
    }
}






