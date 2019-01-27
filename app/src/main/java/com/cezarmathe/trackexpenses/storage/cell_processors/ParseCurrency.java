package com.cezarmathe.trackexpenses.storage.cell_processors;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

import java.util.Currency;

public class ParseCurrency extends CellProcessorAdaptor {

    public ParseCurrency() {}

    public ParseCurrency(CellProcessor next) {
        super(next);
    }

    @Override
    public <Currency> Currency execute(Object value, CsvContext context) {

        validateInputNotNull(value, context);

        return (Currency) java.util.Currency.getInstance(value.toString());
    }
}
