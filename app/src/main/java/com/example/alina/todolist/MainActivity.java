package com.example.alina.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
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

public class MainActivity extends AppCompatActivity{

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ActivityRequest.CREATE_TASK.ordinal()) {
            if (resultCode == Activity.RESULT_OK) {
                Task task = data.getParcelableExtra(BundleKey.TASK.name());
                if (task != null) {
                    dataSource.createTask(task);
                    taskFragmentAdapter.notifyDataSetChanged();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
