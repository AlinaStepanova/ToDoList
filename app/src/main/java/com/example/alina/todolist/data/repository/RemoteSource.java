package com.example.alina.todolist.data.repository;


import com.example.alina.todolist.data.db.DataLoadCallback;
import com.example.alina.todolist.data.db.DatabaseSchema;
import com.example.alina.todolist.entities.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RemoteSource implements IRepositorySource{
    private static final String CHILD_TASK = "TASKS";
    private static final String CHILD_SUB_TASK = "Sub_task";
    private static final String CHILD_CATEGORY = "Category";

    private FirebaseDatabase database;

    public RemoteSource(){
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void getAllTask(final DataLoadCallback<List<Task>> callback, android.support.v4.app.LoaderManager manager) {
        database.getReference().child(CHILD_TASK).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Task> taskList = new ArrayList<>();
                for (DataSnapshot x : dataSnapshot.getChildren()){
                    taskList.add(x.getValue(Task.class));
                }
                callback.onSuccess(taskList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    @Override
    public void createTask(Task task) {
        database.getReference().child(CHILD_TASK).push()
                .updateChildren(task.getMapData());
    }

    public void updateTask(Task task){
        // TODO:
        database.getReference().child(CHILD_TASK).orderByChild(DatabaseSchema.Task.ID)
                .equalTo(String.valueOf(task.getId())).getRef().updateChildren(task.getMapData());
    }
}
