package com.comphenix.rema1000.io;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.internal.Primitives;
import org.apache.poi.ss.usermodel.*;

import java.time.Instant;
import java.util.*;

public class ExcelTableWriter {
    private final ExcelWriter.WorkbookStyle workbookStyle;
    private final Sheet sheet;

    // Position of the header row
    private int headerOffset;
    // Position of the first data row
    private int dataOffset;

    private Row headerRow;
    private Row dataRow;

    // Index of the current row (initially -1)
    private int dataIndex = -1;
    // Index of the current column (initially -1)
    private int columnIndex = -1;

    private BiMap<String, Integer> headerLookup = HashBiMap.create();

    public ExcelTableWriter(ExcelWriter.WorkbookStyle workbookStyle, Sheet sheet) {
        this(workbookStyle, sheet, 0, 1);
    }

    public ExcelTableWriter(ExcelWriter.WorkbookStyle workbookStyle, Sheet sheet, int headerOffset, int dataOffset) {
        if (headerOffset < 0) {
            throw new IllegalArgumentException("headerOffset cannot be negative");
        }
        if (dataOffset < 0) {
            throw new IllegalArgumentException("dataOffset cannot be negative");
        }
        this.workbookStyle = Objects.requireNonNull(workbookStyle, "workbookStyle cannot be NULL");
        this.sheet = Objects.requireNonNull(sheet, "sheet cannot be NULL");
        this.headerOffset = headerOffset;
        this.dataOffset = dataOffset;
    }

    public void incrementRow() {
        this.dataRow = null;
        this.dataIndex++;
    }

    /**
     * Retrieve the index of the given header, if it exists.
     * @param header the header name.
     * @return Index of the header, or -1 if not found.
     */
    public int getHeaderIndex(String header) {
        return headerLookup.getOrDefault(header, -1);
    }

    /**
     * Retrieve the name of the header at the given index.
     * @param index the index.
     * @return The corresponding name.
     */
    public String getHeaderName(int index) {
        return headerLookup.inverse().get(index);
    }

    /**
     * Retrieve the number of headers in the table.
     * @return Number of headers.
     */
    public int getHeaderCount() {
        return columnIndex + 1;
    }

    /**
     * Retrieve a set view of the current headers in the table.
     * @return Unmodifiable view of the headers in the table.
     */
    public Set<String> headers() {
        return Collections.unmodifiableSet(headerLookup.keySet());
    }

    /**
     * Retrieve the number of data rows in the table.
     * @return Number of data rows.
     */
    public int getDataRowCount() {
        return dataIndex + 1;
    }

    /**
     * Create a new header with the given name
     * @param headerName the header name.
     * @return Index of the created or existing index.
     */
    public int createHeader(String headerName) {
        Integer existing = headerLookup.get(headerName);

        if (existing == null) {
            int column = ++columnIndex;

            Cell headerCell = getHeaderRow().createCell(column, CellType.STRING);
            headerCell.setCellStyle(workbookStyle.getHeaderStyle());
            headerCell.setCellValue(headerName);

            headerLookup.put(headerName, column);
            return column;
        }
        return existing;
    }

    /**
     * Write the given value to the column with the given header.
     * @param headerName the header name.
     * @param value the value.
     */
    public void write(String headerName, Object value) {
        write(headerName, value, value != null ? value.getClass() : Object.class);
    }

    /**
     * Write the given value to the column with the given header.
     * @param headerName the header name.
     * @param value the value.
     * @param type the object type.
     */
    public void write(String headerName, Object value, Class<?> type) {
        Objects.requireNonNull(type,"type cannot be NULL");
        Objects.requireNonNull(headerName, "headerName cannot be NULL");

        int headerIndex = createHeader(headerName);
        writeCell(headerIndex, value, type);
    }

    /**
     * Write the given value to the column with the given header.
     * @param headerIndex the header index.
     * @param value the value.
     */
    public void write(int headerIndex, Object value) {
        write(headerIndex, value, value != null ? value.getClass() : Object.class);
    }
    /**
     * Write the given value to the column with the given header.
     * @param headerIndex the header index.
     * @param value the value.
     * @param type the object type.
     */
    public void write(int headerIndex, Object value, Class<?> type) {
        Objects.requireNonNull(type,"type cannot be NULL");

        if (headerIndex < 0 || headerIndex > columnIndex) {
            throw new IllegalArgumentException("Illegal header index " + headerIndex);
        }
        writeCell(headerIndex, value, type);
    }

    private void writeCell(int headerIndex, Object value, Class<?> type) {
        ExcelCells.writeCell(workbookStyle, getDataRow().createCell(headerIndex), value, type);
    }

    private Row getHeaderRow() {
        Row result = headerRow;

        if (result == null) {
            result = headerRow = sheet.createRow(headerOffset);
        }
        return result;
    }

    private Row getDataRow() {
        Row result = dataRow;

        if (result == null) {
            if (dataIndex < 0) {
                throw new IllegalStateException("Must call incrementRow() first");
            }
            result = dataRow = sheet.createRow(dataOffset + dataIndex);
        }
        return result;
    }
}
