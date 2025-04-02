package com.example.myrecipe;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Recipe {
    private String name;
    private String ingredients;
    private String instructions;
    private String ImageUrl;

    public Recipe() {
    }

    public Recipe(String name, String ingredients, String instructions, String image) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.ImageUrl = image;
    }
    public Recipe(String name, String ingredients, String instructions) {
        this.name =name;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImage() {
        return ImageUrl;
    }

    public void setImage(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
