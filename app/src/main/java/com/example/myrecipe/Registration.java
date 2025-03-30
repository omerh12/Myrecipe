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

public class Registration extends AppCompatActivity {

EditText etRegistrationEmail, etRegistrationPassword;
Button btnRegistrationSignUp;

    String TAG = "SignInActivity";
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    ActivityResultLauncher<Intent> signInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        etRegistrationEmail = (EditText) findViewById(R.id.etRegistrationEmail);
        etRegistrationPassword = (EditText)  findViewById(R.id.etRegistrationPassword);
        Button btnRegistrationSignUp = findViewById(R.id.btnRegistrationSignUp);


        btnRegistrationSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etRegistrationEmail.getText().toString();
                String pass = etRegistrationPassword.getText().toString();

                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(Registration.this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
                } else {
                    CreateAccount(email,pass);

                    // Create an Intent to pass the data to SecondActivity
                    Intent intent = new Intent(Registration.this, HomeActivity.class);
                    // Start SecondActivity
                    startActivity(intent);
                }

            }
        });

       public void CreateAccount (String email, String pass){

           Log.d(TAG, "createAccount:" + email);
           mAuth.createUserWithEmailAndPassword(email,pass)
                   .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful())
                   {
                       Log.d(TAG, "User created");
                       firebaseUser user=mAuth.getCurrentUser();
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
    }
}





