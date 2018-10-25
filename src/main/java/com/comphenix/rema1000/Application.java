package com.comphenix.rema1000;

import com.comphenix.rema1000.io.excel.ExcelWriter;
import com.comphenix.rema1000.io.sql.SqlWriter;
import com.comphenix.rema1000.model.DataRoot;
import com.comphenix.rema1000.model.ReceiptEntry;
import com.comphenix.rema1000.model.Transaction;
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
        private DestinationFormat format = null; // Deduce from file extension

        private boolean streamMode;
        private int pathCount;

        private boolean showHelp;

        public void parse(String[] args) {
            if (args == null || args.length == 0) {
                showHelp = true;
                return;
            }
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
                        case "-?":
                        case "-h":
                        case "--help":
                            showHelp = true;
                            return;
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
            if (streamMode && format == null && pathCount < 2) {
                throw new IllegalArgumentException("Must specify a format in stream mode with no output file");
            }
            computeFormat();
        }

        private void computeFormat() {
            if (format == null) {
                String fileExtension = getFileExtension(destination).toUpperCase();

                // Deduce format from extension
                try {
                    format = DestinationFormat.fromExtension(fileExtension);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Unable to output JSON to file extension " + fileExtension, e);
                }
            }
        }

        public boolean isShowHelp() {
            return showHelp;
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
        ArgumentParser parser = new ArgumentParser();
        parser.parse(args);

        if (parser.isShowHelp()) {
            System.out.println("RemaTransactionParser [-f format] [-s] [-h] source destination");
            System.out.println(" -f format     Specify the output format, either XLSX (Excel 2003) or SQL ");
            System.out.println("                (Database Export script for SQLite). If not specified, the");
            System.out.println("                output file extension will be used instead.");
            System.out.println(" -s            Enable stream mode, allowing the program to use standard");
            System.out.println("                output or standard input instead of the file system. Format must");
            System.out.println("                be specified if no output file is specified.");
            System.out.println(" -h            Show this help text.");
            System.out.println(" source        Path to the JSON-file with the exported Rema 1000 data.");
            System.out.println("                May be omitted in stream mode.");
            System.out.println(" destination   Path to the output XLSX- or SQL-file where the conversion");
            System.out.println("                output will be written. May be omitted in stream mode.");
            return;
        }
        Gson gson = new Gson();

        try (BufferedReader reader = getInput(parser)) {
            DataRoot root = parseDataRoot(gson, reader);

            try (OutputStream output = getOutput(parser)) {
                writeOutput(parser.getFormat(), output, root);
            }
        }
        if (!parser.isStreamMode()) {
            System.out.println("Data converted to " + parser.getFormat());
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
        Path outputFile = parser.getDestination();

        return parser.getDestination() != null ? Files.newOutputStream(outputFile) : System.out;
    }

    private static String getFileExtension(Path outputFile) {
        String fileName = outputFile.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex != -1 ? fileName.substring(dotIndex + 1) : "";
    }

    private static void writeOutput(DestinationFormat format, OutputStream output, DataRoot dataRoot) throws IOException {
        switch (format) {
            case XLSX:
                new ExcelWriter().write(output, dataRoot);
                break;
            case SQL:
                new SqlWriter().write(output, dataRoot);
                break;
            default:
                throw new IllegalArgumentException("Unknown format " + format);
        }
    }
}
