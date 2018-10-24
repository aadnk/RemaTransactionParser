package com.comphenix.rema1000.io.excel;

import com.comphenix.rema1000.io.AbstractTableWriter;
import org.dhatim.fastexcel.Worksheet;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class ExcelTableWriter extends AbstractTableWriter {
    private final WorkbookStyle workbookStyle;
    private final Worksheet sheet;

    // Position of the header row
    private int headerOffset;
    // Position of the first data row
    private int dataOffset;

    // Index of the current row (initially -1)
    private int dataIndex = -1;

    public ExcelTableWriter(WorkbookStyle workbookStyle, Worksheet sheet) {
        this(workbookStyle, sheet, 0, 1);
    }

    public ExcelTableWriter(WorkbookStyle workbookStyle, Worksheet sheet, int headerOffset, int dataOffset) {
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

    @Override
    public void incrementRow() {
        this.dataIndex++;
    }

    @Override
    public int getDataRowCount() {
        return dataIndex + 1;
    }

    @Override
    protected void onHeaderCreated(String headerName, int headerIndex) {
        CellStyle.writeStyled(sheet, headerOffset, headerIndex, headerName, workbookStyle.getHeaderStyle());
    }

    @Override
    protected void onWriteValue(int headerIndex, Object value, Class<?> type) {
        if (dataIndex < 0) {
            throw new IllegalStateException("Must call incrementRow() first");
        }
        CellStyle style = Instant.class.equals(type) || Date.class.equals(type) ? workbookStyle.getDateStyle() : null;
        CellStyle.writeStyled(sheet, dataOffset + dataIndex, headerIndex, value, style);
    }

    @Override
    protected void onClosed() {
        // NOP
    }
}
