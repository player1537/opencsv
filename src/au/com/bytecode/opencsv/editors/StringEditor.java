package au.com.bytecode.opencsv.editors;

import java.beans.*;

public class StringEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        String str = (String)getValue();
        if (str == null) {
            // null to empty string
            return "";
        }

        return super.getAsText();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(text);
    }
}
