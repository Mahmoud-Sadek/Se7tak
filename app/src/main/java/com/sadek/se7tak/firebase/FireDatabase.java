package com.sadek.se7tak.firebase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sadek.se7tak.R;
import com.sadek.se7tak.activity.MainActivity;
import com.sadek.se7tak.model.DegreeDoctor;
import com.sadek.se7tak.model.Doctor;
import com.sadek.se7tak.model.Favorite;
import com.sadek.se7tak.model.Order;
import com.sadek.se7tak.model.PatientUser;
import com.sadek.se7tak.model.Rate;
import com.sadek.se7tak.model.SpecialtyModel;
import com.sadek.se7tak.utils.Common;

import java.util.ArrayList;

public class FireDatabase {

    Context context;
    private static final String TAG = FireDatabase.class.getSimpleName();
    public DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    public FireAuth fireAuth;
    ProgressDialog progressDialog;


    public FireDatabase(Context context) {
        this.context = context;
        fireAuth = new FireAuth(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);
    }

    public FireDatabase(Context context, FireAuth fireAuth) {
        this.context = context;
        this.fireAuth = fireAuth;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading...");
    }

    public void addUser(final PatientUser user) {
        progressDialog.show();
        reference.child(Common.FIREBASE_PATIENT).child(user.getId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    progressDialog.dismiss();
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    Toast.makeText(context, "success",
                            Toast.LENGTH_SHORT).show();

                } else {
                    // If sign in fails, display a message to the user.
                    progressDialog.dismiss();
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(context, task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public void editUser(final PatientUser user) {
        progressDialog.show();
        reference.child(Common.FIREBASE_PATIENT).child(user.getId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    progressDialog.dismiss();
                    ((Activity) context).onBackPressed();
                    Toast.makeText(context, "success",
                            Toast.LENGTH_SHORT).show();

                } else {
                    // If sign in fails, display a message to the user.
                    progressDialog.dismiss();
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(context, task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public void getUser(UserCallback callback) {
        progressDialog.show();
        reference.child(Common.FIREBASE_PATIENT).child(fireAuth.mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PatientUser patientUser = new PatientUser();
                if (dataSnapshot.exists()) {
                    patientUser = dataSnapshot.getValue(PatientUser.class);
                    callback.onCallback(patientUser);
                } else
                    callback.onCallback(patientUser);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });

    }


    public void addOrder(final Order model, ResultCallback callback) {
        progressDialog.show();
        String id = reference.push().getKey();
        model.setId(id);
        reference.child(Common.FIREBASE_DOCTOR_ORDER).child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    progressDialog.dismiss();
                    Toast.makeText(context, "success",
                            Toast.LENGTH_SHORT).show();
                    callback.onCallback(true);

                } else {
                    // If sign in fails, display a message to the user.
                    progressDialog.dismiss();
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(context, task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                    callback.onCallback(false);

                }

            }
        });
    }

    Double totalRate = 0.0, countRates = 1.0;

    public void addRate(final Rate model, ResultCallback callback) {
        progressDialog.show();
        String id = model.getUserId() + model.getDoctorId();
        model.setId(id);
        reference.child(Common.FIREBASE_DOCTOR_RATES).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalRate = Double.parseDouble(model.getRate());
                if (dataSnapshot.hasChildren()) {
                    Rate rate = dataSnapshot.getValue(Rate.class);
                    totalRate = Double.parseDouble(rate.getRate()) - Double.parseDouble(model.getRate());
                    countRates = 0.0;
                }
                completeRate(totalRate, countRates);

            }

            private void completeRate(Double totalRate, Double countRates) {
                reference.child(Common.FIREBASE_DOCTOR_RATES).child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            getDoctor(model.getDoctorId(), new DoctorCallback() {
                                @Override
                                public void onCallback(Doctor modelDoctor) {
                                    if (!model.getId().equals("0")) {
                                        model.setId("0");
                                        if (modelDoctor.getRateCount() == null) {
                                            modelDoctor.setRateCount("0");
                                            modelDoctor.setRateTotal("0");
                                        }
                                        modelDoctor.setRateCount(Double.parseDouble(modelDoctor.getRateCount()) + countRates + "");
                                        modelDoctor.setRateTotal(Double.parseDouble(modelDoctor.getRateTotal()) + totalRate + "");
                                        reference.child(Common.FIREBASE_DOCTOR).child(model.getDoctorId()).setValue(modelDoctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Log.d(TAG, "createUserWithEmail:success");
                                                    progressDialog.dismiss();
                                                    Toast.makeText(context, "success",
                                                            Toast.LENGTH_SHORT).show();
                                                    callback.onCallback(true);

                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    progressDialog.dismiss();
                                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                    Toast.makeText(context, task.getException().getMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                                    callback.onCallback(false);

                                                }

                                            }
                                        });
                                    }
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            callback.onCallback(false);

                        }

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void addLike(final Favorite model, boolean remove, ResultCallback callback) {
        progressDialog.show();
        String id = model.getUserId();
        model.setId(id);
        if (remove)
            reference.child(Common.FIREBASE_FAVORITES).child(model.getDoctorId()).child(id).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "success",
                                Toast.LENGTH_SHORT).show();
                        callback.onCallback(false);

                    } else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(context, task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        callback.onCallback(true);

                    }

                }
            });
        else
            reference.child(Common.FIREBASE_FAVORITES).child(model.getDoctorId()).child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "success",
                                Toast.LENGTH_SHORT).show();
                        callback.onCallback(true);

                    } else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(context, task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        callback.onCallback(false);

                    }

                }
            });
    }

    public void isFavorite(String doctorId, ResultCallback callback) {
        progressDialog.show();
        reference.child(Common.FIREBASE_FAVORITES).child(doctorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            Favorite model = snapshot.getValue(Favorite.class);
                            if (model.getUserId().equals(fireAuth.mAuth.getUid())) {
                                callback.onCallback(true);
                                progressDialog.dismiss();
                                return;
                            }
                        }
                    }
                    callback.onCallback(false);

                } else
                    callback.onCallback(false);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }

    public void getUserFavorites(FavoriteListCallback callback) {
        final ArrayList<Favorite> list = new ArrayList<>();
        progressDialog.show();
        reference.child(Common.FIREBASE_FAVORITES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                                Favorite model = snapshot2.getValue(Favorite.class);
                                if (model.getUserId().equals(fireAuth.mAuth.getUid())) {
                                    list.add(model);
                                }
                            }
                        }
                    }
                    callback.onCallback(list);

                } else
                    callback.onCallback(list);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }

    public void getFavorites(String doctorId, ResultStringCallback callback) {
        progressDialog.show();
        reference.child(Common.FIREBASE_FAVORITES).child(doctorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    callback.onCallback(dataSnapshot.getChildrenCount() + " " + context.getString(R.string.likes));
                    progressDialog.dismiss();
                    return;

                } else
                    callback.onCallback("0 " + context.getString(R.string.likes));
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }


    // this method for get all drivers from database
    public void getSpecialty(final SpecialtyCallback callback) {
        final ArrayList<SpecialtyModel> list = new ArrayList<>();
        reference.child(Common.FIREBASE_SPECIALTY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            SpecialtyModel model = snapshot.getValue(SpecialtyModel.class);
                            list.add(model);
                        }
                    }
                    callback.onCallback(list);
                } else
                    callback.onCallback(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }

    public void getEducationDegree(final DcotorDegreeCallback callback) {
        final ArrayList<DegreeDoctor> list = new ArrayList<>();
        reference.child(Common.FIREBASE_DOCTOR).child(fireAuth.mAuth.getUid()).child(Common.FIREBASE_DEGREE_DOCTOR).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            DegreeDoctor model = snapshot.getValue(DegreeDoctor.class);
                            list.add(model);
                        }
                    }
                    callback.onCallback(list);
                } else
                    callback.onCallback(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }

    // this method for get all drivers from database
    public void getDoctor(String doctorId, final DoctorCallback callback) {

        reference.child(Common.FIREBASE_DOCTOR).child(doctorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Doctor doctor = new Doctor();
                if (dataSnapshot.exists()) {
                    doctor = dataSnapshot.getValue(Doctor.class);
                    callback.onCallback(doctor);
                } else
                    callback.onCallback(doctor);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }

    // this method for get all drivers from database
    public void getDoctorList(final DoctorListCallback callback) {
        final ArrayList<Doctor> list = new ArrayList<>();
        reference.child(Common.FIREBASE_DOCTOR).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            Doctor model = snapshot.getValue(Doctor.class);
                            if (model.isPublished())
                                list.add(model);
                        }
                    }
                    callback.onCallback(list);
                } else
                    callback.onCallback(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }

    // this method for get all drivers from database
    public void getDoctorRatesList(String doctorId, final RateListCallback callback) {
        final ArrayList<Rate> list = new ArrayList<>();
        reference.child(Common.FIREBASE_DOCTOR_RATES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            Rate model = snapshot.getValue(Rate.class);
                            if (model.getDoctorId().equals(doctorId))
                                list.add(model);
                        }
                    }
                    callback.onCallback(list);
                } else
                    callback.onCallback(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }

    // this method for get all drivers from database
    public void getAppointments(String status, final OrderListCallback callback) {
        final ArrayList<Order> list = new ArrayList<>();
        reference.child(Common.FIREBASE_DOCTOR_ORDER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            Order model = snapshot.getValue(Order.class);
                            if (model.getUserId().equals(fireAuth.mAuth.getUid()) && model.getStuatus().equals(status))
                                list.add(model);
                        }
                    }
                    callback.onCallback(list);
                } else
                    callback.onCallback(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }

    // this method for get all drivers from database
    public void deleteAppointment(Order order, final ResultCallback callback) {
        progressDialog.show();
        order.setStuatus(Common.ORDER_STATUS_CANCELED);
        reference.child(Common.FIREBASE_DOCTOR_ORDER).child(order.getId()).setValue(order)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful());
                        progressDialog.dismiss();
                    }
                });
    }

    public void getDoctorListBySpecialty(SpecialtyModel specialtyModel, final DoctorListCallback callback) {
        final ArrayList<Doctor> list = new ArrayList<>();
        reference.child(Common.FIREBASE_DOCTOR).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            Doctor model = snapshot.getValue(Doctor.class);
                            if (model.getSpecialty().getId().equals(specialtyModel.getId()))
                                if (model.isPublished())
                                    list.add(model);
                        }
                    }
                    callback.onCallback(list);
                } else
                    callback.onCallback(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }
