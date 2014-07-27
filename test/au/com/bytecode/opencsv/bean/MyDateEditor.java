package au.com.bytecode.opencsv.bean;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateEditor extends PropertyEditorSupport {
    private String format;

    MyDateEditor(String formatString) {
        super();
        format = formatString;
    }

    @Override
    public String getAsText() {
        Date date = ((Date)getValue());
        if (date == null) {
            // null to empty string
            return "";
        }
        else {
            return new SimpleDateFormat(format).format(date);
        }
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Date date;
        if (text.isEmpty()) {
            // empty string to null
            setValue(null);
            return;
        }

        try {
            date = new SimpleDateFormat(format).parse(text);
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format("Cannot parse \"%s\" to Date object", text), e);
        }
        setValue(date);
    }
}
