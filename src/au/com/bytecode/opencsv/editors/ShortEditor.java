package au.com.bytecode.opencsv.editors;

public class ShortEditor extends sun.beans.editors.ShortEditor {
    @Override
    public String getAsText() {
        Short num = ((Short)getValue());
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
