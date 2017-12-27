package com.example.alina.todolist.enums;

import com.example.alina.todolist.R;


public enum NotificationAction {
    TASK_ACTIVITY(R.string.task_actitity),
    MAP_FRAGMENT(R.string.map_fragment);

    private int buttonTitle;

    NotificationAction(int buttonTitle) {
        this.buttonTitle = buttonTitle;
    }

    public int getButtonTitle(){
        return buttonTitle;
    }


}
