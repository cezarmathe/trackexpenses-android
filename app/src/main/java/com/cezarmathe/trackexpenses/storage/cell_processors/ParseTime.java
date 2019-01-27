package com.cezarmathe.trackexpenses.storage.cell_processors;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

public class ParseTime extends CellProcessorAdaptor {

    public ParseTime() {
        super();
    }

    public ParseTime(CellProcessor next) {
        super(next);
    }

    @Override
    public <Time> Time execute(Object value, CsvContext context) {

        validateInputNotNull(value, context);

        return (Time) com.cezarmathe.trackexpenses.storage.types.Time.newInstance();
    }
}
