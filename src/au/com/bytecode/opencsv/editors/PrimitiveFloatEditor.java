package au.com.bytecode.opencsv.editors;

public class PrimitiveFloatEditor extends sun.beans.editors.FloatEditor {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            super.setAsText(text);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Cannot convert \"\" string to float", text), e);
        }
    }
}
