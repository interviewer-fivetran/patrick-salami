package com.fivetran.news;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvWriter implements AutoCloseable {
    private BufferedWriter writer;

    public CsvWriter(Path path) throws IOException {
        String pathAsString = path.toString();
        int filePathIndex = pathAsString.length() - 4;
        int lastIndex = pathAsString.length();

        if (!pathAsString.substring(filePathIndex, lastIndex).equals(".csv")) {
            path = Paths.get(pathAsString + ".csv");
        }
        writer = Files.newBufferedWriter(path);
    }

    public void writeRow(List<String> row) throws IOException {
        writeRowInternal(row);
    }

    public void writeRow(String[] row) throws IOException {
        writeRowInternal(Arrays.asList(row));
    }

    private void writeRowInternal(List<String> row) throws IOException {
        List<String> wrapped = row.stream()
                .map(item -> "\"" + item + "\"")
                .collect(Collectors.toList());
        writer.write(String.join(",", wrapped));
        writer.newLine();
    }

    public static List<Path> csvsInDirectory(Path directory) throws IOException {
        try (Stream<Path> paths = Files.walk(directory)) {
            return paths
                    .filter(path -> path.endsWith(".csv"))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void close() throws Exception {
        writer.close();
    }
}
