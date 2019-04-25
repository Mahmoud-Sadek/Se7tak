package com.sadek.se7tak.model;

import java.util.List;

public class ClinicDoctor {
    String id, nameAR, nameEN, phone, examinationFees, locationAR, locationEN;
    ClinicAssistantDoctor clinicAssistantDoctor;
    double lat, lang;
    List<String> imges;

    public ClinicDoctor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameAR() {
        return nameAR;
    }

    public void setNameAR(String nameAR) {
        this.nameAR = nameAR;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExaminationFees() {
        return examinationFees;
    }

    public void setExaminationFees(String examinationFees) {
        this.examinationFees = examinationFees;
    }

    public ClinicAssistantDoctor getClinicAssistantDoctor() {
        return clinicAssistantDoctor;
    }

    public void setClinicAssistantDoctor(ClinicAssistantDoctor clinicAssistantDoctor) {
        this.clinicAssistantDoctor = clinicAssistantDoctor;
    }

    public List<String> getImges() {
        return imges;
    }

    public void setImges(List<String> imges) {
        this.imges = imges;
    }

    public String getLocationAR() {
        return locationAR;
    }

    public void setLocationAR(String locationAR) {
        this.locationAR = locationAR;
    }

    public String getLocationEN() {
        return locationEN;
    }

    public void setLocationEN(String locationEN) {
        this.locationEN = locationEN;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }
}
