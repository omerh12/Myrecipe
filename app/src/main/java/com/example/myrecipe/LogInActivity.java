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


public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

   String TAG = "SignInActivity";
   FirebaseAuth mAuth;
   GoogleSignInClient mGoogleSignInClient;
   ActivityResultLauncher<Intent> signInLauncher;

    EditText etSignInEmail;
    EditText etSignInPass;
    Button btnSignIn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etSignInEmail=findViewById(R.id.etSignInEmail);
        etSignInPass=findViewById(R.id.etSignInPass);
        btnSignIn=findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
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
                        Intent intent=new Intent(LogInActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(LogInActivity.this,
                                "One or more details are incorrect",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}