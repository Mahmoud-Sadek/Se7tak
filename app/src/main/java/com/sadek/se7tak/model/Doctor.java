package com.sadek.se7tak.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Doctor implements Serializable {
    String id, profileImage, firstName, lastName, phoneNumber, email, password, certificateImage,rateTotal,rateCount;
    SpecialtyModel specialty;
    AboutDoctor aboutDoctor;
    DegreeDoctor degreeDoctor;
    ClinicDoctor clinicDoctor;
    boolean published;
    ArrayList<WorkDaysDoctor> workDaysDoctor  = new ArrayList<>();
    public Doctor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCertificateImage() {
        return certificateImage;
    }

    public void setCertificateImage(String certificateImage) {
        this.certificateImage = certificateImage;
    }

    public SpecialtyModel getSpecialty() {
        return specialty;
    }

    public void setSpecialty(SpecialtyModel specialty) {
        this.specialty = specialty;
    }

    public AboutDoctor getAboutDoctor() {
        return aboutDoctor;
    }

    public void setAboutDoctor(AboutDoctor aboutDoctor) {
        this.aboutDoctor = aboutDoctor;
    }

    public DegreeDoctor getDegreeDoctor() {
        return degreeDoctor;
    }

    public void setDegreeDoctor(DegreeDoctor degreeDoctor) {
        this.degreeDoctor = degreeDoctor;
    }

    public ClinicDoctor getClinicDoctor() {
        return clinicDoctor;
    }

    public void setClinicDoctor(ClinicDoctor clinicDoctor) {
        this.clinicDoctor = clinicDoctor;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public ArrayList<WorkDaysDoctor> getWorkDaysDoctor() {
        return workDaysDoctor;
    }

    public void setWorkDaysDoctor(ArrayList<WorkDaysDoctor> workDaysDoctor) {
        this.workDaysDoctor = workDaysDoctor;
    }

    public String getRateTotal() {
        return rateTotal;
    }

    public void setRateTotal(String rateTotal) {
        this.rateTotal = rateTotal;
    }

    public String getRateCount() {
        return rateCount;
    }

    public void setRateCount(String rateCount) {
        this.rateCount = rateCount;
    }
}
