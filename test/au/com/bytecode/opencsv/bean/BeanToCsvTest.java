package au.com.bytecode.opencsv.bean;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class BeanToCsvTest {

    private static final String TEST_STRING = "\"name\",\"orderNumber\",\"num\"\n"
            + "\"kyle\",\"abc123456\",\"123\"\n"
            + "\"jimmy\",\"def098765\",\"456\"\n";

    private static final String NULL_TEST_STRING = "\"name\",\"orderNumber\",\"num\"\n"
            + "\"\",\"\",\"1\"\n"
            + "\"\",\"\",\"2\"\n";

    private List<MockBean> testData;
    private List<MockBean> nullData;
    private BeanToCsv<MockBean> bean;

    @Before
    public void setUp() {
        bean = new BeanToCsv<MockBean>();
    }

    @Before
    public void setTestData() {
        testData = new ArrayList<MockBean>();
        MockBean mb = new MockBean();
        mb.setName("kyle");
        mb.setOrderNumber("abc123456");
        mb.setNum(123);
        testData.add(mb);
        mb = new MockBean();
        mb.setName("jimmy");
        mb.setOrderNumber("def098765");
        mb.setNum(456);
        testData.add(mb);
    }

    @Before
    public void setNullData() {
        nullData = new ArrayList<MockBean>();
        MockBean mb = new MockBean();
        mb.setName(null);
        mb.setOrderNumber(null);
        mb.setNum(1);
        nullData.add(mb);
        mb = new MockBean();
        mb.setName(null);
        mb.setOrderNumber(null);
        mb.setNum(2);
        nullData.add(mb);
    }

    private MappingStrategy createErrorMappingStrategy() {
        return new MappingStrategy() {

            public PropertyDescriptor findDescriptor(int col)
                    throws IntrospectionException {
                throw new IntrospectionException("This is the test exception");
            }

            public Object createBean() throws InstantiationException,
                    IllegalAccessException {
                return null;
            }

            public void captureHeader(CSVReader reader) throws IOException {
            }

            public String columnName(int col) {
                return "[" + col + "]";
            }
        };
    }

    @Test(expected = RuntimeException.class)
    public void throwRuntimeExceptionWhenExceptionIsThrown() {
        StringWriter sw = new StringWriter();
        CSVWriter writer = new CSVWriter(sw);
        bean.write(createErrorMappingStrategy(), writer, testData);
    }

    @Test
    public void beanReturnsFalseOnEmptyList() {
        ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<MockBean>();
        strat.setType(MockBean.class);
        String[] columns = new String[]{"name", "orderNumber", "num"};
        strat.setColumnMapping(columns);

        StringWriter sw = new StringWriter();

        assertFalse(bean.write(strat, sw, new ArrayList<Object>()));
    }

    @Test
    public void beanReturnsFalseOnNull() {
        ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<MockBean>();
        strat.setType(MockBean.class);
        String[] columns = new String[]{"name", "orderNumber", "num"};
        strat.setColumnMapping(columns);

        StringWriter sw = new StringWriter();

        assertFalse(bean.write(strat, sw, null));
    }

    @Test
    public void testWriteQuotes() throws IOException {
        ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<MockBean>();
        strat.setType(MockBean.class);
        String[] columns = new String[]{"name", "orderNumber", "num"};
        strat.setColumnMapping(columns);

        StringWriter sw = new StringWriter();

        boolean value = bean.write(strat, sw, testData);

        Assert.assertTrue(value);

        String content = sw.getBuffer().toString();
        Assert.assertNotNull(content);
        Assert.assertEquals(TEST_STRING, content);
    }

    @Test
    public void testWriteNulls() throws IOException {
        ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<MockBean>();
        strat.setType(MockBean.class);
        String[] columns = new String[]{"name", "orderNumber", "num"};
        strat.setColumnMapping(columns);

        StringWriter sw = new StringWriter();

        boolean value = bean.write(strat, sw, nullData);

        Assert.assertTrue(value);

        String content = sw.getBuffer().toString();
        Assert.assertNotNull(content);
        Assert.assertEquals(NULL_TEST_STRING, content);
    }

    @Test
    public void testNum() {
        ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<MockBean>();
        strat.setType(MockBean.class);
        String[] columns = new String[]{"num"};
        strat.setColumnMapping(columns);

        StringWriter sw = new StringWriter();

        List<MockBean> list = new ArrayList<MockBean>();

        list.add(new MockBean());
        list.get(0).setNum(0);

        list.add(new MockBean());
        list.get(1).setNum(-1);

        assertThat(list.size(), is(2));

        boolean value = bean.write(strat, sw, list);

        assertThat(value, is(true));

        assertThat(sw.getBuffer().toString(), is("\"num\"" + "\n"
                        + "\"0\"" + "\n"
                        + "\"-1\"" + "\n"
        ));
    }

    @Test
    public void testNullableNum() {
        ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<MockBean>();
        strat.setType(MockBean.class);
        String[] columns = new String[]{"nullableNum"};
        strat.setColumnMapping(columns);

        StringWriter sw = new StringWriter();

        List<MockBean> list = new ArrayList<MockBean>();

        list.add(new MockBean());
        list.get(0).setNullableNum(0);

        list.add(new MockBean());
        list.get(1).setNullableNum(-1);

        list.add(new MockBean());
        list.get(2).setNullableNum(null);

        assertThat(list.size(), is(3));

        boolean value = bean.write(strat, sw, list);

        assertThat(value, is(true));

        assertThat(sw.getBuffer().toString(), is("\"nullableNum\"" + "\n"
                        + "\"0\"" + "\n"
                        + "\"-1\"" + "\n"
                        + "\"\"" + "\n"
        ));
    }

    @Test
    public void testWriteDate() throws IOException {
        ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<MockBean>();
        strat.setType(MockBean.class);
        String[] columns = new String[]{"date"};
        strat.setColumnMapping(columns);

        StringWriter sw = new StringWriter();

        List<MockBean> list = new ArrayList<MockBean>();
        list.add(new MockBean());
        list.add(new MockBean());

        list.get(0).setDate(new Date(2014-1900,1-1,1));
        list.get(1).setDate(null);

        assertThat(list.size(), is(2));

        bean.putPropertyEditor(Date.class, new MyDateEditor("yyyy-MM-dd"));

        boolean value = bean.write(strat, sw, list);

        assertThat(value, is(true));

        assertThat(sw.getBuffer().toString(), is("\"date\"" + "\n"
                        + "\"2014-01-01\"" + "\n"
                        + "\"\"" + "\n"
        ));
   }
}
