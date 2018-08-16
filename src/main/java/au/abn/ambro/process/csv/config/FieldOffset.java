package au.abn.ambro.process.csv.config;


import au.abn.ambro.process.csv.model.Offset;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Setter
@Getter
@Component
@ConfigurationProperties("ambro.processor")
public class FieldOffset {
    private LinkedHashMap<String, Offset> offsets;
}
