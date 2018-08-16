package au.abn.ambro.process.csv.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DataStore {

    void initialise() throws IOException;

    void storeRecords(final List<Object[]> records);

    List<Map<String, Object>> getSummary(final String clientNumber);
}
