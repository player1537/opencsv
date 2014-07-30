package au.com.bytecode.opencsv.editors;

import java.beans.*;

public class PrimitiveByteEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        byte num = (Byte)getValue();

        return Byte.toString(num);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            setValue(Byte.parseByte(text));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Cannot convert \"%s\" string to byte", text), e);
        }
    }
}
