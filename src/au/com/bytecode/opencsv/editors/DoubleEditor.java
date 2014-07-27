package au.com.bytecode.opencsv.editors;

public class DoubleEditor extends sun.beans.editors.DoubleEditor {
    @Override
    public String getAsText() {
        Double num = ((Double)getValue());
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
