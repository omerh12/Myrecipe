package com.example.myrecipe;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignIn extends AppCompatActivity {

    // Initialize the EditText fields


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        EditText enter_name = (EditText) findViewById(R.id.enter_name);
        EditText enter_Email = (EditText)  findViewById(R.id.enter_Email);
        Button submit_reg;

        // Find the Submit button and set an OnClickListener

        Button buttonSubmit = findViewById(R.id.submit_reg);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text entered in the name and email fields
                String name = enter_name.getText().toString();
                String email = enter_Email.getText().toString();

                // Validate input
                if (name.isEmpty() || email.isEmpty()) {
                    Toast.makeText(SignIn.this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
                } else {
                    // Create an Intent to pass the data to SecondActivity
                    Intent intent = new Intent(SignIn.this, Home.class);

                    // Put the name and email as extras in the intent
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);

                    // Start SecondActivity
                    startActivity(intent);
                }
            }
        });
       // Instantiate a Google sign in request using GetGoogleIdOption. Then, create the Credential Manager request using GetCredentialRequest:
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(getBaseContext().getString(R.string.default_web_client_id))
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)

        //You must pass your server's client ID to the setServerClientId method. To find the OAuth 2.0 client ID:

        //Open the Credentials page in the Cloud console.
        //The Web application type client ID is your backend server's OAuth 2.0 client ID.

        After you retrieve the credential, you should create a Google ID token from it:

        if (credential instanceof CustomCredential customCredential && credential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
            Bundle credentialData = customCredential.getData();
            GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);
            firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken());
        } else {
            Log.w(TAG, "Credential is not of type Google ID!");

            //In your sign-in activity's onCreate method, get the shared instance of the FirebaseAuth object:
            private FirebaseAuth mAuth;
// ...
// Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();
            //Then, when initializing your Activity, check to see if the user is currently signed in:

            @Override
            public void onStart() {
                super.onStart();
                // Check if user is signed in (non-null) and update UI accordingly.
                FirebaseUser currentUser = mAuth.getCurrentUser();
                updateUI(currentUser);
            }

            //Now get the user's Google ID token created in step 4, exchange it for a Firebase credential, and authenticate with Firebase using the Firebase credential:
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    updateUI(null);
                                }
                            }
                    );
            //If the call to signInWithCredential succeeds you can use the getCurrentUser method to get the user's account data.
            //To sign out a user, call signOut:
            FirebaseAuth.getInstance().signOut();
            //You also need to clear the current user credential state from all credential providers, as recommended by the Credential Manager
            ClearCredentialStateRequest clearRequest = new ClearCredentialStateRequest();
            credentialManager.clearCredentialStateAsync(
                    clearRequest,
                    new CancellationSignal(),
                    Executors.newSingleThreadExecutor(),
                    new CredentialManagerCallback<>() {
                        @Override
                        public void onResult(@NonNull Void result) {
                            updateUI(null);
                        }

                        @Override
                        public void onError(@NonNull ClearCredentialException e) {
                            Log.e(TAG, "Couldn't clear user credentials: " + e.getLocalizedMessage());
                        }
                    }
            );

        }
    }
}