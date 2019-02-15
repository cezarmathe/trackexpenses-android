package com.cezarmathe.trackexpenses.storage.models;

import com.cezarmathe.trackexpenses.storage.cell_processors.ParseCurrency;
import com.cezarmathe.trackexpenses.storage.cell_processors.ParseLocalTime;
import com.cezarmathe.trackexpenses.storage.cell_processors.ParseOperation;
import com.cezarmathe.trackexpenses.storage.types.Color;
import com.cezarmathe.trackexpenses.storage.types.Operation;
import com.cezarmathe.trackexpenses.storage.types.Tag;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.io.Serializable;
import java.util.Currency;

public class MoneyTableRow implements Serializable {

    public static final transient String[] NAME_MAPPING = {
            "dateYear",
            "dateMonth",
            "dateDay",
            "time",
            "operation",
            "amount",
            "currency",
            "tagName",
            "tagColor"
    };


    public static final transient CellProcessor[] WRITE_CELL_PROCESSORS = null;

    public static final transient CellProcessor[] READ_CELL_PROCESSORS = {
            new ParseInt(),
            new ParseInt(),
            new ParseInt(),
            new ParseLocalTime(),
            new ParseOperation(),
            new ParseDouble(),
            new ParseCurrency(),
            new Optional(),
            new Optional()
    };

    private Color       tagColor;

    private Operation   operation;

    private Double      amount;
    private Currency    currency;

    private int         dateYear;
    private int         dateMonth;
    private int         dateDay;
    private LocalTime   time;

    private String      tagName;

    private String      notes;

    public MoneyTableRow() {
    }

    public Color getTagColor() {
        return tagColor;
    }

    public void setTagColor(Color tagColor) {
        this.tagColor = tagColor;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getDateYear() {
        return dateYear;
    }

    public void setDateYear(int dateYear) {
        this.dateYear = dateYear;
    }

    public int getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(int dateMonth) {
        this.dateMonth = dateMonth;
    }

    public int getDateDay() {
        return dateDay;
    }

    public void setDateDay(int dateDay) {
        this.dateDay = dateDay;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public DateTime getDateTime() {
        return new DateTime()
                .withYear(dateYear)
                .withMonthOfYear(dateMonth)
                .withDayOfMonth(dateDay)
                .withTime(time);
    }

    public void setDateTime(DateTime dateTime) {
        dateYear = dateTime.getYear();
        dateMonth = dateTime.getMonthOfYear();
        dateDay = dateTime.getDayOfYear();
        time = dateTime.toLocalTime();
    }

    public Tag getTag() {
        return new Tag(tagName, tagColor);
    }

    public void setTag(Tag tag) {
        tagName = tag.getName();
        tagColor = tag.getColor();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
