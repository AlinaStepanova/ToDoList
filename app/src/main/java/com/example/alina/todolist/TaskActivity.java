package com.example.alina.todolist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.enums.ActivityRequest;
import com.example.alina.todolist.enums.BundleKey;

public class TaskActivity extends BaseTimerActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView descriptionTask;
    private TextView categoryName;
    private TextView latitude;
    private TextView longitude;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private Task task;
    private String nameTransition, descriptionTransition, categoryTransition, latTransition, lonTransition;

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
        latitude = (TextView) findViewById(R.id.latitudeTask);
        longitude = (TextView) findViewById(R.id.longitudeTask);
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
    }

    private void setTransitions(){
        task = getIntent().getExtras().getParcelable(BundleKey.TASK.name());
        nameTransition = getIntent().getExtras().getString(BundleKey.NAME_TRANSITION.name());
        descriptionTransition = getIntent().getExtras().getString(BundleKey.DESCRIPTION_TRANSITION.name());
        categoryTransition = getIntent().getExtras().getString(BundleKey.CATEGORY_TRANSITION.name());
        latTransition = getIntent().getExtras().getString(BundleKey.LATITUDE_TRANSITION.name());
        lonTransition = getIntent().getExtras().getString(BundleKey.LOGITUDE_TRANSITION.name());
        if(nameTransition != null && descriptionTransition != null && categoryTransition != null
                && latTransition != null && lonTransition != null){
            ViewCompat.setTransitionName(collapsingToolbarLayout, nameTransition);
            ViewCompat.setTransitionName(descriptionTask, descriptionTransition);
            ViewCompat.setTransitionName(categoryName, categoryTransition);
            ViewCompat.setTransitionName(latitude,latTransition);
            ViewCompat.setTransitionName(longitude,lonTransition);
        }
    }

    private void fillData(){
        if(task != null){
            collapsingToolbarLayout.setTitle(task.getName());
            descriptionTask.setText(task.getDescription() + "\n" + "Description");
            categoryName.setText(task.getCategory().getName());
            latitude.setText(String.valueOf(task.getLatitude()));
            longitude.setText(String.valueOf(task.getLongitude()));
            ((GradientDrawable)categoryName.getBackground()).setStroke(8, task.getCategory().getColor());
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (ActivityRequest.values()[requestCode]){
            case EDIT_TASK:
            case WATCH_TASK:
                if(resultCode == Activity.RESULT_OK){
                    task = data.getParcelableExtra(BundleKey.TASK.name());
                    fillData();
                }
                break;

        }
    }
}
