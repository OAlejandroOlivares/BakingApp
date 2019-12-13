package com.oolivares.bakingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>{

    private JSONArray ingredients;


    IngredientsAdapter(Context context, JSONArray ingredients, DetailFragment detailFragment) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject data = ingredients.getJSONObject(position);
            holder.ingredient.setText(data.getString("ingredient"));
            holder.mesure.setText(data.getString("quantity")+ " "+data.getString("measure"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (ingredients!=null) {
            return ingredients.length();
        }else{
            return  0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_ingredient)
        TextView ingredient;
        @BindView(R.id.mesure)
        TextView mesure;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
