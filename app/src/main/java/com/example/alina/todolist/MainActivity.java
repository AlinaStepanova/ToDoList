package com.example.alina.todolist;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.alina.todolist.adapters.TaskAdapter;
import com.example.alina.todolist.data.IDataSource;
import com.example.alina.todolist.data.SharedPreferencesDataSource;
import com.example.alina.todolist.decorators.DividerItemDecoration;
import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.enums.ActivityRequest;
import com.example.alina.todolist.enums.BundleKey;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton createTaskButton;
    private IDataSource dataSource;
    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCreateTaskButton();
        dataSource = new SharedPreferencesDataSource(getApplicationContext());
        initTaskRecycler();
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

    private void initTaskRecycler() {
        int margin = (int) getResources().getDimension(R.dimen.min_margin);
        GridLayoutManager gridLayoutManager = getGridLayoutManager();
        setLayoutManager(gridLayoutManager);
        taskRecyclerView.addItemDecoration(new DividerItemDecoration(this, margin));
    }

    private void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        taskRecyclerView = (RecyclerView) findViewById(R.id.taskRecyclerView);
        taskRecyclerView.setLayoutManager(layoutManager);
        taskAdapter = new TaskAdapter(dataSource.getTaskList(), new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
                intent.putExtra(BundleKey.TASK.name(), task);
                startActivityForResult(intent, ActivityRequest.UPDATE_TASK.ordinal());
            }
        });
        taskRecyclerView.setAdapter(taskAdapter);
    }

    @NonNull
    private GridLayoutManager getGridLayoutManager() {
        return new GridLayoutManager(this, getResources()
                    .getInteger(R.integer.column_count));
    }

    @NonNull
    private LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_change_layout: {
                changeLayout();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (ActivityRequest.values()[requestCode]) {
            case CREATE_TASK:
                if (resultCode == Activity.RESULT_OK) {
                    Task task = data.getParcelableExtra(BundleKey.TASK.name());
                    if (task != null) {
                        dataSource.createTask(task);
                        taskAdapter.add(task);
                    }
                }
                break;
            case UPDATE_TASK:
                if(resultCode == Activity.RESULT_OK) {
                    Task task = data.getParcelableExtra(BundleKey.TASK.name());
                    if (task != null) {
                        dataSource.updateTask(task, taskAdapter.getData().indexOf(task));
                        taskAdapter.update(task);
                    }
                }
                break;
        }
    }

    private void changeLayout() {
        if (taskRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
            setLayoutManager(getLinearLayoutManager());
        } else {
            setLayoutManager(getGridLayoutManager());
        }
    }
}
