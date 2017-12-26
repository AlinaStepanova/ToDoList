package com.example.alina.todolist.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.alina.todolist.AppMain;
import com.example.alina.todolist.R;
import com.example.alina.todolist.adapters.TaskFragmentPagerAdapter;
import com.example.alina.todolist.data.repository.DatabaseSource;
import com.example.alina.todolist.data.db.DataLoadCallback;
import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.enums.ActivityRequest;
import com.example.alina.todolist.enums.BundleKey;
import com.example.alina.todolist.listeners.OnDataChangedListener;
import com.example.alina.todolist.ui.fragment.TaskListFragment;

import java.util.List;

public class MainActivity extends BaseActivity implements TaskListFragment.TaskFragmentCallback, OnDataChangedListener{

    private FloatingActionButton createTaskButton;
//    private IDataSource dataSource;
    private TabLayout mainTabLayout;
    private ViewPager mainViewPager;
    private TaskFragmentPagerAdapter taskFragmentAdapter;
    private DatabaseSource helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCreateTaskButton();

//        dataSource = new FileDataSource(this, this);
        helper = new DatabaseSource(this);

        initViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        int size = (dataSource.getTaskList() == null) ? 0 : dataSource.getTaskList().size();
//        Toast.makeText(this, String.format("%d task%s", size, size > 0 ? "s" : ""),
//                Toast.LENGTH_SHORT).show();
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
        helper.getAllTask(new DataLoadCallback<List<Task>>() {
            @Override
            public void onSuccess(@Nullable List<Task> data) {
                if (data != null && !data.isEmpty()){
                    taskFragmentAdapter = new TaskFragmentPagerAdapter(MainActivity.this, getSupportFragmentManager(), data);
                    mainTabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
                    mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);
                    mainTabLayout.setupWithViewPager(mainViewPager);
                    mainViewPager.setAdapter(taskFragmentAdapter);
                }
            }
        }, getSupportLoaderManager());
        /*taskFragmentAdapter = new TaskFragmentPagerAdapter(this, getSupportFragmentManager(), dataSource.getTaskList());
        mainTabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);
        mainTabLayout.setupWithViewPager(mainViewPager);
        mainViewPager.setAdapter(taskFragmentAdapter);*/
    }

    private void forceInitPager(){
//        int lastTabPosition = mainTabLayout.getSelectedTabPosition();
        helper.getAllTask(new DataLoadCallback<List<Task>>() {
            @Override
            public void onSuccess(@Nullable List<Task> data) {
                int lastTabPosition = mainTabLayout.getSelectedTabPosition();
                taskFragmentAdapter = new TaskFragmentPagerAdapter(MainActivity.this, getSupportFragmentManager(), data);
                mainViewPager.setAdapter(taskFragmentAdapter);
                mainTabLayout.setScrollPosition(lastTabPosition, 0, false);
                mainViewPager.setCurrentItem(lastTabPosition);
            }
        }, getSupportLoaderManager());
        /*taskFragmentAdapter = new TaskFragmentPagerAdapter(this, getSupportFragmentManager(), dataSource.getTaskList());
        mainViewPager.setAdapter(taskFragmentAdapter);
        mainTabLayout.setScrollPosition(lastTabPosition, 0, false);
        mainViewPager.setCurrentItem(lastTabPosition);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ActivityRequest.CREATE_TASK.ordinal()) {
            if (resultCode == Activity.RESULT_OK) {
                Task task = data.getParcelableExtra(BundleKey.TASK.name());
                if (task != null) {
//                    dataSource.createTask(task);
                    helper.createTask(task);
                    AppMain.getRepository().createNewTask(task);
                    forceInitPager();
                }
            }
        } else if (requestCode == ActivityRequest.UPDATE_TASK.ordinal()){
            if (resultCode == RESULT_OK){
                Task task = data.getParcelableExtra(BundleKey.TASK.name());
                if (task != null){
//                    dataSource.updateTask(task);
//                    helper.updateTask(task);
                    AppMain.getRepository().updateTask(task);
                    forceInitPager();
                }
            }
        }else super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onItemClick(Task task) {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(BundleKey.TASK.name(), task);
        startActivity(intent);
    }

    @Override
    public void onEditTaskClick(Task task) {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        intent.putExtra(BundleKey.TASK.name(), task);
        startActivityForResult(intent, ActivityRequest.UPDATE_TASK.ordinal());
    }

    @Override
    public void notifyDataChanged() {
        forceInitPager();
    }
}
