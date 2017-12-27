package com.example.alina.todolist.activities;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alina.todolist.R;
import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.enums.ActivityRequest;
import com.example.alina.todolist.enums.BundleKey;
import com.example.alina.todolist.enums.NotificationAction;
import com.example.alina.todolist.fragments.MapFragment;

public class TaskActivity extends BaseLocationActivity implements MapFragment.OnCloseMapFragment {

    private static final String KEY_NOTIFICATION_TYPE = "KEY_TYPE_NOTIFICATION";
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView descriptionTask;
    private TextView categoryName;
    private TextView latitude;
    private TextView longitude;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private Task task;
    private String nameTransition, descriptionTransition, categoryTransition, latTransition, lonTransition;
    private Button showOnMapButton, makeNotification, removeNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        if (getIntent().getExtras() == null) {
            finish();
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {

            initUi();

            if(bundle.containsKey(KEY_NOTIFICATION_TYPE)) {

                task = bundle.getParcelable(BundleKey.TASK.name());

                if (bundle.getString(KEY_NOTIFICATION_TYPE).equals(
                        NotificationAction.TASK_ACTIVITY.name())) {
                    Toast.makeText(this, "Task activity", Toast.LENGTH_SHORT).show();
                } else {
                    if (bundle.getString(KEY_NOTIFICATION_TYPE).equals(
                            NotificationAction.MAP_FRAGMENT.name())) {
                        startMapFragment(task);
                    }
                }
            }

        }
    }

    private void buildNotification() {
        Notification notification = new NotificationCompat.Builder(this, "channel")
                .setSmallIcon(R.drawable.ic_arrow_downward_black_24dp)
                .addAction(makeAction(R.drawable.ic_assignment_black_24dp,
                        NotificationAction.TASK_ACTIVITY))
                .addAction(makeAction(R.drawable.ic_map_black_24dp,
                        NotificationAction.MAP_FRAGMENT))
                .setContentTitle(task.getName())
                .setContentText(task.getDescription()).build();

        NotificationManagerCompat.from(this).notify(task.getId(),notification);
    }

    @NonNull
    private NotificationCompat.Action makeAction(@DrawableRes int icon, NotificationAction notificationActionType) {
        Intent intent = new Intent(this, TaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_NOTIFICATION_TYPE, String.valueOf(notificationActionType.name()));
        bundle.putParcelable(BundleKey.TASK.name(), task);
        intent.putExtra(KEY_NOTIFICATION_TYPE, notificationActionType.name());
        intent.putExtra(BundleKey.TASK.name(), task);
        intent.putExtras(bundle);
        Log.d("in_bundle", "int action: "+ intent.getStringExtra(KEY_NOTIFICATION_TYPE));

        PendingIntent donePendingIntent = PendingIntent.getActivity(this, icon,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return new NotificationCompat.Action(icon, getString(notificationActionType.getButtonTitle()), donePendingIntent);
    }

    private void startMapFragment(Task task) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKey.TASK.name(), task);
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(bundle);

            floatingActionButton.setVisibility(View.INVISIBLE);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_rigth,
                        R.anim.slide_in_left, R.anim.slide_out_rigth)
                .replace(R.id.fragmentMap, mapFragment)
                .addToBackStack("Map")
                .commit();
    }

    private void initUi() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        descriptionTask = findViewById(R.id.descriptionTask);
        categoryName = findViewById(R.id.categoryName);
        floatingActionButton = findViewById(R.id.fab);
        latitude = findViewById(R.id.latitudeTask);
        longitude = findViewById(R.id.longitudeTask);
        showOnMapButton = findViewById(R.id.showOnMap);
        makeNotification = findViewById(R.id.makeNotification);
        removeNotification = findViewById(R.id.removeNotification);

        setTransitions();
        fillData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskActivity.this,
                        CreateTaskActivity.class);
                intent.putExtra(BundleKey.TASK.name(), task);
                setNeedCheckCurrentTime(false);
                startActivityForResult(intent, ActivityRequest.EDIT_TASK.ordinal());
            }
        });


        showOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMapFragment(task);
            }
        });

        makeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildNotification();
            }
        });

        removeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationManager notificationManager = (NotificationManager)
                        getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(task.getId());

            }
        });

    }

    private void setTransitions() {
        task = getIntent().getExtras().getParcelable(BundleKey.TASK.name());
        nameTransition = getIntent().getExtras().getString(BundleKey.NAME_TRANSITION.name());
        descriptionTransition = getIntent().getExtras().getString(BundleKey.DESCRIPTION_TRANSITION.name());
        categoryTransition = getIntent().getExtras().getString(BundleKey.CATEGORY_TRANSITION.name());
        latTransition = getIntent().getExtras().getString(BundleKey.LATITUDE_TRANSITION.name());
        lonTransition = getIntent().getExtras().getString(BundleKey.LOGITUDE_TRANSITION.name());
        Log.d("in_bundle","set Trans: "+ latTransition+" "+lonTransition+" "+nameTransition);

        if (nameTransition != null && descriptionTransition != null && categoryTransition != null
                && latTransition != null && lonTransition != null) {
            ViewCompat.setTransitionName(collapsingToolbarLayout, nameTransition);
            ViewCompat.setTransitionName(descriptionTask, descriptionTransition);
            ViewCompat.setTransitionName(categoryName, categoryTransition);
            ViewCompat.setTransitionName(latitude, latTransition);
            ViewCompat.setTransitionName(longitude, lonTransition);
        }
    }

    private void fillData() {
        if (task != null) {
            collapsingToolbarLayout.setTitle(task.getName());
            descriptionTask.setText(task.getDescription());
            categoryName.setText(task.getCategory().getName());
            Log.d("in_bundle","fill data: "+ task.getLatitude()+" "+task.getLongitude()+" ");
            latitude.setText(String.valueOf(task.getLatitude()));
            longitude.setText(String.valueOf(task.getLongitude()));
            ((GradientDrawable) categoryName.getBackground()).setStroke(8, task.getCategory().getColor());
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (ActivityRequest.values()[requestCode]) {
            case EDIT_TASK:
            case WATCH_TASK:
                if (resultCode == Activity.RESULT_OK) {
                    task = data.getParcelableExtra(BundleKey.TASK.name());
                    fillData();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClose() {
        floatingActionButton.setVisibility(View.VISIBLE);
    }


}
