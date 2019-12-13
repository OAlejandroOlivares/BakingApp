package com.oolivares.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements ingredientsStepsFragment.OnStepClick2{
    private boolean mTwoPane;
    private Recetas receta;
    private DetailFragment detailFragment;
    private int mIndex=-1;


    @Nullable
    @BindView(R.id.fragment_separator)
    View separator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipie_step_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (savedInstanceState != null){
            receta  = savedInstanceState.getParcelable("receta");
            detailFragment = (DetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, "detail");
            mIndex = savedInstanceState.getInt("mindex");
        }
        if (intent.hasExtra("id")){
            try {
                JSONArray steps = new JSONArray(intent.getStringExtra("steps"));
                JSONArray ingrediets = new JSONArray(intent.getStringExtra("ingredients"));
                receta = new Recetas(intent.getIntExtra("id",0),intent.getStringExtra("name"), ingrediets.toString(),steps.toString(),intent.getIntExtra("servings",0),intent.getStringExtra("image"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(separator != null) {
            if (receta != null){
                ingredientsStepsFragment stepsFragment = (ingredientsStepsFragment) getSupportFragmentManager().findFragmentByTag("ingsteps");
                stepsFragment.setRecipie(receta);
                //getSupportFragmentManager().beginTransaction().replace(R.id.ingredientsSteps,stepsFragment).commit();
            }
            mTwoPane = true;
            detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag("detail");
            detailFragment.init(receta);
        } else {
            mTwoPane = false;
            ingredientsStepsFragment stepsFragment = new ingredientsStepsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,stepsFragment,"ingsteps").commit();
            stepsFragment.setRecipie(receta);
            if (detailFragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,detailFragment,"detail").addToBackStack(null).commit();
            }
        }
    }

    @Override
    public void onStepClick(int index) {
        if(mTwoPane) {
            detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag("detail");
            detailFragment.load(index);
        }else{
            detailFragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,detailFragment).addToBackStack(null).commit();
            try {
                detailFragment.setSteps(new JSONArray(receta.getSteps()),index);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("receta",receta);
        if (getSupportFragmentManager().getBackStackEntryCount()>0) {
            getSupportFragmentManager().putFragment(outState, "detail", detailFragment);
            outState.putInt("mindex",detailFragment.getindex());
        }
    }
}
