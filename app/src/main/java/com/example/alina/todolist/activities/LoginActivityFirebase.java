package com.example.alina.todolist.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alina.todolist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivityFirebase extends BaseActivity {

    private FirebaseAuth auth;
    private EditText emailField;
    private EditText passwordField;
    private Button signInBtn;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_firebase);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(LoginActivityFirebase.this,
                    MainActivity.class));
            finish();
        }
        emailField = (EditText) findViewById(R.id.editText);
        passwordField = (EditText) findViewById(R.id.editText2);
        signInBtn = (Button) findViewById(R.id.button2);
        signUpBtn = (Button) findViewById(R.id.button3);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    signInExistingUser(email, password);
                } else Toast.makeText(LoginActivityFirebase.this, "nooooo",
                        Toast.LENGTH_SHORT).show();
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    createNewUser(email, password);
                } else Toast.makeText(LoginActivityFirebase.this, "nooooo",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNewUser(String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivityFirebase.this,
                                    MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivityFirebase.this,
                                    "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signInExistingUser(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivityFirebase.this,
                                    MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivityFirebase.this,
                                    "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
