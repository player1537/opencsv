package au.com.bytecode.opencsv.editors;

import java.beans.*;

public class PrimitiveBooleanEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        boolean bool = (Boolean)getValue();

        return Boolean.toString(bool);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            setValue(Boolean.valueOf(text));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Cannot convert \"\" string to boolean", text), e);
        }
    }
}
