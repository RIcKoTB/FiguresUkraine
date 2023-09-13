package com.example.outstandingfiguresofukraine.da.entity;

import javafx.scene.image.Image;

import java.sql.Blob;
import java.sql.Date;

public class Humans {
    private int id;
    private Image photo;
    private String pib;
    private String bio;
    private Date date;

    public Humans(Image photo, String pib, String bio, Date date) {
        this.photo = photo;
        this.pib = pib;
        this.bio = bio;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    public String getPib() {
        return pib;
    }

    public void setPib(String pib) {
        this.pib = pib;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
