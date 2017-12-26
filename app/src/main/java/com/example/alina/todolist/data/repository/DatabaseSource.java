package com.example.alina.todolist.data.repository;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;

import com.example.alina.todolist.data.db.ContentProviderValues;
import com.example.alina.todolist.data.db.DataLoadCallback;
import com.example.alina.todolist.data.db.DatabaseSchema;
import com.example.alina.todolist.data.db.loaders.CategoryLoader;
import com.example.alina.todolist.data.db.loaders.TaskLoader;
import com.example.alina.todolist.data.db.loaders.UserLoader;
import com.example.alina.todolist.entities.Category;
import com.example.alina.todolist.entities.SubTask;
import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.entities.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSource implements IRepositorySource{
    private Context context;


    public DatabaseSource(Context context){
        this.context = context;
    }

    /*
    USER
     */

    public void getUser(DataLoadCallback<List<User>> callback, LoaderManager manager){
        new UserLoader(context, manager).loadData(callback, new Bundle());
    }

    public void createNewUser(User user){
        context.getContentResolver().insert(ContentProviderValues.getContentUri(DatabaseSchema.User.TABLE_NAME),
                user.toContentValues());
    }

    public User getUserById(int userId){
        User user = new User();
        String selection = DatabaseSchema.User.ID  + "=?";
        String[] selectionArgs = new String[]{String.valueOf(userId)};

        Cursor query = context.getContentResolver()
                .query(ContentUris.withAppendedId(ContentProviderValues.getContentUri(DatabaseSchema.User.TABLE_NAME) , userId),
                null, selection, selectionArgs, null);
        try {
            if (query != null && query.moveToFirst()){
                do {
                    user.initByCursor(query);
                }while (query.moveToNext());
            }
        } finally {
            if (query != null)
                query.close();
        }
        return user;
    }

    @Nullable
    public User getExistsUser(String email, String pin){
        User user = null;
        String selection = DatabaseSchema.User.EMAIL + "=? AND " +
                DatabaseSchema.User.PIN + "=?";
        String[] selectionArgs = new String[]{email, pin};

        Cursor cursor = context.getContentResolver()
                .query(ContentProviderValues.getContentUri(DatabaseSchema.User.TABLE_NAME),
                        null, selection, selectionArgs, null);
        try{
            if (cursor != null && cursor.moveToFirst()){
                user = new User();
                user.initByCursor(cursor);
            }
        }finally {
            if (cursor != null)
                cursor.close();
        }

        return user;
    }

    /*
    TASK
     */

    public void createTask(Task task){
        context.getContentResolver().insert(ContentProviderValues.getContentUri(DatabaseSchema.Task.TABLE_NAME),
                task.toContentValues());
    }

    public void getAllTask(DataLoadCallback<List<Task>> callback, LoaderManager manager){
        new TaskLoader(context, manager).loadData(callback, new Bundle());
    }

    public void updateTask(Task task){
        context.getContentResolver().update(ContentUris.withAppendedId(ContentProviderValues.getContentUri(DatabaseSchema.Task.TABLE_NAME), task.getId()),
                task.toContentValues(), null, null);
    }

    /*
    SUB_TASK
     */

    public void createSubTask(SubTask subTask){
        context.getContentResolver().insert(ContentProviderValues.getContentUri(DatabaseSchema.SubTask.TABLE_NAME),
                subTask.toContentValues());
    }

    public List<SubTask> getSubtaskByTaskId(int taskId){
        List<SubTask> subTasks = new ArrayList<>();
        String selection = DatabaseSchema.SubTask.TASK_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(taskId)};

        Cursor cursor = context.getContentResolver().query(ContentProviderValues.getContentUri(DatabaseSchema.SubTask.TABLE_NAME),
                null, selection, selectionArgs, null);

        try{
            if (cursor != null && cursor.moveToFirst()){
                do {
                    SubTask subTask = new SubTask();
                    subTask.initByCursor(cursor);
                    subTasks.add(subTask);
                }while(cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return subTasks;
    }

    /*
    CATEGORY
     */

    public void getCategory(DataLoadCallback<List<Category>> callback, LoaderManager manager){
        new CategoryLoader(context, manager).loadData(callback, new Bundle());
    }

    public void createCategory(Category category){
        context.getContentResolver().insert(ContentProviderValues.getContentUri(DatabaseSchema.Category.TABLE_NAME),
                category.toContentValues());
    }

}
