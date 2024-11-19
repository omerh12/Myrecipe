package com.example.myrecipe;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignIn extends AppCompatActivity {

    private Button submit_signIn;
    private Button goback_SI;
    private EditText name_SI=findViewById(R.id.name_SI);
    private EditText email_SI=findViewById(R.id.email_SI);
    private EditText password_SI=findViewById(R.id.password_SI);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit_signIn=findViewById(R.id.submit_signIn);
        goback_SI=findViewById(R.id.goback_SI);

        submit_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text entered in the name and email fields
                String name = name_SI.getText().toString();
                String email = email_SI.getText().toString();
                String password = password_SI.getText().toString();

                // Validate input
                if (name.isEmpty() || email.isEmpty()) {
                    Toast.makeText(SignIn.this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
                } else {
                    // Create an Intent to pass the data to SecondActivity
                    Intent intent = new Intent(SignIn.this, Home.class);

                    // Put the name and email as extras in the intent
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);

                    // Start SecondActivity
                    startActivity(intent);
                }
            }
        });
    }

}