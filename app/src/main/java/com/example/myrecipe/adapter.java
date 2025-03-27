package com.example.myrecipe;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class adapter extends ArrayAdapter<Recipe> {
    private Context Context;
    private List<Recipe> RecipeList;

    public adapter(Context context, int resource, int textViewResourceId, List<Recipe> recipeList) {
        super(context, resource, textViewResourceId, recipeList);
        this.Context = context;
        this.RecipeList = recipeList;
    }








}
