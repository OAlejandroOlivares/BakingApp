package com.oolivares.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class RecipiesAdapter<Receta> extends RecyclerView.Adapter<RecipiesAdapter.MyViewHolder>  {

    private List<Recetas> recipies;
    final private RecipieClickListener recipieClickListener;

    RecipiesAdapter(Context context, List<Recetas> recipies, RecipieListFragment recipieListFragment, RecipieClickListener recipieClickListener) {
        this.recipies = recipies;
        this.recipieClickListener = recipieClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipie_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipiesAdapter.MyViewHolder holder, int position) {
        Recetas recipie = recipies.get(position);
        holder.servings.setText("Servings: "+ recipie.getServings());
        holder.name.setText(recipie.getName());
        Picasso.get().load(Uri.parse(recipie.getImage())).error(R.drawable.ic_cake_black_24dp).into(holder.image);
    }

    public interface RecipieClickListener {
        void onRecipieClick(int index);
    }

    @Override
    public int getItemCount() {
        return recipies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.tv_servings)
        TextView servings;
        @BindView(R.id.image)
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int index =getAdapterPosition();
            recipieClickListener.onRecipieClick(index);
        }
    }
}
