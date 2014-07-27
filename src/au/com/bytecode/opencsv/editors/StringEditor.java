package au.com.bytecode.opencsv.editors;

public class StringEditor extends sun.beans.editors.StringEditor {
    @Override
    public String getAsText() {
        String str = (String)getValue();
        if (str == null) {
            // null to empty string
            return "null";
        }

        return super.getAsText();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        super.setAsText(text);
    }
}
