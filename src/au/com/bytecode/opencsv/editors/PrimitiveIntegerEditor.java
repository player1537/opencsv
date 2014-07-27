package au.com.bytecode.opencsv.editors;

public class PrimitiveIntegerEditor extends sun.beans.editors.IntegerEditor {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            super.setAsText(text);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Cannot convert \"\" string to int", text), e);
        }
    }
}
