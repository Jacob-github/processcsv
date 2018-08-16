package au.abn.ambro.process.csv.service.impl;

import au.abn.ambro.process.csv.config.FieldOffset;
import au.abn.ambro.process.csv.model.InputCsvFile;
import au.abn.ambro.process.csv.model.OutputCsvFile;
import au.abn.ambro.process.csv.service.CsvProcessor;
import au.abn.ambro.process.csv.service.DataStore;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Getter
@Setter
public class CsvProcessorImpl implements CsvProcessor{
    @Value("${ambro.processor.batchsize}")
    private int batchSize;

    @Value("${ambro.processor.headers}")
    private String headers;

    @Autowired
    private DataStore dataStore;

    @Autowired
    private FieldOffset fieldOffset;

    @Override
    public void processCsv(final String inputFilename, final String outputFilename, final String clientNumber) {
        try (final InputCsvFile inputCsvFile = new InputCsvFile(inputFilename, fieldOffset.getOffsets())) {
            dataStore.initialise();
            List<Object[]> batch = inputCsvFile.readRecords(batchSize);
            while (batch.size() != 0) {
                dataStore.storeRecords(batch);
                batch = inputCsvFile.readRecords(batchSize);
            }
            final List<Map<String, Object>> summary = dataStore.getSummary(clientNumber);
            OutputCsvFile outputCsvFile = new OutputCsvFile(outputFilename, headers);
            outputCsvFile.write(summary);
            outputCsvFile.close();
        } catch (Exception e) {
            log.error("Error processing CSV", e);
        }
    }
}
