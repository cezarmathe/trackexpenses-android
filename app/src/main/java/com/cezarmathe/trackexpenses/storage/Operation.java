package com.cezarmathe.trackexpenses.storage;

public enum Operation {
    ADDITION,
    DELETION;

    @Override
    public String toString() {
        return super.toString();
    }

    public String toSign() {
        switch (this) {
            case ADDITION:
                return "+";
            case DELETION:
                return "-";
            default:
                return "";
        }
    }
}
