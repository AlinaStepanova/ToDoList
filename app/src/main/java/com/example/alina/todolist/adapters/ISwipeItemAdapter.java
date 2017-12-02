package com.example.alina.todolist.adapters;

/**
 * Created by Leonid on 27.11.2017.
 */

public interface ISwipeItemAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
