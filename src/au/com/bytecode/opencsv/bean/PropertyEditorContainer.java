package au.com.bytecode.opencsv.bean;

import au.com.bytecode.opencsv.editors.*;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

class PropertyEditorContainer {
    private Map<Class<?>, PropertyEditor> editorMap = new HashMap<Class<?>, PropertyEditor>();

    PropertyEditorContainer() {
        put(boolean.class, new PrimitiveBooleanEditor());
        put(byte.class, new PrimitiveByteEditor());
        put(double.class, new PrimitiveDoubleEditor());
        put(float.class, new PrimitiveFloatEditor());
        put(int.class, new PrimitiveIntegerEditor());
        put(long.class, new PrimitiveLongEditor());
        put(short.class, new PrimitiveShortEditor());
        put(Boolean.class, new BooleanEditor());
        put(Byte.class, new ByteEditor());
        put(Double.class, new DoubleEditor());
        put(Float.class, new FloatEditor());
        put(Integer.class, new IntegerEditor());
        put(Long.class, new LongEditor());
        put(Short.class, new ShortEditor());
        put(String.class, new StringEditor());
    }
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
