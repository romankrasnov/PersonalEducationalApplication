package com.smallredtracktor.yourpersonaleducationalapplication.main.Views.RVAdapters;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Test;

import java.util.List;

public class TrainingListRVAdapter extends RecyclerView.Adapter<TrainingListRVAdapter.TrainingListViewHolder> {

    private List<Test> testList;
    private final int height = Resources.getSystem().getDisplayMetrics().heightPixels / 8;

    public TrainingListRVAdapter(List<Test> testList){
        this.testList = testList;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public TrainingListRVAdapter.TrainingListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.training_list_item, viewGroup, false);
        ((ConstraintLayout) v).setMaxHeight(height);
        return new TrainingListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingListViewHolder trainingListViewHolder, int i) {
        Test item = testList.get(i);
        trainingListViewHolder.trainingNameView.setText(item.getName());
        trainingListViewHolder.trainingItemProgressView.setProgressWithAnimation(item.getProgress());
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    class TrainingListViewHolder extends RecyclerView.ViewHolder{
        TextView trainingNameView;
        CircularProgressBar trainingItemProgressView;
        TrainingListViewHolder(@NonNull View itemView) {
            super(itemView);
            trainingNameView = itemView.findViewById(R.id.trainingItemNameView);
            trainingItemProgressView = itemView.findViewById(R.id.trainingItemProgressView);
        }
    }
}
