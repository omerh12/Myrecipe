package com.example.myrecipe;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }
            Button buttonGoBack = findViewById(R.id.buttonGoBack);
            buttonGoBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity_sign_in.this, Home.class);
                    startActivity(intent);
                    finish(); // Optional: This will remove the SecondActivity from the back stack.
                }
            });
        }
    }

}