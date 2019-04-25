package com.sadek.se7tak.model;

import java.io.Serializable;

public class DegreeDoctor implements Serializable {
    String id, degreeAR, degreeEN, collegeAR, collegeEN,year, image;
    public DegreeDoctor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDegreeAR() {
        return degreeAR;
    }

    public void setDegreeAR(String degreeAR) {
        this.degreeAR = degreeAR;
    }

    public String getDegreeEN() {
        return degreeEN;
    }

    public void setDegreeEN(String degreeEN) {
        this.degreeEN = degreeEN;
    }

    public String getCollegeAR() {
        return collegeAR;
    }

    public void setCollegeAR(String collegeAR) {
        this.collegeAR = collegeAR;
    }

    public String getCollegeEN() {
        return collegeEN;
    }

    public void setCollegeEN(String collegeEN) {
        this.collegeEN = collegeEN;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
