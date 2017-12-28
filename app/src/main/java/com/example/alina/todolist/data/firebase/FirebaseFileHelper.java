package com.example.alina.todolist.data.firebase;

import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseFileHelper {
    private StorageReference reference;
    private int taskId;

    public FirebaseFileHelper(int taskId){
        this.taskId = taskId;
        reference = FirebaseStorage.getInstance().getReference();
    }

    public void uploadFile(String filePath, OnSuccessListener<UploadTask.TaskSnapshot> successListener, OnFailureListener onFailureListener){
        Uri file = Uri.parse("file://"+filePath);
        StorageReference taskImageRef = reference.child("images/" + taskId + "/" + file.getLastPathSegment());
        taskImageRef.putFile(file)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(onFailureListener);
    }

    public void downloadFile(String localFilePath, OnSuccessListener<FileDownloadTask.TaskSnapshot> successListener, OnFailureListener onFailureListener){
        reference.getFile(Uri.parse("file://"+localFilePath))
                .addOnSuccessListener(successListener)
                .addOnFailureListener(onFailureListener);
    }

    public static DatabaseReference getTaskQuery(int taskId){
        return FirebaseDatabase.getInstance().getReference().child("tasks").child(String.valueOf(taskId));
    }

    public static void updateTaskName(int taskId ,String name){
        getTaskQuery(taskId).child("name").setValue(name);
    }

    public static void updateTaskDescription(int taskId, String desc){
        getTaskQuery(taskId).child("description").setValue(desc);
    }
}
