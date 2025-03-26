package com.example.myrecipe;

import android.graphics.Bitmap;

public class Recipe {
    private String Name;
    private String Ingredients;
    private String Instructions;
    private Bitmap Image;

    public Recipe(String name, String ingredients, String instructions, Bitmap image) {
        this.Name = name;
        this.Ingredients = ingredients;
        this.Instructions = instructions;
        this.Image = image;
    }
    public Recipe(String name, String ingredients, String instructions) {
        this.Name=name;
        this.Ingredients = ingredients;
        this.Instructions = instructions;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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
