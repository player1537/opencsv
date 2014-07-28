package au.com.bytecode.opencsv.editors;

import java.beans.*;

public class PrimitiveShortEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        short num = (Short)getValue();

        return Short.toString(num);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            setValue(Short.parseShort(text));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Cannot convert \"\" string to short", text), e);
        }
    }
}
