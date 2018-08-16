package au.abn.ambro.process.csv.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class InputCsvFileTest {

    private LinkedHashMap<String, Offset> offsets = new LinkedHashMap();

    @Before
    public void setUp() {
        Offset offset1 = new Offset();
        offset1.setStart(1);
        offset1.setEnd(5);
        Offset offset2 = new Offset();
        offset2.setStart(11);
        offset2.setEnd(20);
        offsets.put("field1", offset1);
        offsets.put("field2", offset2);
    }

    @Test
    public void shouldReadInputFileAndParseCorrectly() throws Exception {
        final InputCsvFile inputCsvFile = new InputCsvFile("src/test/resources/Input-test.txt", offsets);
        List<Object[]> list = inputCsvFile.readRecords(1);

        assertThat(list.size(), is(1));
        assertThat(list.get(0)[0], is("11111"));
        assertThat(list.get(0)[1], is("3333333333"));

        list = inputCsvFile.readRecords(1);

        assertThat(list.size(), is(1));
        assertThat(list.get(0)[0], is("55555"));
        assertThat(list.get(0)[1], is("7777777777"));

        list = inputCsvFile.readRecords(1);
        assertThat(list.size(), is(0));

        inputCsvFile.close();
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldThrowExceptionWhenFileNotFound() throws Exception {
        final InputCsvFile inputCsvFile = new InputCsvFile("no-exist.txt", offsets);
    }
}