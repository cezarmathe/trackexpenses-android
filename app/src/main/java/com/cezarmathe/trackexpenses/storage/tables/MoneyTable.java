package com.cezarmathe.trackexpenses.storage.tables;

import android.util.Log;

import com.cezarmathe.trackexpenses.storage.Table;
import com.cezarmathe.trackexpenses.storage.cell_processors.ParseCurrency;
import com.cezarmathe.trackexpenses.storage.cell_processors.ParseOperation;
import com.cezarmathe.trackexpenses.storage.cell_processors.ParseTime;
import com.cezarmathe.trackexpenses.storage.models.MoneyTableRow;

import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public final class MoneyTable extends Table<MoneyTableRow> {

    public MoneyTable(File parentFolder, TableEventHook hook) {
        super("MoneyTable",
                "money_table.csv",
                new String[]{"time", "operation", "amount", "currency", "notes"},
                parentFolder,
                new ArrayList<MoneyTableRow>(),
                hook,
                new CellProcessor[]{
                        new ParseTime(),
                        new ParseOperation(),
                        new ParseDouble(),
                        new ParseCurrency(),
                        null
                });
        Log.d(TAG, "MoneyTable() called with: parentFolder = [" + parentFolder + "], hook = [" + hook + "]");
        Log.i(TAG, "MoneyTable: created");
    }

    @Override
    protected ArrayList<MoneyTableRow> readMiddleWare() throws IOException {
        Log.d(TAG, "readMiddleWare() called");
        ArrayList<MoneyTableRow> beans = new ArrayList<>();
        MoneyTableRow bean;
        while ( (bean = reader.read(MoneyTableRow.class, NAME_MAPPING, PROCESSORS)) != null) {
            beans.add(bean);
            Log.d(TAG, "readMiddleWare: read " + bean);
        }
        Log.d(TAG, "readMiddleWare() returned: " + beans);
        return beans;
    }

    @Override
    protected void writeMiddleWare(ArrayList<MoneyTableRow> beans) throws IOException {
        Log.d(TAG, "writeMiddleWare() called with: beans = [" + beans + "]");
        for (int i = 0; i < beans.size(); i++) {
            final MoneyTableRow bean = beans.get(i);
            writer.write(bean, NAME_MAPPING);
            Log.d(TAG, "writeMiddleWare: wrote " + bean);
        }
        Log.d(TAG, "writeMiddleWare() returned");
    }

    @Override
    public void add(MoneyTableRow bean) {
        Log.d(TAG, "add() called with: bean = [" + bean + "]");
        contents.add(bean);
        write();
    }

    @Override
    public void remove(int index) {
        Log.d(TAG, "remove() called with: index = [" + index + "]");
        if (checkIfIndexIsValid(index)) {
            contents.remove(index);
        }
    }

    @Override
    public void edit(MoneyTableRow bean, int index) {
        Log.d(TAG, "edit() called with: bean = [" + bean + "], index = [" + index + "]");
        if (checkIfIndexIsValid(index)) {
            contents.set(index, bean);
        }
    }
}
