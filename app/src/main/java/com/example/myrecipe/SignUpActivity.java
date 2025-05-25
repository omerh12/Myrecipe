package com.example.myrecipe;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    EditText etSignUpEmail, etSignUpPass;
    Button btnSignUp;

    String TAG = "SignUpActivity";
    FirebaseAuth mAuth;

    GoogleSignInClient mGoogleSignInClient;
    ActivityResultLauncher<Intent> mGoogleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        etSignUpEmail = findViewById(R.id.etSignUpEmail);
        etSignUpPass = findViewById(R.id.etSignUpPass);
        btnSignUp = findViewById(R.id.btnSignUp);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etSignUpEmail.getText().toString();
                String pass = etSignUpPass.getText().toString();

                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill in both fields.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    CreateAccount(email, pass);

                }

            }
        });

        TextView tvSignUpLink = findViewById(R.id.tvSignInLink);

        SpannableString spannableString = new SpannableString("Already have an account? Sign in");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, 25, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSignUpLink.setText(spannableString);
        tvSignUpLink.setMovementMethod(LinkMovementMethod.getInstance());
        spannableString.setSpan(new ForegroundColorSpan(Color.YELLOW), 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


    }
        public void CreateAccount (String email, String pass){

            Log.d(TAG, "createAccount:" + email);
            mAuth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Log.d(TAG, "User created");
                                FirebaseUser user=mAuth.getCurrentUser();
                                updateUI(user);
                            }

                            else{
                                Log.w(TAG,"User creation failed", task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);

                            }
                        }
                    });


        }

        private void updateUI(FirebaseUser user) {

        if(user!=null)
            {
                Toast.makeText(SignUpActivity.this, "Registration Successful",
                        Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SignUpActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
            if(user==null)
            {
                Toast.makeText(SignUpActivity.this, "Registration failed",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }






