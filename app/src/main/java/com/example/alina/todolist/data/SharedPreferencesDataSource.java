package com.example.alina.todolist.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import com.example.alina.todolist.entities.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Alina on 14.11.2017.
 */

public class SharedPreferencesDataSource implements IDataSource {

    private static final String NEW_TASK = "NewTask";

    private static SharedPreferences sharedPreferences;

    private static SharedPreferences.Editor editor;

    private static ArrayList<Task> tasks = new ArrayList<>();

    private Gson gson;

    private String jsonPreferences;

    public SharedPreferencesDataSource(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        gson = new Gson();
        jsonPreferences = sharedPreferences.getString(NEW_TASK, null);
    }

    @Override
    public ArrayList<Task> getTaskList() {
        Type type = new TypeToken<ArrayList<Task>>(){}.getType();
        return gson.fromJson(jsonPreferences, type);
    }

    @Override
    public boolean createTask(@NonNull Task task) {
        tasks = getTaskList();
        tasks.add(task);
        return setList(NEW_TASK, tasks);
    }

    @Override
    public boolean updateTask(@NonNull Task task, @IntRange(from = 0, to = Integer.MAX_VALUE)
            int index) {
        boolean result = false;
        if (index >= 0 && index < tasks.size()) {
            tasks.set(index, task);
            setList(NEW_TASK, tasks);
            result = true;
        }
        return result;
    }

    private boolean setList(String key, ArrayList<Task> list) {
        String json = gson.toJson(list);
        return set(key, json);
    }

    private boolean set(String key, String value) {
        editor.putString(key, value);
        return editor.commit();
    }
}
