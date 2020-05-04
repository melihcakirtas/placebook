package com.melih.placebook;

import android.graphics.Bitmap;



public class PlacessClass {
    private static PlacessClass instance;

    private Bitmap image;
    private String name;
    private String detail;

    private PlacessClass(){

    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    public static PlacessClass getinstance(){
        if(instance == null){
            instance = new PlacessClass();
        }
        return instance;
    }
}
