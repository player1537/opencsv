package au.com.bytecode.opencsv.editors;

import java.beans.*;

public class PrimitiveIntegerEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        int num = (Integer)getValue();

        return Integer.toString(num);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            setValue(Integer.parseInt(text));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Cannot convert \"\" string to int", text), e);
        }
    }
}
