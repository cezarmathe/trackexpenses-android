package com.cezarmathe.trackexpenses.storage.types;

import android.graphics.Color;

public class Tag {

    private String  name;

    private Integer color;

    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

    public Tag(String name, Integer color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
}
