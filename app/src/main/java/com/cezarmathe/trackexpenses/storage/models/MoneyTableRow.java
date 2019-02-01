package com.cezarmathe.trackexpenses.storage.models;

import com.cezarmathe.trackexpenses.storage.types.Operation;
import com.cezarmathe.trackexpenses.storage.types.Tag;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Currency;

public class MoneyTableRow implements Serializable {

    private Double      amount;

    private String      notes;

    private DateTime    dateTime;

    private Currency    currency;

    private Operation   operation;

    private String      tagName;

    private Integer     tagColour;

    public MoneyTableRow() {
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
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


    public Tag getTag() {
        return new Tag(tagName, tagColour);
    }

    public void setTag(Tag tag) {
        this.tagName = tag.getName();
        this.tagColour = tag.getColor();
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getTagColour() {
        return tagColour;
    }

    public void setTagColour(Integer tagColour) {
        this.tagColour = tagColour;
    }
}
