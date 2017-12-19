package com.example.alina.todolist.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.alina.todolist.entities.Category;
import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.entities.User;
import com.example.alina.todolist.listeners.OnDataChangedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseDataSource implements IDataSource {

    private static final String TASKS = "tasks";
    private static final String CATEGORIES = "categories";
    private static final String SUBTASKS = "subtasks";

    private String currentUserId;
    private ArrayList<Task> tasks;
    private ArrayList<Category> categories;
    private DatabaseReference database;
    private OnDataChangedListener onDataChangedListener;

    public FirebaseDataSource(final OnDataChangedListener onDataChangedListener){
        this.onDataChangedListener = onDataChangedListener;
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        tasks = new ArrayList<>();
        categories = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference();
        database.child(TASKS).child(currentUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<HashMap<String, Task>> typeIndicator
                                = new GenericTypeIndicator<HashMap<String, Task>>() {};
                        HashMap<String, Task> results = dataSnapshot.getValue(typeIndicator);
                        if(results != null) {
                            tasks.clear();
                            tasks.addAll(results.values());
                            if(onDataChangedListener != null) {
                                onDataChangedListener.notifyDataChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        database.child(CATEGORIES).child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Category>> typeIndicator
                        = new GenericTypeIndicator<HashMap<String, Category>>() {
                };
                HashMap<String, Category> results = dataSnapshot.getValue(typeIndicator);
                if (results != null) {
                    categories.clear();
                    categories.addAll(results.values());
                    if(onDataChangedListener != null) {
                        onDataChangedListener.notifyDataChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public User getCurrentUser() {
        return null;
    }

    @Override
    public ArrayList<User> getUserList() {
        return null;
    }

    @Override
    public ArrayList<Task> getTaskList() {
        return tasks;
    }

    @Override
    public ArrayList<Category> getCategoryList() {
        return categories;
    }

    @Override
    public boolean setCurrentUser(@NonNull User user) {
        return false;
    }

    @Override
    public boolean createTask(@NonNull Task task) {
        database.child(TASKS).child(currentUserId).push().setValue(task);
        return tasks.add(task);
    }

    @Override
    public boolean createCategory(@NonNull Category category) {
        database.child(CATEGORIES).child(currentUserId).push().setValue(category);
        return categories.add(category);
    }

    @Override
    public boolean addUser(@NonNull User user) {
        return false;
    }

    @Override
    public boolean updateTask(@NonNull Task task, int index) {
        return false;
    }

    @Override
    public boolean updateTask(@NonNull Task task) {
        return false;
    }

    @Nullable
    @Override
    public Category getCategoryById(long id) {
        Category returnCategory = null;
        for(Category category : categories){
            if(category.getId() == id){
                returnCategory = category;
                break;
            }
        }
        return returnCategory;
    }

    @Override
    public boolean isNameFreeForCategory(String name) {
        boolean result = true;
        for (Category category : categories){
            if (category.getName().equalsIgnoreCase(name)){
                result = false;
                break;
            }
        }
        return result;
    }
}
