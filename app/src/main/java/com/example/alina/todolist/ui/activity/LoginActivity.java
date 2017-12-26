package com.example.alina.todolist.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.alina.todolist.R;
import com.example.alina.todolist.data.repository.DatabaseSource;
import com.example.alina.todolist.entities.User;
import com.example.alina.todolist.listeners.OnDataChangedListener;
import com.example.alina.todolist.ui.fragment.LoginFragment;
import com.example.alina.todolist.ui.fragment.UserEmailFragment;
import com.example.alina.todolist.ui.fragment.UserNameFragment;
import com.example.alina.todolist.ui.fragment.UserPinFragment;
import com.example.alina.todolist.ui.fragment.UserWelcomeFragment;

import static com.example.alina.todolist.enums.BundleKey.NEED_CHECK_PASSWORD;

public class LoginActivity extends BaseActivity implements LoginFragment.NeedRegistrationListener,
        UserNameFragment.GetNameFromFragment, UserEmailFragment.GetEmailFromFragment, UserPinFragment.GetPinFromFragment,
        UserWelcomeFragment.RunMainActivity, OnDataChangedListener {

    private static final String CURRENT = "current";
    public static boolean NEED_REFRESH_PIN = true;

    private User currentUser;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
//            dataSource.setCurrentUser(getUsersFromData().get(userCount));
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
            setResult(Activity.RESULT_OK);
            NEED_REFRESH_PIN = true;
            finish();
            /*if (pin.equals(dataSource.getCurrentUser().getPin())) {
                setResult(Activity.RESULT_OK);
                NEED_REFRESH_PIN = true;
                finish();
            } else {
                Toast.makeText(this, "Wrong pin", Toast.LENGTH_SHORT).show();
            }*/
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
        DatabaseSource helper = new DatabaseSource(this);
        helper.createNewUser(currentUser);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
