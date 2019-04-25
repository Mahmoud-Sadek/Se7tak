package com.sadek.se7tak.model;

import java.io.Serializable;

public class AboutDoctor implements Serializable {
    String id, aboutAR, aboutEN;
    public AboutDoctor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAboutAR() {
        return aboutAR;
    }

    public void setAboutAR(String aboutAR) {
        this.aboutAR = aboutAR;
    }

    public String getAboutEN() {
        return aboutEN;
    }

    public void setAboutEN(String aboutEN) {
        this.aboutEN = aboutEN;
    }
}
