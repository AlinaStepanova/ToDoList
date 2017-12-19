package com.example.alina.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.alina.todolist.data.DataBaseDataSource;
import com.example.alina.todolist.data.DataSourceFactory;
import com.example.alina.todolist.data.FileDataSource;
import com.example.alina.todolist.data.IDataSource;
import com.example.alina.todolist.db.ContentProviderValues;
import com.example.alina.todolist.db.DataBaseManager;
import com.example.alina.todolist.entities.Category;
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
import java.util.Random;

import static com.example.alina.todolist.enums.BundleKey.NEED_CHECK_PASSWORD;

public class LoginActivity extends BaseActivity implements LoginFragment.NeedRegistrationListener,
        UserNameFragment.GetNameFromFragment, UserEmailFragment.GetEmailFromFragment, UserPinFragment.GetPinFromFragment,
        UserWelcomeFragment.RunMainActivity {

    private static final String CURRENT = "current";
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 4546;
    public static boolean NEED_REFRESH_PIN = true;

    private User currentUser;
    private IDataSource dataSource;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intent = getIntent();

        askForPermissions();

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            startActivity(new Intent(LoginActivity.this, LoginActivityFirebase.class));
            finish();
        }

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

        DataSourceFactory factory = new DataSourceFactory(this, null);
        dataSource = factory.createDataSource();
    }

    private void askForPermissions(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_FINE_LOCATION);
            }
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
            }
        }
    }
}
