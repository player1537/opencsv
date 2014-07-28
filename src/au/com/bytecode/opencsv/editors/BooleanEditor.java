package au.com.bytecode.opencsv.editors;

import java.beans.*;

public class BooleanEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        Boolean bool = (Boolean)getValue();
        if (bool == null) {
            // null to empty string
            return "";
        }

        return bool.toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text.isEmpty()) {
            // empty string to null
            setValue(null);
            return;
        }

        setValue(Boolean.valueOf(text));
    }
}
