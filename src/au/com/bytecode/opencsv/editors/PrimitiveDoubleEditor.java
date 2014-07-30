package au.com.bytecode.opencsv.editors;

import java.beans.*;

public class PrimitiveDoubleEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        double num = (Double)getValue();

        return Double.toString(num);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            setValue(Double.parseDouble(text));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Cannot convert \"%s\" string to double", text), e);
        }
    }
}
