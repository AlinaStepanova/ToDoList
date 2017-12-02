package com.example.alina.todolist.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alina.todolist.R;
import com.example.alina.todolist.entities.Task;

import java.util.List;

/**
 * Created by Alina on 14.11.2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Task> tasks;

    public TaskAdapter(@NonNull List<Task> tasks) {
        super();
        this.tasks = tasks;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case R.layout.item_task:
                viewHolder = new TaskViewHolderRunning(view);
                break;
            case R.layout.item_task_finished:
                viewHolder = new TaskViewHolderFinished(view);
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        int state = R.layout.item_task;
        if (tasks.get(position).isExpire()) {
            state = R.layout.item_task_finished;
        }
        return state;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TaskViewHolderRunning) {
            TaskViewHolderRunning running = (TaskViewHolderRunning) holder;
            running.bind(tasks.get(position));
        } else if(holder instanceof TaskViewHolderFinished){
            TaskViewHolderFinished finished = (TaskViewHolderFinished) holder;
            finished.bind(tasks.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return (tasks == null) ? 0 : tasks.size();
    }

    public void add(Task task) {
        tasks.add(task);
        notifyItemInserted(tasks.size() - 1);
    }

    private class TaskViewHolderRunning extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;

        TaskViewHolderRunning(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameTextView);
            description = (TextView) itemView.findViewById(R.id.descriptionTextView);
        }

        void bind(Task task) {
            name.setText(task.getName());
            description.setText(task.getDescription());
        }
    }

    private class TaskViewHolderFinished extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;

        TaskViewHolderFinished(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameTextViewFinished);
            description = (TextView) itemView.findViewById(R.id.descriptionTextViewFinished);
        }

        void bind(Task task) {
            name.setText(task.getName());
            description.setText(task.getDescription());
        }
    }
}
