package com.example.alina.todolist.ui.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alina.todolist.R;
import com.example.alina.todolist.adapters.SubTaskAdapter;
import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.enums.ActivityRequest;
import com.example.alina.todolist.enums.BundleKey;
import com.example.alina.todolist.enums.NotificationChannels;

public class TaskActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView descriptionTask;
    private TextView categoryName;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private Task task;
    private String nameTransition, descriptionTransition, categoryTransition;
    private RecyclerView subTaskRecycler;
    private SubTaskAdapter subTaskAdapter;
    private Button showOnMapButton;
    private Button sendNotification;
    private Button removeNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        if(getIntent().getExtras() == null) {
            finish();
        }
        initUi();
    }

    private void initUi(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        descriptionTask = (TextView) findViewById(R.id.descriptionTask);
        categoryName = (TextView) findViewById(R.id.categoryName);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        showOnMapButton = findViewById(R.id.showOnMapButton);
        sendNotification = findViewById(R.id.sendNotification);
        removeNotification = findViewById(R.id.removeNotification);
        subTaskRecycler = findViewById(R.id.subTaskRecycler);
        subTaskRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        subTaskAdapter = new SubTaskAdapter(this);
        subTaskRecycler.setAdapter(subTaskAdapter);
        setTransitions();
        fillData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskActivity.this, CreateTaskActivity.class);
                intent.putExtra(BundleKey.TASK.name(), task);
                startActivityForResult(intent, ActivityRequest.EDIT_TASK.ordinal());
            }
        });

        showOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (task.getLocation() != null) {
                    Intent mapIntent = new Intent(TaskActivity.this, MapsActivity.class);
                    mapIntent.putExtra(BundleKey.TASK.name(), task);
                    startActivity(mapIntent);
                }
            }
        });

        sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotificationClick();
            }
        });

        removeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeNotificationClick();
            }
        });
    }

    private void setTransitions(){
        task = getIntent().getExtras().getParcelable(BundleKey.TASK.name());
        nameTransition = getIntent().getExtras().getString(BundleKey.NAME_TRANSITION.name());
        descriptionTransition = getIntent().getExtras().getString(BundleKey.DESCRIPTION_TRANSITION.name());
        categoryTransition = getIntent().getExtras().getString(BundleKey.CATEGORY_TRANSITION.name());
        if(nameTransition != null && descriptionTransition != null && categoryTransition != null){
            ViewCompat.setTransitionName(collapsingToolbarLayout, nameTransition);
            ViewCompat.setTransitionName(descriptionTask, descriptionTransition);
            ViewCompat.setTransitionName(categoryName, categoryTransition);
        }
    }

    private void fillData(){
        if(task != null){
            collapsingToolbarLayout.setTitle(task.getName());
            descriptionTask.setText(task.getDescription());
            if (task.getCategory() != null) {
                categoryName.setText(task.getCategory().getName());
                ((GradientDrawable) categoryName.getBackground()).setStroke(8, task.getCategory().getColor());
            }
            if (task.getSubTasks() != null && !task.getSubTasks().isEmpty()){
                subTaskAdapter.addAllSubTask(task.getSubTasks());
            }

        } else {
            finish();
        }
    }

    private void sendNotificationClick(){
        Notification notification = new NotificationCompat.Builder(this, NotificationChannels.TASK.name())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(task.getName())
                .setContentText(task.getDescription())
                .addAction(getAction(getString(R.string.open_task), R.drawable.ic_open_task, TaskActivity.class))
                .addAction(getAction(getString(R.string.open_map), R.drawable.ic_map, MapsActivity.class))
                .build();
        // TODO: set task id instead hardcode
        NotificationManagerCompat.from(this).notify(0,notification);
    }

    private void removeNotificationClick(){
        // TODO: close by task id
        NotificationManagerCompat.from(this).cancel(0);
    }

    private NotificationCompat.Action getAction(String actionName, @DrawableRes int icon, Class openActivity){
        Intent intent = new Intent(this, openActivity);
        intent.putExtra(BundleKey.TASK.name(), task);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, icon, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return new NotificationCompat.Action(icon, actionName, pendingIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (ActivityRequest.values()[requestCode]){
            case EDIT_TASK:
                if(resultCode == Activity.RESULT_OK){
                    task = data.getParcelableExtra(BundleKey.TASK.name());
                    fillData();
                }
                break;
        }
    }
}
