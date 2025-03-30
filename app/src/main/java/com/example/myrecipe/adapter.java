package com.example.myrecipe;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class adapter extends ArrayAdapter<RecipeObjectClass> {
    private Context Context;
    private List<RecipeObjectClass> RecipeList;

    public adapter(Context context, int resource, int textViewResourceId, List<RecipeObjectClass> recipeList) {
        super(context, resource, textViewResourceId, recipeList);
        this.Context = context;
        this.RecipeList = recipeList;
    }

    public View getview(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) Context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.activity_recipe_example, parent, false);
        TextView recipe_name= (TextView) view.findViewById(R.id.recipe_name);
        TextView recipe_ingredients= (TextView) view.findViewById(R.id.recipe_ingredients);
        TextView recipe_instruction=(TextView) view.findViewById(R.id.recipe_instructions);

        RecipeObjectClass temp= RecipeList.get(position);
        recipe_name.setText(temp.getName());
        recipe_ingredients.setText(temp.getIngredients());
        recipe_instruction.setText(temp.getInstructions());
        return view;


    }
        }


