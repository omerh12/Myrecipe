package com.example.myrecipe;

public class Recipe {
    private String name;
    private String ingredients;
    private String instructions;
    private String imageURL;
    private String authorUid;

    public Recipe() {
    }

    public Recipe(String name, String ingredients, String instructions, String image, String authorUid) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageURL = image;
        this.authorUid = authorUid;
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

    public String getAuthorUid() {
        return authorUid;
    }

    public void setAuthorUid(String authorUid) {
        this.authorUid = authorUid;
    }
}
