package au.abn.ambro.process.csv.model;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static au.abn.ambro.process.csv.utils.Utils.toCommaSeparated;

@Slf4j
public class OutputCsvFile {

    private PrintWriter printWriter;

    public OutputCsvFile(final String filename, final String headers) throws FileNotFoundException {
        printWriter = new PrintWriter(new File(filename));
        printWriter.println(headers);
    }

    public void write(final List<Map<String, Object>> entries) {
        log.info("Write to output file:");
        for (final Map<String, Object> m : entries) {
            final String summaryRow = toCommaSeparated(m.values());
            printWriter.println(summaryRow);
            log.info(summaryRow);
        }
    }

    public void close() {
        printWriter.close();
    }
}
