package com.cezarmathe.trackexpenses.storage;

import java.util.Currency;
import java.util.Date;

public class StorageTableRow {

    public double amount;
    public String notes;
    public Date date;
    public Currency currency;
    public Operation operation;

    public StorageTableRow(double amount, String notes, Date date, Currency currency, Operation operation) {
        this.amount = amount;
        this.notes = notes;
        this.date = date;
        this.currency = currency;
        this.operation = operation;
    }

}
