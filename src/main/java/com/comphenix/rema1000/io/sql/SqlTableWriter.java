package com.comphenix.rema1000.io.sql;

import com.comphenix.rema1000.io.AbstractTableWriter;
import com.google.gson.internal.Primitives;

import java.io.IOException;
import java.io.Writer;
import java.time.Instant;
import java.util.*;

public class SqlTableWriter extends AbstractTableWriter {
    public static class TableColumn {
        private final String columnName;
        private final Class<?> columnType;
        private final boolean primaryKey;
        private final String foreignTable;
        private final String foreignColumn;

        public TableColumn(String columnName) {
            this(columnName, null, false, null, null);
        }

        public TableColumn(String columnName, Class<?> columnType, boolean primaryKey, String foreignTable, String foreignColumn) {
            this.columnName = Objects.requireNonNull(columnName, "tableName cannot be NULL");
            this.columnType = columnType;
            this.primaryKey = primaryKey;
            this.foreignTable = foreignTable;
            this.foreignColumn = foreignColumn;
        }

        public Class<?> getColumnType() {
            return columnType;
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
            TableColumn column = (TableColumn) o;
            return primaryKey == column.primaryKey &&
                    Objects.equals(columnName, column.columnName) &&
                    Objects.equals(columnType, column.columnType) &&
                    Objects.equals(foreignTable, column.foreignTable) &&
                    Objects.equals(foreignColumn, column.foreignColumn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(columnName, columnType, primaryKey, foreignTable, foreignColumn);
        }
    }
    private final String tableName;
    private final Writer writer;

    private final String lineBreak = System.lineSeparator();

    // Current columns
    private Map<String, TableColumn> tableColumns = new HashMap<>();

    // Current values to be written to the output
    private List<Object> values = new ArrayList<>();
    private List<Class<?>> types = new ArrayList<>();

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

        if (result == null && headerIndexLookup.containsKey(headerName)) {
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
        if (columnCreationFrozen) {
            throw new IllegalArgumentException("Cannot create header " + headerName + " after the first row has been finished.");
        }
    }

    @Override
    protected void onWriteValue(int headerIndex, Object value, Class<?> type) throws IOException {
        resizeList(values, headerIndex + 1);
        resizeList(types, headerIndex + 1);
        values.set(headerIndex, value);
        types.set(headerIndex, type);
    }

    @Override
    protected void onClosed() throws IOException {
        if (dataIndex <= 0) {
            writeHeaders();
        }
        if (dataIndex == 0) {
            writeRow();
        }
        writer.write(lineBreak);
    }

    @Override
    public void incrementRow() throws IOException {
        dataIndex++;

        // See if we have any data to write
        if (dataIndex > 0) {
            if (dataIndex == 1) {
                writeHeaders();
            }
            writeRow();
            values.clear();
        }
    }

    /**
     * Determine if column creation is now frozen.
     * @return TRUE if it is, FALSE otherwise.
     */
    public boolean isColumnCreationFrozen() {
        return columnCreationFrozen;
    }

    protected void writeHeaders() throws IOException {
        columnCreationFrozen = true;

        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ");
        writeIdentifier(builder, tableName);
        builder.append(" (").append(lineBreak);

        Set<String> primaryKeys = new HashSet<>();

        for (int i = 0; i < getHeaderCount(); i++) {
            TableColumn column = getTableColumn(i);
            Class<?> columnType = column.getColumnType();

            // Fetch known column type
            if (columnType == null && i < types.size()) {
                columnType = types.get(i);
            }
            if (i > 0) {
                builder.append(",");
                builder.append(lineBreak);
            }
            builder.append("  ");
            writeIdentifier(builder, column.getColumnName());

            if (columnType != null) {
                builder.append(" ");
                writeColumnType(builder, columnType);
            }
            if (column.getForeignTable() != null && !column.getForeignTable().isEmpty()) {
                builder.append(" REFERENCES ");
                writeIdentifier(builder, column.getForeignTable());
                builder.append("(");
                writeIdentifier(builder, column.getForeignColumn());
                builder.append(")");
            }
            if (column.isPrimaryKey()) {
                primaryKeys.add(column.getColumnName());
            }
        }
        // Finally, write primary keys
        if (primaryKeys.size() > 0) {
            builder.append(",");
            builder.append(lineBreak);
            builder.append("  PRIMARY KEY(");
            boolean firstKey = true;

            for (String primaryKey : primaryKeys) {
                if (!firstKey) {
                    builder.append(", ");
                }
                writeIdentifier(builder, primaryKey);
                firstKey = false;
            }
            builder.append(")");
        }
        builder.append(lineBreak);
        builder.append(");");
        builder.append(lineBreak);
        writer.write(builder.toString());
    }

    private void writeColumnType(StringBuilder builder, Class<?> columnType) {
        Class<?> unwrapped = Primitives.unwrap(columnType);

        if (byte.class.equals(unwrapped) || short.class.equals(unwrapped) || int.class.equals(unwrapped) ||
                long.class.equals(unwrapped) || boolean.class.equals(unwrapped)) {
            builder.append("INTEGER");
        } else if (float.class.equals(unwrapped) || double.class.equals(unwrapped)) {
            builder.append("REAL");
        } else if (CharSequence.class.isAssignableFrom(columnType) || char.class.equals(columnType)) {
            builder.append("TEXT");
        } else if (byte[].class.equals(columnType)) {
            builder.append("BLOB");
        } else if (Instant.class.equals(columnType) || Date.class.equals(columnType)) {
            builder.append("DATETIME");
        } else {
            throw new IllegalArgumentException("Unknown column type " + columnType);
        }

    }

    protected void writeRow() throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ");
        writeIdentifier(builder, tableName);
        builder.append("(");

        for (int i = 0; i < values.size(); i++) {
            TableColumn column = getTableColumn(i);

            if (i > 0) {
                builder.append(", ");
            }
            writeIdentifier(builder, column.getColumnName());
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
        } else if (value instanceof CharSequence) {
            writeString(builder, (CharSequence) value);
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

    private void writeIdentifier(StringBuilder builder, String value) {
        if (value.isEmpty()) {
            return;
        }
        writeQuoted(builder, value, '"');
    }

    private void writeString(StringBuilder builder, CharSequence value) {
        writeQuoted(builder, value, '\'');
    }

    private void writeQuoted(StringBuilder builder, CharSequence value, char quote) {
        builder.append(quote);

        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);

            // Escape quote
            if (current == quote) {
                builder.append(quote).append(quote);
            } else {
                builder.append(current);
            }
        }
        builder.append(quote);
    }

    @Override
    public int getDataRowCount() {
        return dataIndex;
    }
}
