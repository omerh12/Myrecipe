package com.example.myrecipe;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class SignIn extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> signInLauncher;
    EditText etSignInEmail;
    EditText etSignInPass;
    Button btnSignIn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        etSignInEmail=findViewById(R.id.etSignInEmail);
        etSignInPass=findViewById(R.id.etSignInPass);
        btnSignIn=findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);

//        EditText enter_name = (EditText) findViewById(R.id.enter_name);
//        EditText enter_Email = (EditText)  findViewById(R.id.enter_Email);
//        Button submit_reg;

        // Initialize Firebase Auth
        //mAuth = FirebaseAuth.getInstance();

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        // Find the Submit button and set an OnClickListener
//
//        // Set up the ActivityResultLauncher
//        signInLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == RESULT_OK) {
//                        Intent data = result.getData();
//                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//                        try {
//                            // Google Sign In was successful, authenticate with Firebase
//                            GoogleSignInAccount account = task.getResult(ApiException.class);
//                            firebaseAuthWithGoogle(account.getIdToken());
//                        } catch (ApiException e) {
//                            // Google Sign In failed
//                            Log.w(TAG, "Google sign in failed", e);
//                            Toast.makeText(SignIn.this, "Google sign in failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//        );
//
//        findViewById(R.id.googleSignInButton).setOnClickListener(v -> {
//            signIn();
//        });

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
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            // User is signed out
            Toast.makeText(this, "User signed out", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view==btnSignIn){
            mAuth=FirebaseAuth.getInstance();
            //String email=etSignInEmail.getText().toString();
            //String pass=etSignInPass.getText().toString();
            String email="abc@a.com";
            String pass="123456";
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent=new Intent(SignIn.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(SignIn.this,
                                "One or more details are incorrect",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}