package au.com.bytecode.opencsv.editors;

import java.beans.*;

public class DoubleEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        Double num = (Double)getValue();
        if (num == null) {
            // null to empty string
            return "";
        }

        return num.toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text.isEmpty()) {
            // empty string to null
            setValue(null);
            return;
        }

        setValue(Double.valueOf(text));
    }
}
