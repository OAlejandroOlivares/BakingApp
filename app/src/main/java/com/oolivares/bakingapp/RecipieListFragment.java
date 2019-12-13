package com.oolivares.bakingapp;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipieListFragment extends Fragment implements RecipiesAdapter.RecipieClickListener{

    private ArrayList<Recetas> recipies = new ArrayList<>();
    private RecipiesAdapter myAdapter;
    OnItemClickListener mCallback;

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         final View rootView = inflater.inflate(R.layout.fragment_recipie_list, container, false);
         ButterKnife.bind(this,rootView);
         LinearLayoutManager llm = new LinearLayoutManager(getContext());
         DisplayMetrics displaysize = getContext().getResources().getDisplayMetrics();
         float displaywith = displaysize.widthPixels / displaysize.density;
        float columnWidthDp = 300;
        int columns = (int) (displaywith / columnWidthDp + 0.5);
         GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),columns);
         mRecyclerView.setLayoutManager(gridLayoutManager);
         mRecyclerView.setHasFixedSize(true);
         myAdapter = new RecipiesAdapter(getContext(),recipies,this,this);
         mRecyclerView.setAdapter(myAdapter);
         getRecipies();
         mCallback = (OnItemClickListener) getContext();
         return rootView;
    }

    private void getRecipies() {
        MyAsyncTask myAsyncTask =  new MyAsyncTask(getContext(),this);
        myAsyncTask.execute();
    }


    public void populateUI(JSONArray jsonArray) {
        if (jsonArray != null){
            try {
                recipies.clear();
                for (int i = 0; i<jsonArray.length();i++){
                    JSONObject recipie = jsonArray.getJSONObject(i);
                    recipies.add(new Recetas(recipie.getInt("id"),recipie.getString("name"),recipie.getJSONArray("ingredients").toString(),recipie.getJSONArray("steps").toString(),recipie.getInt("servings"),recipie.getString("image")));
                }
                myAdapter = new RecipiesAdapter(getContext(),recipies,this,this);
                mRecyclerView.setAdapter(myAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getContext(),R.string.no_internet,Toast.LENGTH_LONG).show();
        }
    }

    public interface OnItemClickListener {
        void onItemSelected(Recetas recetas);
    }

    @Override
    public void onRecipieClick(int index) {
        mCallback.onItemSelected(recipies.get(index));
        Log.d("onRecipieClick", (recipies.get(index)).toString());
        WidgetService.startActionUpdateList(getContext(),recipies.get(index).getIngredients(),recipies.get(index).getName());
    }
}
