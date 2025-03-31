package com.example.myrecipe;

import android.view.LayoutInflater;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

public class RecipiesAdapter extends RecyclerView.Adapter<RecipiesAdapter.RecipeViewHolder> {

    private List<Recipe> recipes;

    public RecipiesAdapter(Context context, List<Recipe> recipes) {
        this.context=context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_recipe_item, parent, false); // Replace with your item layout
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe currentRecipe = recipes.get(position);
        holder.recipeNameTextView.setText(currentRecipe.getName());


        if(currentRecipe.getImageUrl()!=null){
           holder.recipeImageView.setImageResource(R.drawable.default_image);
        }
        else{
            throw new IllegalArgumentException("Recipe image URL is null");
        }
        // You can add more data binding here if you have other recipe properties
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeNameTextView;
        public ImageView recipeImageView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImageView=itemView.findViewById(R.id.recipeImageView);
            recipeNameTextView = itemView.findViewById(R.id.recipeNameTextView); // Replace with your TextView ID
        }
    }
}


