package au.com.bytecode.opencsv.editors;

public class PrimitiveByteEditor extends sun.beans.editors.ByteEditor {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            super.setAsText(text);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Cannot convert \"\" string to byte", text), e);
        }
    }
}
