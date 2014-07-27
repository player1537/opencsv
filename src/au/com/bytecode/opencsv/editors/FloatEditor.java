package au.com.bytecode.opencsv.editors;

public class FloatEditor extends sun.beans.editors.FloatEditor {
    @Override
    public String getAsText() {
        Float num = ((Float)getValue());
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
