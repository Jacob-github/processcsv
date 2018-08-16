package au.abn.ambro.process.csv.service.impl;

import au.abn.ambro.process.csv.config.FieldOffset;
import au.abn.ambro.process.csv.model.Offset;
import au.abn.ambro.process.csv.service.DataStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.Charset.defaultCharset;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.readAllLines;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CsvProcessorImplTest {
    @InjectMocks
    CsvProcessorImpl csvProcessor;

    @Mock
    DataStore dataStore;

    @Mock
    FieldOffset fieldOffset;

    @Before
    public void setUp() throws Exception {
        final LinkedHashMap offsets = new LinkedHashMap();
        Offset offset = new Offset();
        offset.setStart(1);
        offset.setEnd(3);
        offsets.put("H1", offset);
        given(fieldOffset.getOffsets()).willReturn(offsets);

        final File f = new File("Input-test.txt");
        final BufferedWriter writer = new BufferedWriter(new FileWriter("Input-test.txt"));
        writer.write("1234567890");
        writer.close();
    }

    @After
    public void tearDown() throws Exception {
        delete(Paths.get("Input-test.txt"));
    }

    @Test
    public void test() throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        LinkedHashMap<String, Object> entry = new LinkedHashMap<>();
        entry.put("H1", "VALUE1");
        result.add(entry);
        given(dataStore.getSummary("1234")).willReturn(result);
        csvProcessor.processCsv("Input-test.txt", "Output-test.csv", "1234");
        verify(dataStore).getSummary("1234");
        verify(dataStore).initialise();

        final List<String> lines = readAllLines(Paths.get("Output-test.csv"), defaultCharset());
        assertThat(lines.get(1), is("VALUE1"));
        delete(Paths.get("Output-test.csv"));
    }
}