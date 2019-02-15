package com.cezarmathe.trackexpenses.storage.cell_processors;

import org.joda.time.LocalTime;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

public class ParseLocalTime extends CellProcessorAdaptor {

    public ParseLocalTime() {}

    public ParseLocalTime(CellProcessor next) {
        super(next);
    }

    @Override
    public LocalTime execute(Object value, CsvContext context) {

        validateInputNotNull(value, context);

        return LocalTime.parse(value.toString());
    }
}
