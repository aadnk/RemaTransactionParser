package com.comphenix.rema1000.io;

import java.util.Set;

public interface TableWriter {
    /**
     * Increment to the next row.
     * <p>
     * This must be the first call to the writer.
     */
    void incrementRow();

    /**
     * Retrieve the index of the given header, if it exists.
     * @param header the header name.
     * @return Index of the header, or -1 if not found.
     */
    int getHeaderIndex(String header);

    /**
     * Retrieve the name of the header at the given index.
     * @param index the index.
     * @return The corresponding name.
     */
    String getHeaderName(int index);

    /**
     * Retrieve the number of headers in the table.
     * @return Number of headers.
     */
    int getHeaderCount();

    /**
     * Retrieve a set view of the current headers in the table.
     * @return Unmodifiable view of the headers in the table.
     */
    Set<String> headers();

    /**
     * Retrieve the number of data rows in the table.
     * @return Number of data rows.
     */
    int getDataRowCount();

    /**
     * Create a new header with the given name
     * @param headerName the header name.
     * @return Index of the created or existing index.
     */
    int createHeader(String headerName);

    /**
     * Write the given value to the column with the given header.
     * @param headerName the header name.
     * @param value the value.
     */
    void write(String headerName, Object value);

    /**
     * Write the given value to the column with the given header.
     * @param headerName the header name.
     * @param value the value.
     * @param type the object type.
     */
    void write(String headerName, Object value, Class<?> type);

    /**
     * Write the given value to the column with the given header.
     * @param headerIndex the header index.
     * @param value the value.
     */
    void write(int headerIndex, Object value);

    /**
     * Write the given value to the column with the given header.
     * @param headerIndex the header index.
     * @param value the value.
     * @param type the object type.
     */
    void write(int headerIndex, Object value, Class<?> type);
}
