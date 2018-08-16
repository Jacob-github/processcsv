package au.abn.ambro.process.csv.model;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.Charset.defaultCharset;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OutputCsvFileTest {

    @Test
    public void shouldWriteCsvFile() throws Exception {
        final OutputCsvFile outputCsvFile = new OutputCsvFile("test-write-csv.txt", "H1,H2,H3");
        final List<Map<String, Object>> entries = new ArrayList<>();
        final LinkedHashMap<String, Object> entry1 = new LinkedHashMap<>();
        final LinkedHashMap<String, Object> entry2 = new LinkedHashMap<>();
        entry1.put("H1", "V1.1");
        entry1.put("H2", "V1.2");
        entry1.put("H3", "V1.3");
        entry2.put("H1", "V2.1");
        entry2.put("H2", "V2.2");
        entry2.put("H3", "V2.3");
        entries.add(entry1);
        entries.add(entry2);
        outputCsvFile.write(entries);
        outputCsvFile.close();

        final List<String> lines = Files.readAllLines(Paths.get("test-write-csv.txt"), defaultCharset());
        assertThat(lines.get(0), is("H1,H2,H3"));
        assertThat(lines.get(1), is("V1.1,V1.2,V1.3"));
        assertThat(lines.get(2), is("V2.1,V2.2,V2.3"));

        Files.delete(Paths.get("test-write-csv.txt"));
    }
}