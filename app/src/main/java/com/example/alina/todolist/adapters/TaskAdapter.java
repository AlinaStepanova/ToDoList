package com.example.alina.todolist.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.alina.todolist.R;
import com.example.alina.todolist.entities.Task;

import java.util.List;

/**
 * Created by Alina on 14.11.2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    private List<Task> tasks;
    private OnItemClickListener onItemClickListener;

    public TaskAdapter(@NonNull List<Task> tasks, OnItemClickListener onItemClickListener) {
        super();
        this.tasks = tasks;
        this.onItemClickListener = onItemClickListener;
    }

    public List<Task> getData() {
        return tasks;
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
            running.bind(tasks.get(position), onItemClickListener);
        } else if(holder instanceof TaskViewHolderFinished){
            TaskViewHolderFinished finished = (TaskViewHolderFinished) holder;
            finished.bind(tasks.get(position), onItemClickListener);
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

    public void update(Task task) {
        int position = tasks.indexOf(task);
        tasks.set(position, task);
        notifyItemChanged(position);
    }

    private class TaskViewHolderRunning extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;
        TextView subTaskCount;
        FrameLayout container;


        TaskViewHolderRunning(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameTextView);
            description = (TextView) itemView.findViewById(R.id.descriptionTextView);
            subTaskCount = (TextView) itemView.findViewById(R.id.subTaskCount);
            container = (FrameLayout) itemView.findViewById(R.id.subTaskCountContainer);
        }

        void bind(final Task task, final OnItemClickListener onItemClickListener) {
            if(task.getSubTasks().size() > 0) {
                container.setVisibility(View.VISIBLE);
                subTaskCount.setText(String.valueOf(task.getSubTasks().size()));
            }
            name.setText(task.getName());
            description.setText(task.getDescription());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(task);
                }
            });
        }
    }

    private class TaskViewHolderFinished extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;
        TextView subTaskCount;
        FrameLayout container;

        TaskViewHolderFinished(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameTextViewFinished);
            description = (TextView) itemView.findViewById(R.id.descriptionTextViewFinished);
            subTaskCount = (TextView) itemView.findViewById(R.id.subTaskCountFinished);
            container = (FrameLayout) itemView.findViewById(R.id.subTaskCountContainerFinished);
        }

        void bind(final Task task, final OnItemClickListener onItemClickListener) {
            if(task.getSubTasks().size() > 0) {
                container.setVisibility(View.VISIBLE);
                subTaskCount.setText(String.valueOf(task.getSubTasks().size()));
            }
            name.setText(task.getName());
            description.setText(task.getDescription());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(task);
                }
            });
        }
    }
}
