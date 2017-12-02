package com.example.alina.todolist.enums;

import com.example.alina.todolist.R;

/**
 * Created by Alina on 20.11.2017.
 */

public enum TaskState {
    DONE(R.string.done_task),
    EXPIRED(R.string.expired_task),
    ALL(R.string.all_tasks);

    public int pageTitle;

    TaskState(int pageTitle) {
        this.pageTitle = pageTitle;
    }
}
