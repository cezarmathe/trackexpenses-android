package com.cezarmathe.trackexpenses.storage.tables;

import com.cezarmathe.trackexpenses.storage.Table;
import com.cezarmathe.trackexpenses.storage.models.MoneyTableRow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class NewMoneyTable extends Table {

    public NewMoneyTable(String fileName, File parentFolder,  TableEventHook hook) {
        super("MoneyTable",
                "money_table.csv",
                new String[]{},
                parentFolder,
                new ArrayList<MoneyTableRow>(),
                hook);

    }

    @Override
    protected ArrayList<MoneyTableRow> readMiddleWare() throws IOException {
        ArrayList<MoneyTableRow> beans = new ArrayList<>();
        MoneyTableRow bean;
        while ( (bean = reader.read(MoneyTableRow.class, NAME_MAPPING)) != null) {
            beans.add(bean);
        }
        return beans;
    }

    @Override
    protected void writeMiddleWare(ArrayList beans) throws IOException {
        for (int i = 0; i < beans.size(); i++) {
            final MoneyTableRow bean = (MoneyTableRow) beans.get(i);
            writer.write(bean, NAME_MAPPING);
        }
    }

    @Override
    public <T> void add(T bean) {

    }

    @Override
    public void remove(int index) {

    }

    @Override
    public <T> void edit(T bean, int index) {

    }
}
