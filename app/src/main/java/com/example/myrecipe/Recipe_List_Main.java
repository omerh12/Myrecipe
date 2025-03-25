package com.example.myrecipe;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import android.view.Menu;


import com.google.firebase.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;

public class Recipe_List_Main extends AppCompatActivity {

    ArrayList<Recipe> Recipe_List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list_main);

        Recipe_List=new ArrayList<Recipe>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference RecipeRef=database.getReference("recipes");

        RecipeRef.setValue("")

     //   for(int i=0;i<6;i++){

       //     Recipe rec=new Recipe ("ביצים, קמח, חלב, חמאה, שמן, מלח", "לערבב את כל המרכיבים ולשים בתנור");
         //   Recipe_List.add(rec);
           // DatabaseReference newrecRef= RecipeRef.push();
            //newrecRef.setValue(rec);

       // }
    }


}