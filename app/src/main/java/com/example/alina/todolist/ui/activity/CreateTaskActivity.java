package com.example.alina.todolist.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
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

import com.example.alina.todolist.BuildConfig;
import com.example.alina.todolist.R;
import com.example.alina.todolist.adapters.ItemTouchHelperCallback;
import com.example.alina.todolist.adapters.SubTaskAdapter;
import com.example.alina.todolist.data.firebase.FirebaseFileHelper;
import com.example.alina.todolist.entities.SubTask;
import com.example.alina.todolist.entities.Task;
import com.example.alina.todolist.entities.TaskObject;
import com.example.alina.todolist.enums.ActivityRequest;
import com.example.alina.todolist.enums.BundleKey;
import com.example.alina.todolist.enums.TaskImageStatus;
import com.example.alina.todolist.enums.TaskState;
import com.example.alina.todolist.ui.fragment.AddSubTaskDialogFragment;
import com.example.alina.todolist.ui.fragment.DatePickerFragment;
import com.example.alina.todolist.validators.Constants;
import com.example.alina.todolist.validators.Validator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class CreateTaskActivity extends BaseActivity implements
        DatePickerFragment.OnDateSelectedListener,
        AddSubTaskDialogFragment.CreateSubTaskDialogListener,
        SubTaskAdapter.ItemSwipeCallback{

    private static final String FILE_PREFIX = "JPEG_";
    private static final String FILE_EXT = ".jpg";

    private Task task;
    private String currentFilePath;
    private TextInputLayout nameWrapper;
    private TextInputLayout descriptionWrapper;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private TextView dateTextView;
    private RecyclerView subTaskRecycler;
    private SubTaskAdapter subTaskAdapter;
    private LinearLayout taskDateLayout;
    private Button openCameraButton;
    private ImageView taskImage;
    private Validator stringValidator = new Validator.StringValidatorBuilder()
            .setNotEmpty()
            .setMinLength(3)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null
                && bundle.containsKey(BundleKey.TASK.name())) {
            task = bundle.getParcelable(BundleKey.TASK.name());
            initUI();
            setData();
        } else {
            Toast.makeText(getApplicationContext(), "Task not found", Toast.LENGTH_LONG).show();
            finish();
        }
        initDatePickerClick();
        initSubTaskRecycler();
        initCreateTaskButton();
        initOpenCameraButton();
    }

    private void initUI() {
        nameWrapper = (TextInputLayout) findViewById(R.id.nameWrapper);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        descriptionWrapper = (TextInputLayout) findViewById(R.id.descriptionWrapper);
        descriptionEditText = (EditText) findViewById(R.id.descriptionText);
        subTaskRecycler = (RecyclerView) findViewById(R.id.subTaskRecycler);
        taskDateLayout = (LinearLayout) findViewById(R.id.taskDateLayout);
        latitudeEditText = findViewById(R.id.latitudeEditText);
        longitudeEditText = findViewById(R.id.longitudeEditText);
        openCameraButton = findViewById(R.id.openCameraButton);
        taskImage = findViewById(R.id.taskImage);
    }

    private void setData() {
        nameEditText.setText(task.getName());
        descriptionEditText.setText(task.getDescription());
        dateTextView.setText(task.getExpireDateString());
        if (task.getLocation() != null){
            latitudeEditText.setText(String.valueOf(task.getLocation().getLatitude()));
            longitudeEditText.setText(String.valueOf(task.getLocation().getLongitude()));
        }
        showTaskImage();
    }

    private void showTaskImage() {
        if (!TextUtils.isEmpty(task.getImageUrl())){
            Picasso.with(this)
                    .load("file://" + task.getImageUrl())
                    .fit()
                    .centerInside()
                    .into(taskImage);
        }
    }

    private void fillData() {
        task.setName(nameEditText.getText().toString());
        task.setDescription(descriptionEditText.getText().toString());
    }

    private void showEditDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddSubTaskDialogFragment subTaskDialogFragment = AddSubTaskDialogFragment.newInstance("SubTask dialog");
        subTaskDialogFragment.show(fragmentManager, fragmentManager.getClass().getSimpleName());
    }

    private void initDatePickerClick(){
        taskDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void initOpenCameraButton(){
        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startCameraIntent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void startCameraIntent() throws IOException {
        File photoFile = createImageFile();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, ActivityRequest.REQUEST_CAMERA.ordinal());
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = FILE_PREFIX + Constants.IMAGE_FORMAT.format(new Date()) + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, FILE_EXT, storageDir);
        currentFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ActivityRequest.REQUEST_CAMERA.ordinal()){
            if (resultCode == Activity.RESULT_OK){
                setTaskImage(currentFilePath);
                task.setImageUrl(currentFilePath);
                task.setImageDownloadState(TaskImageStatus.IN_PROGRES);
                loadFileIntoFirebase();
            }
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    private void setTaskImage(String imageUri){
        Picasso.with(this)
                .load("file://" + imageUri)
                .fit().centerInside()
                .into(taskImage);
    }


    private void loadFileIntoFirebase(){
        FirebaseFileHelper helper = new FirebaseFileHelper(0);
        helper.uploadFile(currentFilePath, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // TODO: save file url
                // and change status
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();

            }
        });
    }

    @Override
    public void onFinishSubTask(SubTask subTask) {
        subTaskAdapter.addNewSubTask(subTask);
    }

    private void initSubTaskRecycler(){
        subTaskAdapter = new SubTaskAdapter(this);
        subTaskRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        subTaskRecycler.setAdapter(subTaskAdapter);
        if (task.getSubTasks().size() != 0){
            subTaskAdapter.addAllSubTask(task.getSubTasks());
        }

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(subTaskAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(subTaskRecycler);
    }

    private void initCreateTaskButton() {
        FloatingActionButton createSubTaskButton = (FloatingActionButton)
                findViewById(R.id.createSubTaskButton);
        createSubTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
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
        }
        else if (subTaskAdapter.isAllSubTaskDone() || subTaskAdapter.getSubTaskList().size() == 0) {
            task.setStatus(TaskObject.TaskStatus.DONE);
            saveTask();
        } else {
            Toast.makeText(this, "All SubTasks must be done!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTask() {
        if (validate(nameWrapper) && validate(descriptionWrapper)) {
            fillData();
            fillLocation();
            task.setSubTasks(subTaskAdapter.getSubTaskList());
            Intent result = new Intent();
            result.putExtra(BundleKey.TASK.name(), task);
            result.putExtra(BundleKey.TASK_STATUS.name(), getRootTaskStatus());
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }

    private void fillLocation(){
        Location location = new Location("");
        location.setLatitude(Double.parseDouble(latitudeEditText.getText().toString()));
        location.setLongitude(Double.parseDouble(longitudeEditText.getText().toString()));
        task.setLocation(location);
    }

    private String getRootTaskStatus(){
        String status;
        if (task.isAllSubTasksDone() || task.isDone()){
            status = TaskState.DONE.name();
        }
        else if (task.isExpire())
            status = TaskState.EXPIRED.name();
        else status = TaskState.ALL.name();

        return status;
    }

    private boolean validate(TextInputLayout wrapper) {
        wrapper.setErrorEnabled(false);
        boolean result = stringValidator.validate(wrapper.getEditText().getText().toString(),
                wrapper.getHint().toString());
        if (!result) {
            wrapper.setErrorEnabled(true);
            wrapper.setError(stringValidator.getLastMessage());
        }
        return result;
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSelected(Date date) {
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        task.setExpireDate(date);
        dateTextView.setText(task.getExpireDateString());
    }

    @Override
    public void onItemRemoved() {
        showUndoSnackBar();
    }

    private void showUndoSnackBar(){
        Snackbar.make(findViewById(R.id.activity_create_task), R.string.sub_task_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subTaskAdapter.restoreRemovedItem();
                    }
                }).show();
    }
}