package com.cezarmathe.trackexpenses.storage.cell_processors;

import org.joda.time.DateTime;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

public class ParseDateTime extends CellProcessorAdaptor {

    public ParseDateTime() {}

    public ParseDateTime(CellProcessor next) {
        super(next);
    }

    @Override
    public DateTime execute(Object value, CsvContext context) {

        validateInputNotNull(value, context);

        return DateTime.parse(value.toString());
    }
}
