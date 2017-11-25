package com.example.alina.todolist.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.alina.todolist.R;
import com.example.alina.todolist.entities.SubTask;
import com.example.alina.todolist.entities.TaskObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonid on 23.11.2017.
 */

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskAdapter.SubTaskHolder> {

    private List<SubTask> subTaskList;

    public SubTaskAdapter(){
        subTaskList = new ArrayList<>();
    }

    public void addNewSubTask(SubTask subTask){
        subTaskList.add(subTask);
        notifyItemInserted(subTaskList.size());
    }

    public void addAllSubTask(List<SubTask> subTasks){
        for (SubTask x : subTasks){
            addNewSubTask(x);
        }
    }

    @Override
    public SubTaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubTaskHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_task, parent, false));
    }

    @Override
    public void onBindViewHolder(SubTaskHolder holder, int position) {
        holder.onBind(subTaskList.get(position));
    }

    @Override
    public int getItemCount() {
        return subTaskList.size();
    }

    public List<SubTask> getSubTaskList() {
        return subTaskList;
    }

    class SubTaskHolder extends RecyclerView.ViewHolder {

        private TextView subTaskDescription;
        private SwitchCompat subTaskStatusSwitcher;

        SubTaskHolder(View itemView) {
            super(itemView);
            subTaskDescription = (TextView) itemView.findViewById(R.id.itemSubTaskDesc);
            subTaskStatusSwitcher = (SwitchCompat) itemView.findViewById(R.id.itemSubTaskStatus);
        }

        void onBind(final SubTask subTask){
            subTaskDescription.setText(subTask.getDescription());
            subTaskStatusSwitcher.setChecked(subTask.isDone());
            subTaskStatusSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    subTask.setStatus(isChecked ? TaskObject.TaskStatus.DONE : TaskObject.TaskStatus.NEW);
                }
            });
        }
    }
}
