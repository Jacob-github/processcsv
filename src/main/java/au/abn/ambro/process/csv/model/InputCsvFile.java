package au.abn.ambro.process.csv.model;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class InputCsvFile implements Closeable {
    private BufferedReader bufferedReader;
    private LinkedHashMap<String, Offset> offsets;
    private int lineNumber;

    public InputCsvFile(final String filename, final LinkedHashMap<String, Offset> offsets) throws FileNotFoundException {
        try {
            FileInputStream fileInputStream = new FileInputStream(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            this.offsets = offsets;
            this.lineNumber = 0;
        } catch (FileNotFoundException e) {
            log.error("Unable to open file: {}", filename);
            throw e;
        }
    }

    public List<Object[]> readRecords(final int batchSize) {
        final List<Object[]> result = new ArrayList<>();
        try {
            for (int i = 0; i < batchSize; i++) {
                final String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                result.add(extractRecord(line));
                lineNumber++;
                if (log.isDebugEnabled()) {
                    log.debug(lineNumber + ": " + line);
                }
            }
        } catch (IOException ex) {
            log.error("Unable to read file", ex);
        }
        log.info("Read {} records", lineNumber);
        return result;
    }

    private Object[] extractRecord(final String line) {
        final Object[] result = new Object[offsets.size()];
        int i = 0;
        for (final Offset offset: offsets.values()) {
            result[i++] = line.substring(offset.getStart() - 1, offset.getEnd()).trim();
        }
        return result;
    }

    @Override
    public void close() throws IOException {
        if (bufferedReader != null) {
            bufferedReader.close();
        }
    }
}
