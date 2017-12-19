package com.example.alina.todolist;

import android.content.ContentProvider;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.test.ProviderTestCase2;
import android.util.Log;

import com.example.alina.todolist.db.ContentProviderValues;
import com.example.alina.todolist.db.ToDoProvider;
import com.example.alina.todolist.db.loaders.TaskLoader;
import com.example.alina.todolist.entities.Task;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Student_3 on 07/12/2017.
 */


public class DataBaseTestWithContentProvider extends ProviderTestCase2<ToDoProvider> {

    public DataBaseTestWithContentProvider() {
        super(ToDoProvider.class, ToDoProvider.class.getName());
    }

    protected void setUp() throws Exception {
        super.setUp();
    }


    @Test
    public void testQuery(){
        ContentProvider provider = getProvider();
        Task task = new Task();
        task.setName("foo");
        task.setDescription("bar");
        task.setExpireDate(new Date());
        provider.insert(ContentProviderValues.TASKS_CONTENT_URI, task.toContentValues());
        Cursor cursor = provider.query(ContentProviderValues.TASKS_CONTENT_URI, null, null, null, null);

        assertNotNull(cursor);

        cursor = null;
        try {
            cursor = provider.query(Uri.parse("definitelywrong"), null, null, null, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}
