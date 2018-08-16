package au.abn.ambro.process.csv.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UtilsTest {

    @Test
    public void shouldReadResourceFileContent() throws Exception {
        final String content = Utils.readResourceFile(this.getClass(), "Input-test.txt");
        assertThat(content, is("111112222233333333334444444444\r\n555556666677777777778888888888"));
    }

    @Test
    public void shouldConcatenateListElementsWithCommas() {
        final List<String> list = new ArrayList<>();
        list.add("entry1");
        list.add("entry2");
        list.add("entry3");
        final String result = Utils.toCommaSeparated(list);
        assertThat(result, is("entry1,entry2,entry3"));
    }
}