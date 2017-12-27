package com.example.alina.todolist.data;

import android.support.annotation.NonNull;

import com.example.alina.todolist.data.database.OnEntitiesLoad;
import com.example.alina.todolist.entities.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseDataSource implements DataSource {

    private static final String TASKS ="Tasks";

    private DatabaseReference database;

    public FirebaseDataSource(){
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void loadTaskList(@NonNull final OnEntitiesLoad<Task> onTasksLoaded) {
        database.child(TASKS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Task>> typeIndicator
                        = new GenericTypeIndicator<HashMap<String, Task>>() {};
                HashMap<String, Task> results = dataSnapshot.getValue(typeIndicator);
                if(results != null) {
                    onTasksLoaded.onSuccess(new ArrayList<>(results.values()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void createTask(@NonNull Task task) {
        database.child(TASKS)
                .child(String.valueOf(task.getId()))
                .setValue(task);
    }
}