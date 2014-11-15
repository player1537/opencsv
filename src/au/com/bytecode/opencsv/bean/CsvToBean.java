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

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.editors.*;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CsvToBean<T> {
    private PropertyEditorContainer propertyEditorManager = new PropertyEditorContainer();

    public CsvToBean() {
    }

    public List<T> parse(MappingStrategy<T> mapper, Reader reader) {
        return parse(mapper, new CSVReader(reader));
    }

    public List<T> parse(MappingStrategy<T> mapper, CSVReader csv) {
        T obj = null;
        int i = 0;
        try {
            mapper.captureHeader(csv);
            String[] line;
            List<T> list = new ArrayList<T>();
            while (null != (line = csv.readNext())) {
                i++;
                obj = processLine(mapper, line);
                list.add(obj); // TODO: (Kyle) null check object
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error parsing CSV at line %d: %s", i, e.getMessage()), e);
        }
    }

    protected T processLine(MappingStrategy<T> mapper, String[] line) throws IllegalAccessException, InvocationTargetException, InstantiationException, IntrospectionException {
        T bean = mapper.createBean();
        for (int col = 0; col < line.length; col++) {
            PropertyDescriptor prop = mapper.findDescriptor(col);
            if (null != prop) {
                String value = checkForTrim(line[col], prop);
                Object obj;
                try {
                    obj = convertValue(value, prop);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(String.format("[column: %s, value: %s], %s",
                            mapper.columnName(col), line[col], e.getMessage()), e);
                }
                prop.getWriteMethod().invoke(bean, obj);
            }
        }
        return bean;
    }

    private String checkForTrim(String s, PropertyDescriptor prop) {
        return trimmableProperty(prop) ? s.trim() : s;
    }

    private boolean trimmableProperty(PropertyDescriptor prop) {
        return !prop.getPropertyType().getName().contains("String");
    }

    protected Object convertValue(String value, PropertyDescriptor prop) throws InstantiationException, IllegalAccessException {
        PropertyEditor editor = propertyEditorManager.get(prop);
        Object obj = value;
        if (null != editor) {
            editor.setAsText(value);
            obj = editor.getValue();
        }
        return obj;
    }

    public void putPropertyEditor(Class<?> cls, PropertyEditor editor) {
        propertyEditorManager.put(cls, editor);
    }
}
