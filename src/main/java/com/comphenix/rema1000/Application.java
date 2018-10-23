package com.comphenix.rema1000;

import com.comphenix.rema1000.io.ExcelWriter;
import com.comphenix.rema1000.model.DataRoot;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Application {
    public static void main(String[] args) throws IOException {
        if (args.length <= 0) {
            System.out.println("RemaTransactionParser [source json] [destination format] [destination path]");
            return;
        } else if (args.length != 3) {
            throw new IllegalArgumentException("Expected arguments [source] [format] [destination]");
        }
        Path source = Paths.get(args[0]);
        DestinationFormat format = DestinationFormat.valueOf(args[1]);
        Path destination = Paths.get(args[2]);

        Gson gson = new Gson();

        try (BufferedReader reader = Files.newBufferedReader(source)) {
            DataRoot root = gson.fromJson(new JsonReader(reader), DataRoot.class);

            // Process root
            writeOutput(format, destination, root);
        }
        System.out.println("Data written to " + destination.toAbsolutePath());
    }

    private static void writeOutput(DestinationFormat format, Path output, DataRoot dataRoot) throws IOException {
        switch (format) {
            case XLSX:
                new ExcelWriter().write(output, dataRoot);
                break;
            default:
                throw new IllegalArgumentException("Unknown format " + format);
        }
    }
}
