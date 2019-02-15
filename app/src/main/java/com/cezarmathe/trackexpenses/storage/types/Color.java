package com.cezarmathe.trackexpenses.storage.types;

public class Color {

    private Integer value;

    public Color() {}

    public Color(Integer value) {
        this.value = value;
    }

    public Color(String value) {
        this.value = Integer.valueOf(value);
    }

    public Color(Integer red, Integer green, Integer blue) {
        this.value = (red << 16 | green << 8 | blue);
    }

    public int getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setValue(Integer red, Integer green, Integer blue) {
        value = (red << 16 | green << 8 | blue);
    }

    public Integer getRed() {
        return value / 65536;
    }

    public Integer getGreen() {
        return value / 256 % 256;
    }

    public Integer getBlue() {
        return value % 256;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
