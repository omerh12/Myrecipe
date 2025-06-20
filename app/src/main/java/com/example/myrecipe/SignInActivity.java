package com.example.myrecipe;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import android.content.SharedPreferences;



public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

   FirebaseAuth mAuth;    // מחלקה שאחראית על התחברות עם Firebase
    GoogleSignInClient mGoogleSignInClient;    // לקוח של Google Sign-In
    ActivityResultLauncher<Intent> signInLauncher;    // משגר תוצאה עבור התחברות עם חשבון Google
    GoogleSignInOptions gso;    // אפשרויות ההתחברות של Google


    EditText etSignInEmail,etSignInPass;    // שדות להזנת אימייל וסיסמה
    Button btnSignInEmailAndPass, btnSignInGoogle;    // כפתורים להתחברות עם אימייל וסיסמה או עם Google
    TextView tvSignUpLink;    // טקסט לחיץ שמפנה למסך ההרשמה


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        etSignInEmail=findViewById(R.id.etSignInEmail);
        etSignInPass=findViewById(R.id.etSignInPass);
        btnSignInEmailAndPass =findViewById(R.id.btnSignInEmail);
        btnSignInGoogle=findViewById(R.id.btnSignInGoogle);

        btnSignInEmailAndPass.setOnClickListener(this);
        btnSignInGoogle.setOnClickListener(this);


        tvSignUpLink = findViewById(R.id.tvSignUpLink);
        tvSignUpLink.setPaintFlags(tvSignUpLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tvSignUpLink.setOnClickListener(v->
        {  Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);  }) ;


        mAuth = FirebaseAuth.getInstance();

        // בנייה של אפשרויות ההתחברות עם Google
         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);        // יצירת לקוח התחברות לפי ההגדרות שנבנו למעלה

        // הרשמה לפעולה שמאזינה לתוצאה מההתחברות עם Google
        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);    // אם הצליח, מוציא את חשבון ה-Google ושולח לפיירבייס
                            firebaseAuthWithGoogle(account.getIdToken());
                        }

                        catch (ApiException e) {
                            Toast.makeText(SignInActivity.this, "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    // פעולה שמפעילה את חלון ההתחברות של גוגל
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);

    }
    // פעולה ששומרת משתמש אחרי שהתחבר דרך גוגל
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {                        // התחברות הצליחה, שומרת את המשתמש בשיירד פרפרנס
                        FirebaseUser user = mAuth.getCurrentUser();
                        saveUserToPreferences(user);
                        updateUI(user);
                    } else {
                        Toast.makeText(SignInActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    // אם המשתמש מחובר מעבירה למסך הבית
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(this, "User signed in", Toast.LENGTH_SHORT).show();
           Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
       } else {
           Toast.makeText(this, "User signed out", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view== btnSignInEmailAndPass)
            handleEmailSignIn();
        else if (view==btnSignInGoogle) {
            signInWithGoogle();
        }
    }

    // פעולה שמטפלת בהתחברות עם אימייל וסיסמה
    public void handleEmailSignIn() {
        mAuth = FirebaseAuth.getInstance();
        String email = etSignInEmail.getText().toString().trim();
        String pass = etSignInPass.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    saveUserToPreferences(user);
                    updateUI(user);
                } else {
                    Toast.makeText(SignInActivity.this,
                            "One or more details are incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // שמירת פרטי המשתמש ב-SharedPreferences
    private void saveUserToPreferences(FirebaseUser user) {
        if (user == null) return;

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_logged_in", true);
        editor.putString("user_email", user.getEmail());
        editor.apply();
    }

}