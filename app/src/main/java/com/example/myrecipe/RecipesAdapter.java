package com.example.myrecipe;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private List<Recipe> recipes;
    private Context context;

    public RecipesAdapter(Context context, List<Recipe> recipes) {
        this.context=context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_recipe_item, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe currentRecipe = recipes.get(position);
        holder.recipeNameTextView.setText(currentRecipe.getName());

        String imagePath = currentRecipe.getImage();

        if (imagePath != null && !imagePath.isEmpty()) {

            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(imagePath);
            storageRef.getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(context)
                                    .load(uri)
                                    .centerCrop() // Or other scaling options
                                    .into(holder.recipeImageView);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                             Log.e("RecipesAdapter", "Error loading image: " + e.getMessage());
                        }
                    });

        } else {
            holder.recipeImageView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(context, RecipeViewActivity.class);

                intent.putExtra("recipeName", currentRecipe.getName());
                intent.putExtra("recipeIngredients", currentRecipe.getIngredients());
                intent.putExtra("recipeInstructions", currentRecipe.getInstructions());
                intent.putExtra("imagePath", currentRecipe.getImage());

                context.startActivity(intent);

            }

        } );

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
            recipeImageView=itemView.findViewById(R.id.recipeItemImageView);
            recipeNameTextView = itemView.findViewById(R.id.tvRecipeItemRecipeName);
        }
    }
}


