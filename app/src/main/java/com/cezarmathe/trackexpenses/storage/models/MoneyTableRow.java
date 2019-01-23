package com.cezarmathe.trackexpenses.storage.models;

import com.cezarmathe.trackexpenses.storage.types.Time;
import com.cezarmathe.trackexpenses.storage.types.Operation;

import java.io.Serializable;
import java.util.Currency;

public class MoneyTableRow implements Serializable {

    private double      amount;
    private String      notes;
    private Time        time;
    private Currency    currency;
    private Operation   operation;

    public MoneyTableRow() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
