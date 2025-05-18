package com.example.myrecipe;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import androidx.annotation.Nullable;
import com.example.myrecipe.PreferenceManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

   String TAG = "SignInActivity";
   FirebaseAuth mAuth;
   PreferenceManager preferenceManager;
    GoogleSignInClient mGoogleSignInClient;
   ActivityResultLauncher<Intent> signInLauncher;

    EditText etSignInEmail;
    EditText etSignInPass;
    Button btnSignInEmail, btnSignInGoogle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etSignInEmail=findViewById(R.id.etSignInEmail);
        etSignInPass=findViewById(R.id.etSignInPass);
        btnSignInEmail=findViewById(R.id.btnSignInEmail);
        btnSignInGoogle=findViewById(R.id.btnSignInGoogle);

        btnSignInEmail.setOnClickListener(this);
        btnSignInGoogle.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        preferenceManager = new PreferenceManager(this);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        boolean isLoggedIn = preferenceManager.isLoggedIn();

        if (currentUser != null && isLoggedIn) {
            Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish(); // Prevent back navigation
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Replace with your client ID
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            firebaseAuthWithGoogle(account.getIdToken());
                        }

                        catch (ApiException e) {
                            // Google Sign In failed, update UI appropriately
                            Log.w(TAG, "Google sign in failed", e);
                            Toast.makeText(LogInActivity.this, "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LogInActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // User is signed in, navigate to the next activity
            preferenceManager.setUserEmail(user.getEmail());
            preferenceManager.setLoggedIn(true);

            Toast.makeText(this, "User signed in", Toast.LENGTH_SHORT).show();
            // Example: Start the main activity
           Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
       } else {
            //User is signed out
           Toast.makeText(this, "User signed out", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view==btnSignInEmail)
            handleEmailSignIn();
        else if (view==btnSignInGoogle) {
            signIn();
        }
    }

    public void handleEmailSignIn() {
        mAuth = FirebaseAuth.getInstance();
        //String email=etSignInEmail.getText().toString();
        //String pass=etSignInPass.getText().toString();
        String email = "abc@a.com";
        String pass = "123456";
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user=mAuth.getCurrentUser();
                    preferenceManager.setUserEmail(user.getEmail());
                    preferenceManager.setLoggedIn(true);

                    Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LogInActivity.this,
                            "One or more details are incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}