package au.abn.ambro.process.csv.service.impl;

import au.abn.ambro.process.csv.config.FieldOffset;
import au.abn.ambro.process.csv.service.DataStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static au.abn.ambro.process.csv.utils.Utils.readResourceFile;
import static au.abn.ambro.process.csv.utils.Utils.toCommaSeparated;
import static java.lang.String.format;

@Slf4j
@Service
public class DataStoreImpl implements DataStore {
    public static final String CREATE_TABLE_DDL = "create-table.ddl";
    private JdbcTemplate jdbcTemplate;
    private String tablename;
    private String query;
    private FieldOffset fieldOffset;
    private String insertQueury;

    @Autowired
    public DataStoreImpl(final JdbcTemplate jdbcTemplate,
                         final @Value("${ambro.processor.tablename}") String tablename,
                         final @Value("${ambro.processor.query}") String query,
                         final FieldOffset fieldOffset) {

        this.tablename = tablename;
        this.jdbcTemplate = jdbcTemplate;
        this.query = query;
        this.fieldOffset = fieldOffset;
    }

    public void initialise() throws IOException {
        try {
            log.info("initialise");
            final String schema = readResourceFile(this.getClass(), CREATE_TABLE_DDL);
            log.info(schema);
            jdbcTemplate.execute("DROP TABLE " + tablename + " IF EXISTS");
            jdbcTemplate.execute(schema);
            insertQueury = createInsertString();
        } catch (IOException ex) {
            log.error("Unable to read {}", CREATE_TABLE_DDL, ex);
            throw ex;
        }
    }

    public void storeRecords(final List<Object[]> records) {
        jdbcTemplate.batchUpdate(insertQueury, records);
    }

    public List<Map<String, Object>> getSummary(final String clientNumber) {
        return jdbcTemplate.queryForList(query.replaceAll("%CLIENT_NUMBER%", clientNumber));
    }

    private String createInsertString() {
        final List<String> tmp = new ArrayList<>();
        for (int i = 0; i < fieldOffset.getOffsets().size(); i++) {
            tmp.add("?");
        }
        return format("INSERT INTO %s(%s) VALUES(%s)", tablename,
                toCommaSeparated(fieldOffset.getOffsets().keySet()), toCommaSeparated(tmp));
    }
}
