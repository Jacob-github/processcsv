package au.abn.ambro.process.csv;

import au.abn.ambro.process.csv.service.CsvProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class Application implements CommandLineRunner {

    public static final String INPUT_FILENAME = "Input.txt";
    public static final String OUTPUT_FILENAME = "Output.csv";
    @Autowired
    private CsvProcessor csvProcessor;

    static public void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        String clientNumber = "";
        String inputFilename = INPUT_FILENAME;
        String outputFilename = OUTPUT_FILENAME;
        switch (strings.length) {
            case (0):
                System.out.printf("Usage: <clientNumber> [InputFilename] [OutputFilename]");
                System.out.printf("InputFilename (Default): " + INPUT_FILENAME);
                System.out.printf("InputFilename (Default): " + OUTPUT_FILENAME);
                break;
            case (1):
                clientNumber = strings[0];
                break;
            case (2):
                clientNumber = strings[0];
                inputFilename = strings[1];
                break;
            case (3):
                clientNumber = strings[0];
                inputFilename = strings[1];
                outputFilename = strings[2];
        }

        log.info("Start processing inputFile={} outputFile={} clientNumber={}", inputFilename, outputFilename, clientNumber);
        csvProcessor.processCsv(inputFilename, outputFilename, clientNumber);
        log.info("Completed Processing {}. Output file generated: {}", inputFilename, outputFilename);
    }
}
