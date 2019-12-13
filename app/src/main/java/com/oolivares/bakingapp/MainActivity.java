package com.oolivares.bakingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements RecipieListFragment.OnItemClickListener{
    public static final String ACTION_RECIPIE_DETAIL = "com.oolivares.bakingapp.action.detail_list" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void onItemSelected(Recetas recetas) {
        //DetailFragment detailFragment = new DetailFragment();
        //detailFragment.setRecipie(recetas);
        //getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment,detailFragment).commit();
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra("id",recetas.getId());
        intent.putExtra("name",recetas.getName());
        intent.putExtra("ingredients",recetas.getIngredients().toString());
        intent.putExtra("steps",recetas.getSteps().toString());
        intent.putExtra("servings",recetas.getServings());
        intent.putExtra("image",recetas.getImage());
        startActivity(intent);
    }
}
