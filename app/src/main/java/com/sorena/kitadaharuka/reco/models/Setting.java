package com.sorena.kitadaharuka.reco.models;

import java.io.Serializable;

/**
 * Created by kitadaharuka on 2018/03/31.
 */

public class Setting implements Serializable{
    private String title;
    private String value;

    public Setting(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
