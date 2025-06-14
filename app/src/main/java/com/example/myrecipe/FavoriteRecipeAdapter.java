package com.example.myrecipe;

import android.view.LayoutInflater;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.List;

public class FavoriteRecipeAdapter extends RecyclerView.Adapter<FavoriteRecipeAdapter.RecipeViewHolder> {
    //יאפשר לדעת על איזה מתכון לחצו בקרוסלה
    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    private List<Recipe> favoriteList;// רשימת המתכונים האהובים
    private Context context;
    private OnRecipeClickListener onRecipeClickListener;

    public FavoriteRecipeAdapter(List<Recipe> favoriteList, Context context, OnRecipeClickListener onRecipeClickListener) {
        this.favoriteList = favoriteList;
        this.context = context;
        this.onRecipeClickListener = onRecipeClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_carousel, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = favoriteList.get(position);// מקבלים את המתכון שצריך להציג כרגע
        holder.title.setText(recipe.getName());// מציגים את שם המתכון בכותרת
        String imagePath = recipe.getImage();// מקבלים את הפאת' של התמונה מהמתכון
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imagePath);   // טוענים את התמונה מה-Firebase Storage
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(context)
                    .load(uri)
                    .centerCrop()
                    .into(holder.imageView);// מציגים את התמונה בתוך התמונה במסך
        });

        holder.itemView.setOnClickListener(v -> {// אם לוחצים על המתכון, מפעילים את הפעולה מהממשק
            if (onRecipeClickListener != null) {
                onRecipeClickListener.onRecipeClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder { // מחלקת ViewHolder שמחזיקה את הרכיבים של כל מתכון בתצוגה
        ShapeableImageView imageView;
        TextView title;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recipeImage);
            title = itemView.findViewById(R.id.recipeTitle);
        }
    }
}
