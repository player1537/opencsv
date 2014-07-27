package au.com.bytecode.opencsv.bean;

import au.com.bytecode.opencsv.CSVReader;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringReader;
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
                "\"0\"\n" +
                "-1\n";

        ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<MockBean>();
        strat.setType(MockBean.class);

        strat.setColumnMapping("num");

        CsvToBean<MockBean> csv = new CsvToBean<MockBean>();
        List<MockBean> list = csv.parse(strat, new StringReader(s));
        assertThat(list, is(notNullValue()));
        assertThat(list.size(), is(2));

        assertThat(list.get(0).getNum(), is(0));
        assertThat(list.get(1).getNum(), is(-1));
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
                "\"0\"\n" +
                "\"-1\"\n" +
                "\"\"\n";

        ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<MockBean>();
        strat.setType(MockBean.class);

        strat.setColumnMapping("nullableNum");

        CsvToBean<MockBean> csv = new CsvToBean<MockBean>();
        List<MockBean> list = csv.parse(strat, new StringReader(s));
        assertThat(list, is(notNullValue()));
        assertThat(list.size(), is(3));

        assertThat(list.get(0).getNullableNum(), is(0));
        assertThat(list.get(1).getNullableNum(), is(-1));
        assertThat(list.get(2).getNullableNum(), is(nullValue()));
    }
}
