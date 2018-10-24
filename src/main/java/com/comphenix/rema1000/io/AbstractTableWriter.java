package com.comphenix.rema1000.io;

import java.io.IOException;
import java.util.*;

public abstract class AbstractTableWriter implements TableWriter {
    // Index of the current column (initially -1)
    private int columnIndex = -1;
    protected Map<String, Integer> headerIndexLookup = new HashMap<>();
    protected Map<Integer, String> headerNameLookup = new HashMap<>();

    protected boolean closed;

    @Override
    public int getHeaderIndex(String header) {
        return headerIndexLookup.getOrDefault(header, -1);
    }

    @Override
    public String getHeaderName(int index) {
        return headerNameLookup.get(index);
    }

    @Override
    public int getHeaderCount() {
        return columnIndex + 1;
    }

    @Override
    public Set<String> headers() {
        return Collections.unmodifiableSet(headerIndexLookup.keySet());
    }

    @Override
    public int createHeader(String headerName) throws IOException {
        checkClosed();
        Integer existing = headerIndexLookup.get(headerName);

        if (existing == null) {
            int column = ++columnIndex;

            onHeaderCreated(headerName, column);
            headerIndexLookup.put(headerName, column);
            headerNameLookup.put(column, headerName);
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

    /**
     * Determine if the current writer is closed.
     */
    protected void checkClosed() {
        if (closed) {
            throw new IllegalStateException("Writer is closed");
        }
    }

    @Override
    public void write(String headerName, Object value) throws IOException {
        write(headerName, value, value != null ? value.getClass() : Object.class);
    }

    @Override
    public void write(String headerName, Object value, Class<?> type) throws IOException {
        checkClosed();
        Objects.requireNonNull(type,"type cannot be NULL");
        Objects.requireNonNull(headerName, "headerName cannot be NULL");

        int headerIndex = createHeader(headerName);
        onWriteValue(headerIndex, value, type);
    }

    @Override
    public void write(int headerIndex, Object value) throws IOException {
        write(headerIndex, value, value != null ? value.getClass() : Object.class);
    }

    @Override
    public void write(int headerIndex, Object value, Class<?> type) throws IOException {
        checkClosed();
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
    protected abstract void onWriteValue(int headerIndex, Object value, Class<?> type) throws IOException;

    /**
     * Invoked when the writer is closed.
     */
    protected abstract void onClosed() throws IOException;

    @Override
    public void close() throws IOException {
        if (!closed) {
            closed = true;
            onClosed();
        }
    }
}
