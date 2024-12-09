package com.example.myrecipe;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class Home extends AppCompatActivity {

    Button Search;
    Button Log_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log_out = findViewById(R.id.Log_out);
        Log_out.setSoundEffectsEnabled(false);

        Log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, SignIn.class);
                startActivity(intent);
            }

            Search = findViewById(R.id.Log_out);
            Search.setSoundEffectsEnabled(false);

             Search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Home.this, Search1.class);
                    startActivity(intent);

        });


    }


}