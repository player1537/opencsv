package au.com.bytecode.opencsv.editors;

public class IntegerEditor extends sun.beans.editors.IntegerEditor {
    @Override
    public String getAsText() {
        Integer num = ((Integer)getValue());
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
