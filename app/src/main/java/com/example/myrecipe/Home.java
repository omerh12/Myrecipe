package com.example.myrecipe;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
            Button buttonGoToSecond = findViewById(R.id.buttonGoToSecond);
            buttonGoToSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Home.this, SignIn.class);
                    startActivity(intent);
                }
            }

    private void startActivity(Intent intent) {
    });
        }
    }


}