package au.com.bytecode.opencsv.bean;

/**
 Copyright 2007 Kyle Miller.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.editors.IntegerEditor;
import au.com.bytecode.opencsv.editors.PrimitiveIntegerEditor;
import au.com.bytecode.opencsv.editors.StringEditor;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Allows to export Java beans content to a new CSV spreadsheet file.
 *
 * @author Kali &lt;kali.tystrit@gmail.com&gt;
 */
public class BeanToCsv<T> {
    private PropertyEditorContainer propertyEditorManager = new PropertyEditorContainer();

    public BeanToCsv() {
        propertyEditorManager.put(int.class, new PrimitiveIntegerEditor());
        propertyEditorManager.put(Integer.class, new IntegerEditor());
        propertyEditorManager.put(String.class, new StringEditor());
    }

    public boolean write(MappingStrategy<T> mapper, Writer writer,
                         List<?> objects) {
        return write(mapper, new CSVWriter(writer), objects);
    }

    public boolean write(MappingStrategy<T> mapper, CSVWriter csv,
                         List<?> objects) {
        if (objects == null || objects.isEmpty())
            return false;

        try {
            csv.writeNext(processHeader(mapper));
            List<Method> getters = findGetters(mapper);
            for (Object obj : objects) {
                String[] line = processObject(mapper, getters, obj);
                csv.writeNext(line);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error writing CSV !", e);
        }
    }

    protected String[] processHeader(MappingStrategy<T> mapper)
            throws IntrospectionException {
        List<String> values = new ArrayList<String>();
        int i = 0;
        PropertyDescriptor prop = mapper.findDescriptor(i);
        while (prop != null) {
            values.add(prop.getName());
            i++;
            prop = mapper.findDescriptor(i);
        }
        return values.toArray(new String[0]);
    }

    protected String[] processObject(MappingStrategy<T> mapper, List<Method> getters, Object bean)
            throws IntrospectionException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        List<String> values = new ArrayList<String>();

        int i = 0;
        PropertyDescriptor prop = mapper.findDescriptor(i);

        // retrieve bean values
        for (Method getter : getters) {
            Object value = getter.invoke(bean, (Object[]) null);
            PropertyEditor editor = propertyEditorManager.get(prop);
            editor.setValue(value);
            values.add(editor.getAsText());

            i++;
            prop = mapper.findDescriptor(i);
        }
        return values.toArray(new String[0]);
    }

    /**
     * Build getters list from provided mapper.
     */
    private List<Method> findGetters(MappingStrategy<T> mapper)
            throws IntrospectionException {
        int i = 0;
        PropertyDescriptor prop = mapper.findDescriptor(i);
        // build getters methods list
        List<Method> readers = new ArrayList<Method>();
        while (prop != null) {
            readers.add(prop.getReadMethod());
            i++;
            prop = mapper.findDescriptor(i);
        }
        return readers;
    }

    public void putPropertyEditor(Class<?> cls, PropertyEditor editor) {
        propertyEditorManager.put(cls, editor);
    }
}
