package com.example.myrecipe;

import android.graphics.Bitmap;

public class Recipe {

    private String Ingredients;
    private String Instructions;
    private Bitmap Image;


    public Recipe(String ingredients, String instructions, Bitmap image) {
        Ingredients = ingredients;
        Instructions = instructions;
        Image = image;
    }

    public Recipe(String ingredients, String instructions) {
        Ingredients = ingredients;
        Instructions = instructions;
    }

    public String getIngredients() {
        return Ingredients;
    }

    public void setIngredients(String ingredients) {
        Ingredients = ingredients;
    }

    public String getInstructions() {
        return Instructions;
    }

    public void setInstructions(String instructions) {
        Instructions = instructions;
    }

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }
}
