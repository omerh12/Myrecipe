package com.example.myrecipe;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.content.Context;
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

public class RecipiesAdapter extends RecyclerView.Adapter<RecipiesAdapter.RecipeViewHolder> {

    private List<Recipe> recipes;
    private Context context;

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
                            // Handle any errors (e.g., image not found, permission issues)
                            // You might want to log the error or display a default image
                            //holder.recipeImageView.setImageResource(R.drawable.default_image);
                            // Or log the error:
                             Log.e("RecipesAdapter", "Error loading image: " + e.getMessage());
                        }
                    });

        } else {
            // Handle the case where the image URL is missing or empty
            holder.recipeImageView.setVisibility(View.GONE);
        }
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
            recipeNameTextView = itemView.findViewById(R.id.recipeNameTextView);
        }
    }
}


