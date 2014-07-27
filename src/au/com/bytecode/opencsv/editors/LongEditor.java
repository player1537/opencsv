package au.com.bytecode.opencsv.editors;

public class LongEditor extends sun.beans.editors.LongEditor {
    @Override
    public String getAsText() {
        Long num = ((Long)getValue());
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
