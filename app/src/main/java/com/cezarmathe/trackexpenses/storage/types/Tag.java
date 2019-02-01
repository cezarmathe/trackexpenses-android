package com.cezarmathe.trackexpenses.storage.types;

import android.graphics.Color;

public class Tag {

    private String name;

    private Color color;

    public Tag() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
