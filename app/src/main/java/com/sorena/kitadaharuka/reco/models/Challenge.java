package com.sorena.kitadaharuka.reco.models;

import java.util.List;

/**
 * Created by kitadaharuka on 2018/04/01.
 */

public class Challenge {
    private String id;
    private String name;
    private String current_day;
    private List<String> data;
    private List<Integer> check_list;

    public Challenge() {
    }

    public Challenge(String id, String name, String current_day, List<String> data, List<Integer> check_list) {
        this.id = id;
        this.name = name;
        this.current_day = current_day;
        this.data = data;
        this.check_list = check_list;
    }

    public Challenge(String id, String name, String current_day, List<String> data) {
        this.id = id;
        this.name = name;
        this.current_day = current_day;
        this.data = data;
    }

    public Challenge(String id, String name, List<String> data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }

    public Challenge(String name, String current_day) {
        this.name = name;
        this.current_day = current_day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrent_day() {
        return current_day;
    }

    public void setCurrent_day(String current_day) {
        this.current_day = current_day;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public List<Integer> getCheck_list() {
        return check_list;
    }

    public void setCheck_list(List<Integer> check_list) {
        this.check_list = check_list;
    }
}
