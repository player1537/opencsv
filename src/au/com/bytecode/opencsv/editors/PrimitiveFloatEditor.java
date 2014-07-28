package au.com.bytecode.opencsv.editors;

import java.beans.*;

public class PrimitiveFloatEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        float num = (Float)getValue();

        return Float.toString(num);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            setValue(Float.parseFloat(text));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Cannot convert \"\" string to float", text), e);
        }
    }
}
