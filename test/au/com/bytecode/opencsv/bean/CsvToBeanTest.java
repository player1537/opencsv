package au.com.bytecode.opencsv.bean;

import au.com.bytecode.opencsv.CSVReader;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class CsvToBeanTest {

    private static final String TEST_STRING = "name,orderNumber,num\n" +
            "kyle,abc123456,123\n" +
            "jimmy,def098765,456 ";

    private CSVReader createReader() {
        StringReader reader = new StringReader(TEST_STRING);
        return new CSVReader(reader);
    }

    private MappingStrategy createErrorMappingStrategy() {
        return new MappingStrategy() {

            public PropertyDescriptor findDescriptor(int col) throws IntrospectionException {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public Object createBean() throws InstantiationException, IllegalAccessException {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public void captureHeader(CSVReader reader) throws IOException {
                throw new IOException("This is the test exception");
            }

            public String columnName(int col) {
                return null;
            }
        };
    }

    @Test(expected = RuntimeException.class)
    public void throwRuntimeExceptionWhenExceptionIsThrown() {
        CsvToBean bean = new CsvToBean();
        bean.parse(createErrorMappingStrategy(), createReader());
    }

    @Test
    public void testNum() {
        String s = "" +
                "\"1\"\n" +
                "\"0\"\n" +
                "\"-1\"\n" +
                "\"010\"\n" +   // not octal, but decimal
                "\"2147483647\"\n" + // max int value
                "\"-2147483648\""; // min int value

        ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<MockBean>();
        strat.setType(MockBean.class);

        strat.setColumnMapping("num");

        CsvToBean<MockBean> csv = new CsvToBean<MockBean>();
        List<MockBean> list = csv.parse(strat, new StringReader(s));
        assertThat(list, is(notNullValue()));

        int i = 0;
        assertThat(list.get(i++).getNum(), is(1));
        assertThat(list.get(i++).getNum(), is(0));
        assertThat(list.get(i++).getNum(), is(-1));
        assertThat(list.get(i++).getNum(), is(10));
        assertThat(list.get(i++).getNum(), is(Integer.MAX_VALUE));
        assertThat(list.get(i++).getNum(), is(Integer.MIN_VALUE));
        assertThat(list.size(), is(i));
    }

    @Test
    public void testNumErrorForEmptyString() {
        String s = "" +
                "\"\"\n";

        ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<MockBean>();
        strat.setType(MockBean.class);

        strat.setColumnMapping("num");

        CsvToBean<MockBean> csv = new CsvToBean<MockBean>();
        try {
            csv.parse(strat, new StringReader(s));
        }
        catch (RuntimeException e) {
            return;
        }
        fail("should cause Exception");
    }

    @Test
    public void testNullableNum() {
        String s = "" +
                "\"1\"\n" +
                "\"0\"\n" +
                "\"-1\"\n" +
                "\"010\"\n" +   // not octal, but decimal
                "\"2147483647\"\n" + // max int value
                "\"-2147483648\"\n" + // min int value
                "\"\"\n"; // null

        ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<MockBean>();
        strat.setType(MockBean.class);

        strat.setColumnMapping("nullableNum");

        CsvToBean<MockBean> csv = new CsvToBean<MockBean>();
        List<MockBean> list = csv.parse(strat, new StringReader(s));
        assertThat(list, is(notNullValue()));

        int i = 0;
        assertThat(list.get(i++).getNullableNum(), is(1));
        assertThat(list.get(i++).getNullableNum(), is(0));
        assertThat(list.get(i++).getNullableNum(), is(-1));
        assertThat(list.get(i++).getNullableNum(), is(10));
        assertThat(list.get(i++).getNullableNum(), is(Integer.MAX_VALUE));
        assertThat(list.get(i++).getNullableNum(), is(Integer.MIN_VALUE));
        assertThat(list.get(i++).getNullableNum(), is(nullValue()));

        assertThat(list.size(), is(i));
    }

    @Test
    public void testDate() {
        String s = "" +
                "\"2014-1-1\"\n" +
                "\"\"\n";

        ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<MockBean>();
        strat.setType(MockBean.class);

        strat.setColumnMapping("date");

        CsvToBean<MockBean> csv = new CsvToBean<MockBean>();

        csv.putPropertyEditor(Date.class, new MyDateEditor("yyyy-MM-dd"));

        List<MockBean> list = csv.parse(strat, new StringReader(s));
        assertThat(list, is(notNullValue()));

        int i = 0;
        assertThat(list.get(i++).getDate(), is(new Date(2014-1900,1-1,1)));
        assertThat(list.get(i++).getDate(), is(nullValue()));
        assertThat(list.size(), is(i));
    }

    @Test(expected = RuntimeException.class)
    public void testConvertError1() {
        String s = "" +
                "\"\"\n";

        ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<MockBean>();
        strat.setType(MockBean.class);

        strat.setColumnMapping("num");

        CsvToBean<MockBean> csv = new CsvToBean<MockBean>();
        try {
            csv.parse(strat, new StringReader(s));
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("Error parsing CSV at line 1: [column: num, value: ], Cannot convert \"\" string to int"));
            throw e;
        }
        fail();
    }

    @Test(expected = RuntimeException.class)
    public void testConvertError2() {
        String s = "" +
                "\"0\", \"2011-1-1\"\n";

        ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<MockBean>();
        strat.setType(MockBean.class);

        strat.setColumnMapping(new String[] {"num", "nullableNum"});

        CsvToBean<MockBean> csv = new CsvToBean<MockBean>();
        try {
            csv.parse(strat, new StringReader(s));
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("Error parsing CSV at line 1: [column: nullableNum, value: 2011-1-1], For input string: \"2011-1-1\""));
            throw e;
        }
        fail();
    }

}
