package com.example.alina.todolist.data;


import com.example.alina.todolist.entities.Category;
import com.example.alina.todolist.entities.Task;

import java.util.List;

public interface IDataSourceAsynch {
    void getTaskList(DataCallback<List<Task>> callback);
    void addTask(Task task);
    void updateTask(int taskId, Task task);
    void deleteTask(int taskId);

    void getCategoryList(DataCallback<List<Category>> callback);
    void addCategory(Category category);
    void updateCategory(int catId, Category category);
    void deleteCategory(int categoryId);
}
