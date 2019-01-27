package com.cezarmathe.trackexpenses.storage.cell_processors;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

public class ParseOperation extends CellProcessorAdaptor {

    public ParseOperation() {}

    public ParseOperation(CellProcessor next) {
        super(next);
    }

    @Override
    public <Operation> Operation execute(Object value, CsvContext context) {

        validateInputNotNull(value, context);

        return (Operation) com.cezarmathe.trackexpenses.storage.types.Operation.parseString(value.toString());
    }
}
