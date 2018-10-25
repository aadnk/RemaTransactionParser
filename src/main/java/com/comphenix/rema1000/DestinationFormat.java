package com.comphenix.rema1000;

public enum DestinationFormat {
    XLSX("xlsx"),
    XLS("xls"),
    SQL("sql");

    private final String extension;

    DestinationFormat(String extension) {
        this.extension = extension;
    }

    public static DestinationFormat fromExtension(String extension) {
        return valueOf(extension.toUpperCase());
    }

    public String getExtension() {
        return extension;
    }
}
