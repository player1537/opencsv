package au.com.bytecode.opencsv.editors;

public class BooleanEditor extends sun.beans.editors.BooleanEditor {
    @Override
    public String getAsText() {
        Boolean bool = ((Boolean)getValue());
        if (bool == null) {
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
