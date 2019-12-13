package com.oolivares.bakingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.MyViewHolder> {

    private JSONArray steps;
    final private StepsAdapter.StepClickListener stepClickListener;

    StepsAdapter(Context context, JSONArray steps, ingredientsStepsFragment detailFragment, StepClickListener stepClickListener) {
        this.steps = steps;
        this.stepClickListener = stepClickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            JSONObject data = steps.getJSONObject(position);
            holder.description.setText(data.getString("shortDescription"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (steps!=null) {
            return steps.length();
        }else{
            return 0;
        }
    }

    public interface StepClickListener {
        void onStepClick(int index);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_step_name)
        TextView description;
        @BindView(R.id.iv_step)
        ImageView button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            stepClickListener.onStepClick(getAdapterPosition());
        }
    }
}
