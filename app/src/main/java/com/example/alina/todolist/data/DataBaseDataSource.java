package com.example.alina.todolist.data;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.example.alina.todolist.db.ContentProviderValues;
import com.example.alina.todolist.db.loaders.CategoryLoader;
import com.example.alina.todolist.db.loaders.CudLoader;
import com.example.alina.todolist.db.loaders.OnEntitiesLoad;
import com.example.alina.todolist.db.loaders.SubTaskLoader;
import com.example.alina.todolist.db.loaders.TaskLoader;
import com.example.alina.todolist.db.loaders.UserLoader;
import com.example.alina.todolist.entities.Category;
import com.example.alina.todolist.entities.SubTask;
import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.entities.User;
import com.example.alina.todolist.enums.BundleKey;
import com.example.alina.todolist.listeners.OnDataChangedListener;

import java.util.ArrayList;
import java.util.List;

public class DataBaseDataSource implements IDataSource, LoaderManager.LoaderCallbacks{

    private ArrayList<Task> tasks;
    private ArrayList<Category> categories;
    private AppCompatActivity context;
    private OnDataChangedListener onDataChangedListener;
    private User currentUser;
    private ArrayList<User> users;
    private LoaderManager loaderManager;

    private OnEntitiesLoad<Task> onTaskLoaded;
    private OnEntitiesLoad<Category> onCategoryLoaded;
    private OnEntitiesLoad<User> onUserLoaded;
    private OnEntitiesLoad<SubTask> onSubTaskLoaded;

    public DataBaseDataSource(AppCompatActivity context, OnDataChangedListener dataListener){
        tasks = new ArrayList<>();
        categories = new ArrayList<>();
        users = new ArrayList<>();
        currentUser = null;
        this.context = context;
        this.onDataChangedListener = dataListener;
        this.loaderManager = this.context.getSupportLoaderManager();
        setUpCallbacks();
        new CategoryLoader(context, loaderManager).loadCategories(onCategoryLoaded, new Bundle());
        new TaskLoader(context, loaderManager).loadTasks(onTaskLoaded, new Bundle());
        new UserLoader(context, loaderManager).loadUsers(onUserLoaded, new Bundle());
    }

    void loadTasks(){
        new TaskLoader(context, loaderManager).loadTasks(onTaskLoaded, new Bundle());
        //new SubTaskLoader(context, loaderManager).loadSubtasks(onSubTaskLoaded, new Bundle());
    }

    void loadCategories(){
        new CategoryLoader(context, loaderManager).loadCategories(onCategoryLoaded, new Bundle());
    }

    void loadUsers(){
        new UserLoader(context, loaderManager).loadUsers(onUserLoaded, new Bundle());
    }

    private void setUpCallbacks(){
        onTaskLoaded = new OnEntitiesLoad<Task>() {
            @Override
            public void onSuccess(List<Task> entities) {
                tasks.clear();
                tasks.addAll(entities);
                if(onDataChangedListener != null) {
                    onDataChangedListener.notifyDataChanged();
                }
            }
        };
        onCategoryLoaded = new OnEntitiesLoad<Category>() {
            @Override
            public void onSuccess(List<Category> entities) {
                categories.clear();
                categories.addAll(entities);
                if(onDataChangedListener != null) {
                    onDataChangedListener.notifyDataChanged();
                }
            }
        };
        onUserLoaded = new OnEntitiesLoad<User>() {
            @Override
            public void onSuccess(List<User> entities) {
                users.clear();
                users.addAll(entities);
                if(onDataChangedListener != null){
                    onDataChangedListener.notifyDataChanged();
                }
            }
        };
        onSubTaskLoaded = new OnEntitiesLoad<SubTask>() {
            @Override
            public void onSuccess(List<SubTask> entities) {
//                for (SubTask subTask : entities){
//                    for (Task task : tasks){
//                        if (subTask.get)
//                    }
//                }
            }
        };
    }

    private void changeDbInBackground(Bundle args){
        if(this.context.getSupportLoaderManager().getLoader(CudLoader.LOADER_ID) != null){
            this.context.getSupportLoaderManager().restartLoader(CudLoader.LOADER_ID, args, this);
        } else {
            this.context.getSupportLoaderManager().initLoader(CudLoader.LOADER_ID, args, this);
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CudLoader(context, args);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public ArrayList<User> getUserList() {

        return users;
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
        return true;
    }

    @Override
    public boolean createTask(@NonNull Task task) {
        Bundle args = new Bundle();
        args.putInt(BundleKey.DB_INTERACTION_TYPE.name(), CudLoader.CudType.INSERT.ordinal());
        args.putParcelable(BundleKey.CONTENT_VALUES.name(), task.toContentValues());
        args.putParcelable(BundleKey.URI.name(), ContentProviderValues.TASKS_CONTENT_URI);
        changeDbInBackground(args);
        return tasks.add(task);
    }

    @Override
    public boolean createCategory(@NonNull Category category) {
        Bundle args = new Bundle();
        args.putInt(BundleKey.DB_INTERACTION_TYPE.name(), CudLoader.CudType.INSERT.ordinal());
        args.putParcelable(BundleKey.CONTENT_VALUES.name(), category.toContentValues());
        args.putParcelable(BundleKey.URI.name(), ContentProviderValues.CATEGORY_CONTENT_URI);
        changeDbInBackground(args);
        return categories.add(category);
    }

    @Override
    public boolean addUser(@NonNull User user) {
        Bundle args = new Bundle();
        args.putInt(BundleKey.DB_INTERACTION_TYPE.name(), CudLoader.CudType.INSERT.ordinal());
        args.putParcelable(BundleKey.CONTENT_VALUES.name(), user.toContentValues());
        args.putParcelable(BundleKey.URI.name(), ContentProviderValues.USER_CONTENT_URI);
        changeDbInBackground(args);
        return users.add(user);
    }

    @Override
    public boolean updateTask(@NonNull Task task, int index) {
        Bundle args = new Bundle();
        args.putInt(BundleKey.DB_INTERACTION_TYPE.name(), CudLoader.CudType.UPDATE.ordinal());
        args.putParcelable(BundleKey.CONTENT_VALUES.name(), task.toContentValues());
        args.putParcelable(BundleKey.URI.name(), ContentProviderValues.TASK_CATEGORY_CONTENT_URI);
        changeDbInBackground(args);
        tasks.set(index, task);
        return true;
    }

    @Override
    public boolean updateTask(@NonNull Task task) {
        return updateTask(task, tasks.indexOf(task));
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
