package au.abn.ambro.process.csv.service;

public interface CsvProcessor {
   void processCsv(final String inputFilename, final String outputFilename, final String clientNumber);
}
