package com.sorena.kitadaharuka.reco.models;

/**
 * Created by kitadaharuka on 2018/03/25.
 */

public class Records {
    private String recordDate;
    private float weight;
    private float bodyFat;
    private float muscle;
    private String note;

    public Records() {

    }

    public Records(String recordDate) {
        this.recordDate = recordDate;
        this.weight = 0;
        this.bodyFat = 0;
        this.muscle = 0;
        this.note = "";
    }

    public Records(String recordDate, float weight, float bodyFat, float muscle, String note) {
        this.recordDate = recordDate;
        this.weight = weight;
        this.bodyFat = bodyFat;
        this.muscle = muscle;
        this.note = note;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(float bodyFat) {
        this.bodyFat = bodyFat;
    }

    public float getMuscle() {
        return muscle;
    }

    public void setMuscle(float muscle) {
        this.muscle = muscle;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
