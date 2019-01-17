package com.cezarmathe.trackexpenses.storage;

import java.util.Date;

public class StorageTableRow {

    public int amount;
    public String notes;
    public Date date;
    public Operation operation;

    public StorageTableRow(int amount, String notes, Date date, Operation operation) {
        this.amount = amount;
        this.notes = notes;
        this.date = date;
        this.operation = operation;
    }

}
