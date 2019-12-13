package com.oolivares.bakingapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ingredientsStepsFragment extends Fragment implements StepsAdapter.StepClickListener {
    private JSONArray ingredients;
    private JSONArray steps;
    private StepsAdapter stepsAdapter;
    private OnStepClick2 onStepClick2;

    @BindView(R.id.materialCardView)
    CardView cardView;

    @BindView(R.id.rv_steps1)
    RecyclerView rv_steps;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.ingredientsstep, container, false);
        ButterKnife.bind(this,rootView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv_steps.setLayoutManager(llm);
        stepsAdapter = new StepsAdapter(getContext(),steps,this,this);
        rv_steps.setAdapter(stepsAdapter);
        onStepClick2 = (OnStepClick2) getContext();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> listItems = new ArrayList<String>();

                for (int i = 0 ; i<ingredients.length(); i++){
                    try {
                        JSONObject item = (JSONObject) ingredients.get(i);
                        listItems.add(item.getString("ingredient") + " " + item.getInt("quantity") + " "+ item.getString("measure"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                final CharSequence[] charSequenceItems = listItems.toArray(new CharSequence[listItems.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Ingredients")
                        .setItems(charSequenceItems,null);
                builder.create();
                builder.show();
            }
        });
        return rootView;
    }

    public void setRecipie(Recetas recetas) {
        try {
            ingredients = new JSONArray(recetas.getIngredients());
            steps = new JSONArray(recetas.getSteps());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        stepsAdapter = new StepsAdapter(getContext(),steps,this,this);
        if (rv_steps != null) {
            rv_steps.setAdapter(stepsAdapter);
        }
    }

    @Override
    public void onStepClick(int index) {
        onStepClick2.onStepClick(index);
    }

    public interface OnStepClick2 {
        void onStepClick(int index);
    }
}
