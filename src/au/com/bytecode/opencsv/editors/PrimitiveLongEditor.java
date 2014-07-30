package au.com.bytecode.opencsv.editors;

import java.beans.*;

public class PrimitiveLongEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        long num = (Long)getValue();

        return Long.toString(num);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            setValue(Long.parseLong(text));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Cannot convert \"%s\" string to long", text), e);
        }
    }
}
