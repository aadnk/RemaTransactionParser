package com.comphenix.rema1000.io;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractTableWriter implements TableWriter {
    // Index of the current column (initially -1)
    private int columnIndex = -1;
    private BiMap<String, Integer> headerLookup = HashBiMap.create();

    @Override
    public int getHeaderIndex(String header) {
        return headerLookup.getOrDefault(header, -1);
    }

    @Override
    public String getHeaderName(int index) {
        return headerLookup.inverse().get(index);
    }

    @Override
    public int getHeaderCount() {
        return columnIndex + 1;
    }

    @Override
    public Set<String> headers() {
        return Collections.unmodifiableSet(headerLookup.keySet());
    }

    @Override
    public int createHeader(String headerName) {
        Integer existing = headerLookup.get(headerName);

        if (existing == null) {
            int column = ++columnIndex;

            onHeaderCreated(headerName, column);
            headerLookup.put(headerName, column);
            return column;
        }
        return existing;
    }

    /**
     * Invoked when a new header is created.
     * @param headerName the new header.
     * @param headerIndex the index of the header.
     */
    protected abstract void onHeaderCreated(String headerName, int headerIndex);


    @Override
    public void write(String headerName, Object value) {
        write(headerName, value, value != null ? value.getClass() : Object.class);
    }

    @Override
    public void write(String headerName, Object value, Class<?> type) {
        Objects.requireNonNull(type,"type cannot be NULL");
        Objects.requireNonNull(headerName, "headerName cannot be NULL");

        int headerIndex = createHeader(headerName);
        onWriteValue(headerIndex, value, type);
    }

    @Override
    public void write(int headerIndex, Object value) {
        write(headerIndex, value, value != null ? value.getClass() : Object.class);
    }

    @Override
    public void write(int headerIndex, Object value, Class<?> type) {
        Objects.requireNonNull(type,"type cannot be NULL");

        if (headerIndex < 0 || headerIndex > columnIndex) {
            throw new IllegalArgumentException("Illegal header index " + headerIndex);
        }
        onWriteValue(headerIndex, value, type);
    }

    /**
     * Invoked when a value must be written to the table.
     * @param headerIndex the header index.
     * @param value the value.
     * @param type the value type.
     */
    protected abstract void onWriteValue(int headerIndex, Object value, Class<?> type);
}
