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

import java.beans.*;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CsvToBean<T> {
    private PropertyEditorContainer propertyEditorManager = new PropertyEditorContainer();

    public CsvToBean() {
    }

    boolean strictMode = false;

    public CsvToBean<T> strict(boolean bool) {
        this.strictMode = bool;
        return this;
    }

    boolean checkFullyPopulated = false;

    public CsvToBean<T> setCheckFullyPopulated(boolean bool) {
        this.strictMode = bool;
        this.checkFullyPopulated = bool;
        return this;
    }

    public static <T> Set<String> getWritablePropertyNames(Class<T> beanClass) {
        Set<String> names = new HashSet<String>();

        PropertyDescriptor[] props;
        try {
            props = Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            return names;
        }

        for (PropertyDescriptor prop : props) {
            if (prop.getWriteMethod() != null) {
                names.add(prop.getName());
            }
        }
        return names;
    }

    /* get property names preserve order defined by class */
    public static <T> Set<String> getWritablePropertyNamesOrdered(Class<T> beanClass) {
        Set<String> names = getWritablePropertyNames(beanClass);
        Set<String> orderedNames = new LinkedHashSet<String>();

        for (Field field : beanClass.getDeclaredFields()) {
            // field.setAccessible(true);
            String fieldName = field.getName();
            if (names.contains(fieldName)) {
                orderedNames.add(fieldName);
            }
        }
        return orderedNames;
    }

    protected class ColumnNameValidator<T> {
        Class<T> beanClass;
        boolean strict;
        Set<String> expectUpperColumnNames = new HashSet<String>();
        Set<String> actualUpperColumnNames = new HashSet<String>();
        Set<String> beanProperties;

        public ColumnNameValidator(Class<T> beanClass) {
            this.beanClass = beanClass;
            this.beanProperties = getWritablePropertyNames(beanClass);

            strict = (beanProperties != null);
            if (strict) {
                for (String name : beanProperties) {
                    expectUpperColumnNames.add(name.toUpperCase());
                }
            }
        }
        public void validateColumnName(String columnName, String value) {
            if (!strict) {
                return;
            }

            String upperColumnName = columnName.toUpperCase();

            if (!expectUpperColumnNames.contains(upperColumnName)) {
                throw new IllegalArgumentException(String.format(
                        "[column: %s, value: %s], column \"%s\" is not contained in the bean \"%s\"",
                        columnName, value, columnName, beanClass.getName()));

            }

            if (actualUpperColumnNames.contains(upperColumnName)) {
                throw new IllegalArgumentException(String.format(
                        "[column: %s, value: %s], column \"%s\" has been already set for the bean \"%s\"",
                        columnName, value, columnName, beanClass.getName()));
            }

            actualUpperColumnNames.add(upperColumnName);
        }

        public void validateRemaining() {
            if (!strict) {
                return;
            }
            if (!checkFullyPopulated) {
                return;
            }

            // strict check
            List unsetColumns = new ArrayList();
            for (String name: beanProperties) {
                if (!actualUpperColumnNames.contains(name.toUpperCase())) {
                    unsetColumns.add(name);
                }
            }
            if (unsetColumns.size() > 0) {
                Collections.sort(unsetColumns);
                throw new IllegalArgumentException(String.format("given CSV does not contain all fiedlds. fields %s have not been set",
                        unsetColumns.toString()));
            }
        }
    }

    public List<T> parse(MappingStrategy<T> mapper, Reader reader) {
        return parse(mapper, new CSVReader(reader));
    }

    public List<T> parse(MappingStrategy<T> mapper, CSVReader csv) {
        ColumnNameValidator validator = (strictMode) ? new ColumnNameValidator(mapper.getType()) : null;

        int i = 0;
        try {
            mapper.captureHeader(csv);
            String[] line;
            List<T> list = new ArrayList<T>();
            while (null != (line = csv.readNext())) {
                i++;
                T obj = processLine(mapper, line, validator);
                list.add(obj); // TODO: (Kyle) null check object

                validator = null; // strict checking is caused only at first line
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error parsing CSV at line %d: %s", i, e.getMessage()), e);
        }
    }

    protected T processLine(MappingStrategy<T> mapper, String[] line, ColumnNameValidator validator)
            throws IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException {
        T bean = mapper.createBean();

        for (int col = 0; col < line.length; col++) {
            String columnName = mapper.columnName(col);

            if (validator != null) {
                validator.validateColumnName(columnName, line[col]);
            }

            PropertyDescriptor prop = mapper.findDescriptor(col);
            if (prop == null) {
                continue;
            }
            String value = checkForTrim(line[col], prop);

            Object obj;
            try {
                obj = convertValue(value, prop);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(String.format("[column: %s, value: %s], %s",
                        columnName, line[col], e.getMessage()), e);
            }

            try {
                prop.getWriteMethod().invoke(bean, obj);
            } catch (IllegalArgumentException e) {
                // type mismatch
                if (obj instanceof String && ((String)obj).isEmpty()) {
                    // If the prop has not the PropertyEditor and obj is empty string.
                    // set null instead of cause type mismatch error.
                    prop.getWriteMethod().invoke(bean, new Object[]{null});
                }
                else {
                    throw e;
                }
            }
        }

        if (validator != null) {
            validator.validateRemaining();
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
