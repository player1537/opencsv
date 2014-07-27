package au.com.bytecode.opencsv.editors;

public class PrimitiveShortEditor extends sun.beans.editors.ShortEditor {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            super.setAsText(text);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Cannot convert \"\" string to short", text), e);
        }
    }
}
