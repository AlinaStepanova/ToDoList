package com.example.alina.todolist.data.database;

import java.util.List;

public interface OnEntitiesLoad<T extends DataBaseContract> {
    void onSuccess(List<T> entities);
}