package com.example.alina.todolist.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alina.todolist.R;
import com.example.alina.todolist.adapters.ItemTouchHelperCallback;
import com.example.alina.todolist.adapters.SubTaskAdapter;
import com.example.alina.todolist.data.FirebaseDataSource;
import com.example.alina.todolist.data.DataSource;
import com.example.alina.todolist.entities.Category;
import com.example.alina.todolist.entities.SubTask;
import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.entities.TaskObject;
import com.example.alina.todolist.enums.ActivityRequest;
import com.example.alina.todolist.enums.BundleKey;
import com.example.alina.todolist.enums.TaskState;
import com.example.alina.todolist.fragments.AddSubTaskDialogFragment;
import com.example.alina.todolist.fragments.DatePickerFragment;
import com.example.alina.todolist.validators.Validator;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateTaskActivity extends BaseLocationActivity implements
        DatePickerFragment.OnDateSelectedListener,
        AddSubTaskDialogFragment.CreateSubTaskDialogListener,
        SubTaskAdapter.ItemSwipeCallback {

    private Task task;
    private TextInputLayout nameWrapper;
    private TextInputLayout descriptionWrapper;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private TextView dateTextView;
    private TextInputLayout categoryWrapper;
    private TextView categoryTextView;
    private Category currentCategory;

    private RecyclerView subTaskRecycler;
    private SubTaskAdapter subTaskAdapter;
    private LinearLayout taskDateLayout;
    private Validator stringValidator = new Validator.StringValidatorBuilder()
            .setNotEmpty()
            .setMinLength(3)
            .build();

    private TextInputLayout latitudeWrapper;
    private TextInputLayout longitudeWrapper;
    private EditText latitudeEditText;
    private EditText longitudeEditText;

    private Button makePhotoButton;
    private ImageView taskPhotoView;
    private String currentPhotoPath;
    private File currentFile;

    private DataSource firebaseDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null
                && bundle.containsKey(BundleKey.TASK.name())) {
            task = bundle.getParcelable(BundleKey.TASK.name());
            firebaseDataSource = new FirebaseDataSource();
            initUI();
            setData();
        } else {
            Toast.makeText(getApplicationContext(), "Task not found", Toast.LENGTH_LONG).show();
            finish();
        }
        initDatePickerClick();
        initSubTaskRecycler();
        initCreateTaskButton();
    }

    private void initUI() {
        nameWrapper = findViewById(R.id.nameWrapper);
        nameEditText = findViewById(R.id.nameEditText);
        dateTextView = findViewById(R.id.dateTextView);
        descriptionWrapper = findViewById(R.id.descriptionWrapper);
        descriptionEditText = findViewById(R.id.descriptionText);
        subTaskRecycler = findViewById(R.id.subTaskRecycler);
        taskDateLayout = findViewById(R.id.taskDateLayout);
        latitudeWrapper = findViewById(R.id.latitudeWrapper);
        latitudeEditText = findViewById(R.id.latitude);
        longitudeEditText = findViewById(R.id.longitude);
        longitudeWrapper = findViewById(R.id.longitudeWrapper);
        categoryWrapper = findViewById(R.id.categoryWrapper);
        categoryTextView = findViewById(R.id.categoryTextView);

        makePhotoButton = findViewById(R.id.makePhoto);
        taskPhotoView = findViewById(R.id.taskPhoto);

        makePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        categoryWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNeedCheckCurrentTime(false);
                startActivityForResult(CategoryActivity.launchInEditMode(getApplicationContext()),
                        ActivityRequest.GET_CATEGORY.ordinal());
            }
        });
    }

    private void openCamera() throws IOException {
        File photoFile = createImageFile();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI = FileProvider.getUriForFile(this,
                "com.example.alina.todolist.fileprovider", photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            setNeedCheckCurrentTime(false);
            startActivityForResult(takePictureIntent, ActivityRequest.REQUEST_CODE_CAMERA.ordinal());
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        currentFile = image;
        return image;
    }



    private void setData() {
        nameEditText.setText(task.getName());
        descriptionEditText.setText(task.getDescription());
        dateTextView.setText(task.getExpireDateString());
      //  categoryTextView.setText(task.getCategory().getName());
        latitudeEditText.setText(String.valueOf(task.getLatitude()));
        longitudeEditText.setText(String.valueOf(task.getLongitude()));
    }



    private void fillData() {
        task.setName(nameEditText.getText().toString());
        task.setDescription(descriptionEditText.getText().toString());
        task.setLocation(Double.parseDouble(latitudeEditText.getText().toString())
                , Double.parseDouble(longitudeEditText.getText().toString()));
        Log.d("in_bundle", "fill data create: " + task.getLatitude() + " " + task.getLongitude() + " ");
    }

    private void showEditDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddSubTaskDialogFragment subTaskDialogFragment = AddSubTaskDialogFragment.newInstance("SubTask dialog");
        subTaskDialogFragment.show(fragmentManager, fragmentManager.getClass().getSimpleName());
    }

    private void initDatePickerClick() {
        taskDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    @Override
    public void onFinishSubTask(SubTask subTask) {
        subTaskAdapter.addNewSubTask(subTask);
    }

    private void initSubTaskRecycler() {
        subTaskAdapter = new SubTaskAdapter(this);
        subTaskRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        subTaskRecycler.setAdapter(subTaskAdapter);

        if (task.getSubTasks().size() != 0) {
            subTaskAdapter.addAllSubTask(task.getSubTasks());
        }

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(subTaskAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(subTaskRecycler);
    }

    private void initCreateTaskButton() {
        FloatingActionButton createSubTaskButton = findViewById(R.id.createSubTaskButton);
        createSubTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_editor, menu);
        menu.findItem(R.id.item_done_task).setTitle(task.isDone() ?
                R.string.undone_task : R.string.done_task);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_save:
                saveTask();
                return true;
            case R.id.item_done_task:
                setTaskDone();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setTaskDone() {
        if (task.getStatus() == TaskObject.TaskStatus.DONE) {
            task.setStatus(TaskObject.TaskStatus.NEW);
            saveTask();
        } else if (subTaskAdapter.isAllSubTaskDone() || subTaskAdapter.getSubTaskList().size() == 0) {
            task.setStatus(TaskObject.TaskStatus.DONE);
            saveTask();
        } else {
            Toast.makeText(this, "All SubTasks must be done!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (ActivityRequest.values()[requestCode]) {
            case GET_CATEGORY:
                if (resultCode == Activity.RESULT_OK) {
                    currentCategory = data.getParcelableExtra(BundleKey.CATEGORY.name());
                    if (currentCategory != null) {
                        task.setCategory(currentCategory);
                        categoryTextView.setText(currentCategory.getName());
                        categoryTextView.setTextColor(currentCategory.getColor());
                    }
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    fillImageView();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void fillImageView() {
        Picasso.with(this)
                .load("file://" + currentPhotoPath)
                .resize(700, 700)
                .into(taskPhotoView);
    }


    private void uploadImage(){
        Uri photoURI = FileProvider.getUriForFile(this,
                "com.example.alina.todolist.fileprovider",
                currentFile);
        FirebaseStorage.getInstance().getReference().child("images").child(String.valueOf(task.getId())).putFile(photoURI);
    }

    private void saveTask() {
        if (validateText(nameWrapper) && validateText(descriptionWrapper) && validate()) {
            fillData();
            task.setSubTasks(subTaskAdapter.getSubTaskList());

            firebaseDataSource.createTask(task);

            if (taskPhotoView.getDrawable() != null) {
                uploadImage();
                task.setPhotoPath(currentPhotoPath);
            }
            Intent result = new Intent();
            result.putExtra(BundleKey.TASK.name(), task);
            Log.d("in_bundle", "in create : " + task.getLatitude() + " " + task.getLongitude());
            result.putExtra(BundleKey.TASK_STATUS.name(), getRootTaskStatus());
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }

    private String getRootTaskStatus() {
        String status;
        if (task.isAllSubTasksDone() || task.isDone()) {
            status = TaskState.DONE.name();
        } else if (task.isExpire())
            status = TaskState.EXPIRED.name();
        else status = TaskState.ALL.name();

        return status;
    }

    private boolean validateText(TextInputLayout wrapper) {
        wrapper.setErrorEnabled(false);
        boolean result = stringValidator.validate(wrapper.getEditText().getText().toString(),
                wrapper.getHint().toString());
        if (!result) {
            wrapper.setErrorEnabled(true);
            wrapper.setError(stringValidator.getLastMessage());
        }
        return result;
    }


    private boolean validate() {
        boolean result = validateText(nameWrapper) & validateText(descriptionWrapper);
        categoryWrapper.setErrorEnabled(false);
        if (currentCategory == null) {
            categoryWrapper.setErrorEnabled(true);
            categoryWrapper.setError(getString(R.string.no_category_chosen));
            result = false;
        }
        return result;
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    public void openCategoryActivity(View v) {
        setNeedCheckCurrentTime(false);
        startActivityForResult(CategoryActivity.launchInEditMode(this),
                ActivityRequest.GET_CATEGORY.ordinal());
    }


    @Override
    public void onDateSelected(Date date) {
        dateTextView = findViewById(R.id.dateTextView);
        task.setExpireDate(date);
        dateTextView.setText(task.getExpireDateString());
    }

    @Override
    public void onItemRemoved() {
        showUndoSnackBar();
    }

    private void showUndoSnackBar() {
        Snackbar.make(findViewById(R.id.activity_create_task), R.string.sub_task_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subTaskAdapter.restoreRemovedItem();
                    }
                }).show();
    }
}