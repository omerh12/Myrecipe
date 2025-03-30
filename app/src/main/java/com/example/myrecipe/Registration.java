package com.example.myrecipe;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.GoogleAuthProvider;

public class Registration extends AppCompatActivity {

    EditText etRegistrationEmail, etRegistrationPassword;
    Button btnRegistrationSignUp;

    String TAG = "SignUpActivity";
    FirebaseAuth mAuth;

    GoogleSignInClient mGoogleSignInClient;
    ActivityResultLauncher<Intent> mGoogleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        etRegistrationEmail = findViewById(R.id.etRegistrationEmail);
        etRegistrationPassword = findViewById(R.id.etRegistrationPassword);
        btnRegistrationSignUp = findViewById(R.id.btnRegistrationSignUp);


        btnRegistrationSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etRegistrationEmail.getText().toString();
                String pass = etRegistrationPassword.getText().toString();

                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(Registration.this, "Please fill in both fields.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    CreateAccount(email, pass);

                }

            }
        });
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
                                Toast.makeText(Registration.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);

                            }
                        }
                    });


        }

        private void updateUI(FirebaseUser user) {

        if(user!=null)
            {
                Toast.makeText(Registration.this, "Registration Successful",
                        Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Registration.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
            if(user==null)
            {
                Toast.makeText(Registration.this, "Registration failed",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }






