package au.com.bytecode.opencsv.editors;

public class PrimitiveDoubleEditor extends sun.beans.editors.DoubleEditor {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            super.setAsText(text);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Cannot convert \"\" string to double", text), e);
        }
    }
}
