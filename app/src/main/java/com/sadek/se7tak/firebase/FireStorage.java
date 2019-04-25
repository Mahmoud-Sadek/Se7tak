package com.sadek.se7tak.firebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class FireStorage {

    Context context;
    private static final String TAG = FireStorage.class.getSimpleName();
    private StorageReference storage = FirebaseStorage.getInstance().getReference();
    ProgressDialog progressDialog;

    public FireStorage(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading...");
    }


    public void uploadImage(Bitmap bitmap, final urlCallback callback){
        progressDialog.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        final StorageReference storageRef = storage.child("images/"+System.currentTimeMillis()+".png" );;
        UploadTask uploadTask = storageRef.putBytes(data);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.e(TAG,"error---->"+task.getException().getMessage());
                    progressDialog.dismiss();
                    throw task.getException();

                }
                // Continue with the task to get the download URL
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    //image url
                    Uri downloadUri = task.getResult();
                    callback.onCallback(downloadUri.toString());
                } else {
                    callback.onCallback("");
                    Log.e(TAG,"error---->"+task.getException().getMessage());
                }
                progressDialog.dismiss();
            }
        });


    }


    public void uploadVideo(Uri video, final urlCallback callback){
        final StorageReference storageRef = storage.child("videos/"+System.currentTimeMillis()+".png" );;
        UploadTask uploadTask = storageRef.putFile(video);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.e(TAG,"error---->"+task.getException().getMessage());
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    //image url
                    Uri downloadUri = task.getResult();
                    callback.onCallback(downloadUri.toString());
                } else {
                    callback.onCallback("");
                    Log.e(TAG,"error---->"+task.getException().getMessage());
                }
            }
        });


    }


    //this callback for get only one driver
    public interface urlCallback {
        void onCallback(String url);
    }

}
