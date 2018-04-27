package com.sorena.kitadaharuka.reco.models;

/**
 * Created by kitadaharuka on 2018/03/16.
 */

public class User {
    private String targetWeight;
    private String height;
    private String birthday;

    public User() {

    }

    public User(String weight, String height, String birthday) {
        this.targetWeight = weight;
        this.height = height;
        this.birthday = birthday;
    }

    public String getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(String targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