/*
    public void getAppointments(final AppointmentCallback callback) {
        final ArrayList<Appointment> list = new ArrayList<>();
        reference.child(Common.FIREBASE_APPOINTMENT).child(fireAuth.mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            Appointment model = snapshot.getValue(Appointment.class);
                            list.add(model);
                        }
                    }
                    callback.onCallback(list);
                } else
                    callback.onCallback(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }*/


    public void getClinicPhoto(final ClinicPhotoCallback callback) {
        final ArrayList<String> list = new ArrayList<>();
        reference.child(Common.FIREBASE_DOCTOR).child(fireAuth.mAuth.getUid()).child(Common.FIREBASE_CLINIC_DOCTOR).child(Common.FIREBASE_CLINIC_PHOTO).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            String model = snapshot.getValue(String.class);
                            list.add(model);
                        }
                    }
                    callback.onCallback(list);
                } else
                    callback.onCallback(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }


    public interface SpecialtyCallback {
        void onCallback(ArrayList<SpecialtyModel> model);
    }

    public interface DcotorDegreeCallback {
        void onCallback(ArrayList<DegreeDoctor> model);
    }

    public interface DoctorCallback {
        void onCallback(Doctor model);
    }

    public interface UserCallback {
        void onCallback(PatientUser model);
    }

    public interface ClinicPhotoCallback {
        void onCallback(ArrayList<String> model);
    }

    public interface DoctorListCallback {
        void onCallback(ArrayList<Doctor> model);
    }

    public interface FavoriteListCallback {
        void onCallback(ArrayList<Favorite> model);
    }

    public interface RateListCallback {
        void onCallback(ArrayList<Rate> model);
    }

    public interface OrderListCallback {
        void onCallback(ArrayList<Order> model);
    }

    public interface ResultCallback {
        void onCallback(boolean success);
    }

    public interface ResultStringCallback {
        void onCallback(String success);
    }

  /*  public interface PatientCallback {
        void onCallback(List<Patient> model);
    }

    public interface AppointmentCallback {
        void onCallback(List<Appointment> model);
    }*/
}
