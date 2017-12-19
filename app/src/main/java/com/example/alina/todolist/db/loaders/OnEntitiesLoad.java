package com.example.alina.todolist.db.loaders;

import com.example.alina.todolist.db.DataBaseContract;

import java.util.List;

public interface OnEntitiesLoad<T extends DataBaseContract> {
    void onSuccess(List<T> entities);
}
