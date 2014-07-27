package au.com.bytecode.opencsv.bean;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

class PropertyEditorContainer {
    private Map<Class<?>, PropertyEditor> editorMap = new HashMap<Class<?>, PropertyEditor>();

    private PropertyEditor getPropertyEditorValue(Class<?> cls) {
        PropertyEditor editor = editorMap.get(cls);

        if (editor == null) {
            editor = java.beans.PropertyEditorManager.findEditor(cls);
            put(cls, editor);
        }

        return editor;
    }

    public void put(Class<?> cls, PropertyEditor editor) {
        if (editor != null) {
            editorMap.put(cls, editor);
        }
    }

    /*
     * Attempt to find custom property editor on descriptor first, else try the propery editor manager.
     */
    public PropertyEditor get(PropertyDescriptor desc) throws InstantiationException, IllegalAccessException {
        Class<?> cls = desc.getPropertyEditorClass();
        if (null != cls) return (PropertyEditor) cls.newInstance();
        return getPropertyEditorValue(desc.getPropertyType());
    }
}
