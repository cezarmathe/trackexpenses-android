package com.cezarmathe.trackexpenses.storage.types;

import android.support.annotation.Nullable;

public enum Operation {
    ADDITION,
    SUBTRACTION;

    @Override
    public String toString() {
        return super.toString();
    }

    public String toSign() {
        switch (this) {
            case ADDITION:
                return "+";
            case SUBTRACTION:
                return "-";
            default:
                return "";
        }
    }

    public static Operation parseString(@Nullable String string) {
        if (string == null) {
            return Operation.SUBTRACTION;
        }
        switch (string) {
            case "+":
                return Operation.ADDITION;
            case "-":
                return Operation.SUBTRACTION;
            default:
                return Operation.SUBTRACTION;
        }
    }
}
