package com.example.alina.todolist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.alina.todolist.data.FileDataSource;
import com.example.alina.todolist.data.IDataSource;
import com.example.alina.todolist.db.ContentProviderValues;
import com.example.alina.todolist.db.DataBaseManager;
import com.example.alina.todolist.entities.SubTask;
import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.entities.TaskObject;
import com.example.alina.todolist.entities.User;
import com.example.alina.todolist.fragments.LoginFragment;
import com.example.alina.todolist.fragments.UserEmailFragment;
import com.example.alina.todolist.fragments.UserNameFragment;
import com.example.alina.todolist.fragments.UserPinFragment;
import com.example.alina.todolist.fragments.UserWelcomeFragment;
import com.example.alina.todolist.listeners.OnDataChangedListener;

import java.util.ArrayList;
import java.util.Date;

import static com.example.alina.todolist.enums.BundleKey.NEED_CHECK_PASSWORD;

public class LoginActivity extends BaseActivity implements LoginFragment.NeedRegistrationListener,
        UserNameFragment.GetNameFromFragment, UserEmailFragment.GetEmailFromFragment, UserPinFragment.GetPinFromFragment,
        UserWelcomeFragment.RunMainActivity, OnDataChangedListener {

    private static final String CURRENT = "current";
    public static boolean NEED_REFRESH_PIN = true;

    private User currentUser;
    private IDataSource dataSource;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (int i = 0; i < 40; i++){
            SubTask subTask = new SubTask();
            subTask.setDescription("asdasd" + i*300);
            subTask.setStatus(TaskObject.TaskStatus.NEW);
            subTask.setId(3);
        }
        Task task = new Task();
        task.setSubTasks(subTasks);
        getContentResolver().





        intent = getIntent();

        if (savedInstanceState == null && !intent.getBooleanExtra(NEED_CHECK_PASSWORD.name(), false)) {
            LoginFragment loginFragment = new LoginFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userFragmentContainer, loginFragment)
                    .commit();
            NEED_REFRESH_PIN = true;
        }

        if (intent.getBooleanExtra(NEED_CHECK_PASSWORD.name(), false)&& NEED_REFRESH_PIN) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_rigth,R.anim.slide_in_left, R.anim.slide_out_rigth)
                    .replace(R.id.userFragmentContainer, new UserPinFragment())
                    .commit();
            NEED_REFRESH_PIN = false;
        }

        dataSource = new FileDataSource(this, this);
    }

    @Override
    public void notifyDataChanged() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT, getCurrentUser());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentUser = savedInstanceState.getParcelable(CURRENT);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public ArrayList<User> getUsersFromData() {
        return dataSource.getUserList();
    }

    @Override
    public void needRegistration(boolean needRegistration, int userCount) {
        if (needRegistration) {
            currentUser = new User();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_rigth,
                            R.anim.slide_in_left, R.anim.slide_out_rigth)
                    .replace(R.id.userFragmentContainer, new UserNameFragment())
                    .addToBackStack("UserName")
                    .commit();
        } else {
            dataSource.setCurrentUser(getUsersFromData().get(userCount));

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void getName(String name) {
        currentUser.setName(name);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_rigth,
                        R.anim.slide_in_left, R.anim.slide_out_rigth)
                .replace(R.id.userFragmentContainer, new UserEmailFragment())
                .addToBackStack("UserMail")
                .commit();
    }

    @Override
    public void getEmail(String email) {
        currentUser.setEmail(email);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_rigth)
                .replace(R.id.userFragmentContainer, new UserPinFragment())
                .addToBackStack("UserPin")
                .commit();
    }

    @Override
    public void getPin(String pin) {
        if (intent.getBooleanExtra(NEED_CHECK_PASSWORD.name(), false)) {
            if (pin.equals(dataSource.getCurrentUser().getPin())) {
                setResult(Activity.RESULT_OK);
                NEED_REFRESH_PIN = true;
                finish();
            } else {
                Toast.makeText(this, "Wrong pin", Toast.LENGTH_SHORT).show();
            }
        } else {
            currentUser.setPin(pin);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_rigth)
                    .replace(R.id.userFragmentContainer, new UserWelcomeFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void runMainActivity() {
        dataSource.addUser(currentUser);
        dataSource.setCurrentUser(currentUser);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
