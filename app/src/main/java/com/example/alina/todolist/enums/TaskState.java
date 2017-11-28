package com.example.alina.todolist.enums;

import com.example.alina.todolist.R;

/**
 * Created by Alina on 20.11.2017.
 */

public enum TaskState {
    ALL(R.string.tab_all),
    EXPIRED(R.string.tab_expired),
    DONE(R.string.tab_done);

    public int pageTitle;

    TaskState(int pageTitle) {
        this.pageTitle = pageTitle;
    }
}
