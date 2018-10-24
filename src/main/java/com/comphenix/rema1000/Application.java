package com.comphenix.rema1000;

import com.comphenix.rema1000.io.excel.ExcelWriter;
import com.comphenix.rema1000.model.DataRoot;
import com.comphenix.rema1000.model.ReceiptEntry;
import com.comphenix.rema1000.model.Transaction;
import com.google.common.io.MoreFiles;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Application {
    private static class ArgumentParser {
        private Path source;
        private Path destination;
        private DestinationFormat format = DestinationFormat.XLSX; // Default

        private boolean streamMode;
        private int pathCount;

        public void parse(String[] args) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];

                if (arg.startsWith("-")) {
                    switch (arg) {
                        case "-f":
                        case "--format":
                            format = DestinationFormat.valueOf(args[++i].toUpperCase());
                            break;
                        case "-s":
                        case "-stream":
                            // Allow no source or destination path
                            streamMode = true;
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown format " + arg);
                    }
                    continue;
                }

                // Handle paths
                switch (pathCount++) {
                    case 0:
                        source = Paths.get(arg); break;
                    case 1:
                        destination = Paths.get(arg); break;
                    default:
                        throw new IllegalArgumentException("Too many file paths passed - expected source and destination");
                }
            }

            if (!streamMode && pathCount != 2) {
                throw new IllegalArgumentException("Must supply source and destination path (or enable stream mode).");
            }
        }

        public boolean isStreamMode() {
            return streamMode;
        }

        public Path getDestination() {
            return destination;
        }

        public Path getSource() {
            return source;
        }

        public DestinationFormat getFormat() {
            return format;
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length <= 0) {
            System.out.println("RemaTransactionParser [-f format] [-s] source destination");
            return;
        }
        ArgumentParser parser = new ArgumentParser();
        parser.parse(args);

        Gson gson = new Gson();

        try (BufferedReader reader = getInput(parser)) {
            DataRoot root = parseDataRoot(gson, reader);

            try (OutputStream output = getOutput(parser)) {
                writeOutput(parser.getFormat(), output, root);
            }
        }
        if (!parser.isStreamMode()) {
            System.out.println("Data converted.");
        }
    }

    private static DataRoot parseDataRoot(Gson gson, BufferedReader reader) {
        DataRoot root = gson.fromJson(new JsonReader(reader), DataRoot.class);
        long receiptId = 0;

        //  Generate IDs
        if (root.getTransactionsInfo() != null && root.getTransactionsInfo().getTransactionList() != null) {
            for (Transaction transaction : root.getTransactionsInfo().getTransactionList()) {
                for (ReceiptEntry entry : transaction.getReceiptEntries()) {
                    entry.setEntryId(receiptId++);
                }
            }
        }
        return root;
    }

    private static BufferedReader getInput(ArgumentParser parser) throws IOException {
        return parser.getSource() != null ?
                Files.newBufferedReader(parser.getSource()) :
                new BufferedReader(new InputStreamReader(System.in));
    }

    private static OutputStream getOutput(ArgumentParser parser) throws IOException {
        String extension = parser.getFormat().getExtension();
        Path outputFile = parser.getDestination();

        if (extension != null && outputFile != null) {
            @SuppressWarnings("UnstableApiUsage")
            String fileExtension = MoreFiles.getFileExtension(outputFile);

            // Error
            if (!extension.equalsIgnoreCase(fileExtension)) {
                throw new IllegalArgumentException("Extension must be " + extension);
            }
        }
        return parser.getDestination() != null ? Files.newOutputStream(outputFile) : System.out;
    }

    private static void writeOutput(DestinationFormat format, OutputStream output, DataRoot dataRoot) throws IOException {
        switch (format) {
            case XLSX:
                new ExcelWriter(ExcelWriter.Format.XLSX).write(output, dataRoot);
                break;
            case XLS:
                new ExcelWriter(ExcelWriter.Format.XLS).write(output, dataRoot);
                break;
            default:
                throw new IllegalArgumentException("Unknown format " + format);
        }
    }
}
