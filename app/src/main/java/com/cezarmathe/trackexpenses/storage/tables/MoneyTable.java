package com.cezarmathe.trackexpenses.storage.tables;

import android.util.Log;

import com.cezarmathe.trackexpenses.storage.Table;
import com.cezarmathe.trackexpenses.storage.cell_processors.ParseLocalTime;
import com.cezarmathe.trackexpenses.storage.cell_processors.ParseCurrency;
import com.cezarmathe.trackexpenses.storage.cell_processors.ParseOperation;
import com.cezarmathe.trackexpenses.storage.models.MoneyTableRow;

import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class MoneyTable extends Table<MoneyTableRow> {

    public MoneyTable(File parentFolder, TableEventHook hook) {
        super("MoneyTable",
                "money_table.csv",
                new String[]{"datetime", "operation", "amount", "currency", "notes", "tagName", "tagColour"},
                parentFolder,
                new ArrayList<MoneyTableRow>(),
                hook,
                new CellProcessor[]{
                        new ParseLocalTime(),
                        new ParseOperation(),
                        new ParseDouble(),
                        new ParseCurrency(),
                        null,
                        null,
                        new ParseInt()
                },
                new CellProcessor[]{
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                },
                MoneyTableRow.class);
        Log.d(TAG, "MoneyTable() called with: parentFolder = [" + parentFolder + "], hook = [" + hook + "]");
        Log.i(TAG, "MoneyTable: created");
    }
}
