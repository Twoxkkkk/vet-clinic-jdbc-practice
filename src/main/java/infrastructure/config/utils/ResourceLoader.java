package infrastructure.config.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ResourceLoader {

    private final String baseDirectory;

    public ResourceLoader(String baseDirectory){

        this.baseDirectory = baseDirectory;

    }

    public String load(String fileName, String directory) throws IOException {

        ClassLoader classLoader = getClass().getClassLoader();

        String filePath;

        if (directory != null) {
            filePath = directory;
        } else {
            filePath = this.baseDirectory;
        }

        filePath += fileName;

        try (InputStream inputStream = classLoader.getResourceAsStream(filePath)) {

            if (inputStream == null) {
                throw new IllegalArgumentException("File not found at path: " + filePath);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
    }
}
