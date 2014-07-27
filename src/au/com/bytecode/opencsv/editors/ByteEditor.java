package au.com.bytecode.opencsv.editors;

public class ByteEditor extends sun.beans.editors.ByteEditor {
    @Override
    public String getAsText() {
        Byte num = ((Byte)getValue());
        if (num == null) {
            // null to empty string
            return "";
        }

        return super.getAsText();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text.isEmpty()) {
            // empty string to null
            setValue(null);
            return;
        }

        super.setAsText(text);
    }
}
