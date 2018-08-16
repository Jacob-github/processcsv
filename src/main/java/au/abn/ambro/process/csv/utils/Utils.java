package au.abn.ambro.process.csv.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class Utils {

    public static String readResourceFile(final Class clazz, final String filename) throws IOException {
        final ClassLoader classLoader = clazz.getClassLoader();
        final InputStream resourceStream = classLoader.getResourceAsStream(filename);
        return IOUtils.toString(resourceStream);
    }

    public static String toCommaSeparated(final Collection collection) {
        return collection.toString().replaceAll("\\[|]", "").replaceAll(", ", ",");
    }
}
