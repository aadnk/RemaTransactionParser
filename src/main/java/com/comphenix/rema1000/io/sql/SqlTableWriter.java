package com.comphenix.rema1000.io.sql;

import com.comphenix.rema1000.io.AbstractTableWriter;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.io.Writer;
import java.time.Instant;
import java.util.*;

public class SqlTableWriter extends AbstractTableWriter {
    public static class TableColumn {
        private final String columnName;
        private final boolean primaryKey;
        private final String foreignTable;
        private final String foreignColumn;

        public TableColumn(String tableName) {
            this(tableName, false, null, null);
        }

        public TableColumn(String columnName, boolean primaryKey, String foreignTable, String foreignColumn) {
            this.columnName = Objects.requireNonNull(columnName, "tableName cannot be NULL");
            this.primaryKey = primaryKey;
            this.foreignTable = foreignTable;
            this.foreignColumn = foreignColumn;
        }

        public String getColumnName() {
            return columnName;
        }

        public boolean isPrimaryKey() {
            return primaryKey;
        }

        public String getForeignTable() {
            return foreignTable;
        }

        public String getForeignColumn() {
            return foreignColumn;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TableColumn that = (TableColumn) o;
            return primaryKey == that.primaryKey &&
                    Objects.equals(columnName, that.columnName) &&
                    Objects.equals(foreignTable, that.foreignTable) &&
                    Objects.equals(foreignColumn, that.foreignColumn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(columnName, primaryKey, foreignTable, foreignColumn);
        }
    }
    private final String tableName;
    private final Writer writer;

    private final String lineBreak = System.lineSeparator();

    // Current columns
    private Map<String, TableColumn> tableColumns = Maps.newHashMap();

    // Current values to be written to the output
    private List<Object> values = new ArrayList<>();

    // Index of the current row (initially -1)
    private int dataIndex = -1;

    private boolean columnCreationFrozen = false;

    public SqlTableWriter(String tableName, Writer writer) {
        this.tableName = Objects.requireNonNull(tableName, "tableName cannot be NULL");
        this.writer = Objects.requireNonNull(writer, "writer cannot be NULL");
    }

    /**
     * Associate the given header with the given table metadata, even if it does not yet exist.
     * @param headerName the header name.
     * @param column the column metadata.
     * @return This writer, for chaining.
     */
    public SqlTableWriter putTableColumn(String headerName, TableColumn column) {
        tableColumns.put(headerName, column);
        return this;
    }

    /**
     * Retrieve the table column associated with the given header.
     * @param headerIndex the header index.
     * @return Corresponding table column, or NULL if the header does not exists.
     */
    public TableColumn getTableColumn(int headerIndex) {
        String headerName = getHeaderName(headerIndex);
        return headerName != null ? getTableColumn(headerName) : null;
    }

    /**
     * Retrieve the table column associated with the given header.
     * @param headerName the header name.
     * @return Corresponding table column, or NULL if the header does not exists.
     */
    public TableColumn getTableColumn(String headerName) {
        TableColumn result = tableColumns.get(headerName);

        if (result == null && headerLookup.containsKey(headerName)) {
            // Default column
            result = new TableColumn(headerName);
        }
        return result;
    }

    private void resizeList(List<?> list, int newSize) {
        while (list.size() < newSize) {
            list.add(null);
        }
    }

    @Override
    protected void onHeaderCreated(String headerName, int headerIndex) {
        // Do nothing until we write the headers
    }

    @Override
    protected void onWriteValue(int headerIndex, Object value, Class<?> type) throws IOException {
        resizeList(values, headerIndex + 1);
        values.set(headerIndex, value);
    }

    @Override
    protected void onClosed() throws IOException {
        if (dataIndex <= 0) {
            writeHeaders();
        }
        if (dataIndex == 0) {
            writeRow();
        }
    }

    @Override
    public void incrementRow() throws IOException {
        dataIndex++;

        // See if we have any data to write
        if (dataIndex > 0) {
            writeRow();
            values.clear();
        }
    }

    protected void writeHeaders() {
        // TODO
    }

    protected void writeRow() throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ");
        builder.append(tableName);
        builder.append("(");

        for (int i = 0; i < values.size(); i++) {
            TableColumn column = getTableColumn(i);

            if (i > 0) {
                builder.append(", ");
            }
            builder.append(column.getColumnName());
        }
        builder.append(") VALUES (");

        for (int i = 0; i < values.size(); i++) {
            Object value = values.get(i);

            if (i > 0) {
                builder.append(", ");
            }
            writeValue(builder, value);
        }
        builder.append(");");
        builder.append(lineBreak);

        writer.write(builder.toString());
    }

    private void writeValue(StringBuilder builder, Object value) {
        if (value instanceof Instant) {
            // Store as unix epoch
            builder.append(((Instant) value).toEpochMilli());
        } else if (value instanceof Date) {
            builder.append(((Date) value).getTime());
        } else if (value instanceof String) {
            builder.append("\"");
            builder.append(value);
            builder.append("\"");
        } else if (value instanceof Number) {
            builder.append(value.toString());
        } else if (value instanceof Boolean) {
            builder.append((boolean)value ? 1 : 0);
        } else if (value == null) {
            builder.append("null");
        } else {
            throw new IllegalArgumentException("Unknown type of value " + value);
        }
    }

    @Override
    public int getDataRowCount() {
        return dataIndex;
    }
}
