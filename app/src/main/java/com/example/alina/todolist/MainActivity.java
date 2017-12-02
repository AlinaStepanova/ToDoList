package com.example.alina.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.alina.todolist.adapters.TaskFragmentPagerAdapter;
import com.example.alina.todolist.data.IDataSource;
import com.example.alina.todolist.data.SharedPreferencesDataSource;
import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.enums.ActivityRequest;
import com.example.alina.todolist.enums.BundleKey;
import com.example.alina.todolist.fragments.TaskListFragment;

public class MainActivity extends AppCompatActivity implements TaskListFragment.TaskFragmentCallback {

    private FloatingActionButton createTaskButton;
    private IDataSource dataSource;
    private TabLayout mainTabLayout;
    private ViewPager mainViewPager;
    private TaskFragmentPagerAdapter taskFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCreateTaskButton();
        dataSource = new SharedPreferencesDataSource(getApplicationContext());
        initViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int size = (dataSource.getTaskList() == null) ? 0 : dataSource.getTaskList().size();
        Toast.makeText(this, String.format("%d task%s", size, size > 0 ? "s" : ""),
                Toast.LENGTH_SHORT).show();
    }

    private void initCreateTaskButton() {
        createTaskButton = (FloatingActionButton) findViewById(R.id.createTaskButton);
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
                intent.putExtra(BundleKey.TASK.name(), task);
                startActivityForResult(intent, ActivityRequest.CREATE_TASK.ordinal());
            }
        });
    }

    private void initViewPager(){
        taskFragmentAdapter = new TaskFragmentPagerAdapter(this, getSupportFragmentManager(), dataSource.getTaskList());
        mainTabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);
        mainTabLayout.setupWithViewPager(mainViewPager);
        mainViewPager.setAdapter(taskFragmentAdapter);
    }

    private void forceInitPager(){
        int lastTabPosition = mainTabLayout.getSelectedTabPosition();
        taskFragmentAdapter = new TaskFragmentPagerAdapter(this, getSupportFragmentManager(), dataSource.getTaskList());
        mainViewPager.setAdapter(taskFragmentAdapter);
        mainTabLayout.setScrollPosition(lastTabPosition, 0, false);
        mainViewPager.setCurrentItem(lastTabPosition);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ActivityRequest.CREATE_TASK.ordinal()) {
            if (resultCode == Activity.RESULT_OK) {
                Task task = data.getParcelableExtra(BundleKey.TASK.name());
                if (task != null) {
                    dataSource.createTask(task);
                    forceInitPager();
                }
            }
        } else if (requestCode == ActivityRequest.UPDATE_TASK.ordinal()){
            if (resultCode == RESULT_OK){
                Task task = data.getParcelableExtra(BundleKey.TASK.name());
                if (task != null){
                    dataSource.updateTask(task);
                    forceInitPager();
                }
            }
        }else super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemLongClick(Task task) {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        intent.putExtra(BundleKey.TASK.name(), task);
        startActivityForResult(intent, ActivityRequest.UPDATE_TASK.ordinal());
    }

    @Override
    public void onItemClick(Task task) {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(BundleKey.TASK.name(), task);
        startActivity(intent);
        /*View nameTextView = findViewById(R.id.nameTextView);
        View descriptionTextView = findViewById(R.id.descriptionTextView);
        View categoryTextView = findViewById(R.id.categoryTextView);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                new Pair<>(nameTextView, ViewCompat.getTransitionName(nameTextView)),
                new Pair<>(descriptionTextView, ViewCompat.getTransitionName(descriptionTextView))
                //new Pair<>(categoryTextView, ViewCompat.getTransitionName(categoryTextView))
        );
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(BundleKey.TASK.name(), task);
        intent.putExtra(BundleKey.NAME_TRANSITION.name(), ViewCompat.getTransitionName(nameTextView));
        intent.putExtra(BundleKey.DESCRIPTION_TRANSITION.name(), ViewCompat.getTransitionName(descriptionTextView));
        intent.putExtra(BundleKey.CATEGORY_TRANSITION.name(), ViewCompat.getTransitionName(categoryTextView));
        ActivityCompat.startActivity(this, intent, activityOptionsCompat.toBundle());
        ActivityCompat.startActivity();*/

    }
}
