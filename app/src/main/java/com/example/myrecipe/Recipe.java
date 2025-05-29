package com.example.myrecipe;

public class Recipe {
    private String name;
    private String ingredients;
    private String instructions;
    private String imageURL;

    public Recipe() {
    }

    public Recipe(String name, String ingredients, String instructions, String image) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageURL = image;
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
        return imageURL;
    }

    public void setImage(String imageUrl) {
        this.imageURL = imageUrl;
    }
}
