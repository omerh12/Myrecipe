package com.example.myrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Registration extends AppCompatActivity {

    // Initialize the EditText fields
    private EditText enter_name = findViewById(R.id.enter_name);
    private EditText enter_Email = findViewById(R.id.enter_Email);
    private EditText enter_password=findViewById(R.id.enter_password);
    // Find the Submit button and set an OnClickListener
    Button submit_reg;
    Button goback_Reg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonSubmit = findViewById(R.id.submit_reg);
        Button goback_Reg=findViewById(R.id.goback_Reg);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text entered in the name and email fields
                String name = enter_name.getText().toString();
                String email = enter_Email.getText().toString();
                String password=enter_password.getText().toString();;

                // Validate input
                if (name.isEmpty() || email.isEmpty()) {
                    Toast.makeText(Registration.this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
                } else {
                    // Create an Intent to pass the data to SecondActivity
                    Intent intent = new Intent(Registration.this, Home.class);
                    Intent intent_back = new Intent(Registration.this, MainActivity.class);

                    // Put the name and email as extras in the intent
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);

                    // Start SecondActivity
                    startActivity(intent);
                }
            }
        });
    }

    }





