package com.example.myrecipe;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class SignIn extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> signInLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


//        EditText enter_name = (EditText) findViewById(R.id.enter_name);
//        EditText enter_Email = (EditText)  findViewById(R.id.enter_Email);
//        Button submit_reg;

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Find the Submit button and set an OnClickListener

        // Set up the ActivityResultLauncher
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
                        } catch (ApiException e) {
                            // Google Sign In failed
                            Log.w(TAG, "Google sign in failed", e);
                            Toast.makeText(SignIn.this, "Google sign in failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

//        Button buttonSubmit = findViewById(R.id.submit_reg);

//        buttonSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Get the text entered in the name and email fields
//                String name = enter_name.getText().toString();
//                String email = enter_Email.getText().toString();
//
//                // Validate input
//                if (name.isEmpty() || email.isEmpty()) {
//                    Toast.makeText(SignIn.this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Create an Intent to pass the data to the SecondActivity
//                    Intent intent = new Intent(SignIn.this, Home.class);
//
//                    // Put the name and email as extras in the intent
//                    intent.putExtra("name", name);
//                    intent.putExtra("email", email);
//
//                    // Start SecondActivity
//                    startActivity(intent);
//                }
//            }
//        });

        findViewById(R.id.googleSignInButton).setOnClickListener(v -> {
            signIn();
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
                        Toast.makeText(SignIn.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // User is signed in, navigate to the next activity
            Toast.makeText(this, "User signed in", Toast.LENGTH_SHORT).show();
            // Example: Start the main activity
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();
        } else {
            // User is signed out
            Toast.makeText(this, "User signed out", Toast.LENGTH_SHORT).show();
        }
    }
}