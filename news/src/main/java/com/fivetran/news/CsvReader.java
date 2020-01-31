package com.fivetran.news;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class CsvReader implements AutoCloseable {
    private BufferedReader reader;

    public CsvReader(Path path) throws IOException {
        reader = Files.newBufferedReader(path);
    }

    public List<String> readRow() throws IOException {
        String result = reader.readLine();
        if (result == null) return null;

        List<String> results = Arrays.asList(reader.readLine().split("\",\""));
        if (results.size() == 0) return results;

        int firstIndex = 0;
        results.set(firstIndex, results.get(firstIndex).substring(1, results.get(firstIndex).length()));

        int lastIndex = results.size() - 1;
        results.set(lastIndex, results.get(lastIndex).substring(0, results.get(lastIndex).length() - 1));
        return results;
    }

    @Override
    public void close() throws Exception {
        reader.close();
    }
}
