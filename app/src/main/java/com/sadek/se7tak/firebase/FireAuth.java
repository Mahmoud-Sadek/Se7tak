package com.sadek.se7tak.firebase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.sadek.se7tak.R;
import com.sadek.se7tak.activity.MainActivity;
import com.sadek.se7tak.model.PatientUser;
import com.sadek.se7tak.utils.Common;

public class FireAuth {
    Context context;
    private static final String TAG = FireAuth.class.getSimpleName();
    public FireDatabase fireDatabase;
    public FirebaseAuth mAuth;
    public FirebaseUser firebaseUser;
    ProgressDialog progressDialog;


    public FireAuth(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);
        fireDatabase = new FireDatabase(context, this);
    }

    public void signIn(String email, String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (mAuth.getCurrentUser().getDisplayName() != null) {
                        if (mAuth.getCurrentUser().getDisplayName().equals(Common.FIREBASE_PATIENT)) {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }else {
                            mAuth.signOut();
                            Toast.makeText(context, context.getString(R.string.not_valid_user), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    } else {
                        mAuth.signOut();
                        Toast.makeText(context, context.getString(R.string.not_valid_user), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmailAndPassword:failure", task.getException());
                    Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }

            }
        });
    }

    public void forgetPassword(String email) {
        progressDialog.show();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, context.getString(R.string.sent_to_mail), Toast.LENGTH_SHORT).show();
                            ((Activity) context).finish();
                            Log.d(TAG, "Email sent.");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmailAndPassword:failure", task.getException());
                            Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    public void updatePassword(String newPassword, PatientUser patientUser) {
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(patientUser.getEmail(), patientUser.getPassword());

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            patientUser.setPassword(newPassword);
                                            fireDatabase.editUser(patientUser);
                                            progressDialog.dismiss();
                                        }
                                        progressDialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


    }

    public void signUp(final PatientUser user) {
        progressDialog.setMessage("loading...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = mAuth.getCurrentUser();
                            setUserType(firebaseUser, new ResultCallback() {
                                @Override
                                public void onCallback(boolean success) {
                                    user.setId(firebaseUser.getUid());
                                    fireDatabase.addUser(user);
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }


    private void setUserType(FirebaseUser firebaseUser, ResultCallback callback) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Common.FIREBASE_PATIENT)
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            callback.onCallback(task.isSuccessful());
                        }
                    }
                });
    }

    public interface ResultCallback {
        void onCallback(boolean success);
    }

}
