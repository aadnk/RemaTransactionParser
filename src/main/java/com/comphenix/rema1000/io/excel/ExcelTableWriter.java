package com.comphenix.rema1000.io.excel;

import com.comphenix.rema1000.io.AbstractTableWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Objects;

public class ExcelTableWriter extends AbstractTableWriter {
    private final WorkbookStyle workbookStyle;
    private final Sheet sheet;

    // Position of the header row
    private int headerOffset;
    // Position of the first data row
    private int dataOffset;

    private Row headerRow;
    private Row dataRow;

    // Index of the current row (initially -1)
    private int dataIndex = -1;

    public ExcelTableWriter(WorkbookStyle workbookStyle, Sheet sheet) {
        this(workbookStyle, sheet, 0, 1);
    }

    public ExcelTableWriter(WorkbookStyle workbookStyle, Sheet sheet, int headerOffset, int dataOffset) {
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
        this.dataRow = null;
        this.dataIndex++;
    }

    @Override
    public int getDataRowCount() {
        return dataIndex + 1;
    }

    @Override
    protected void onHeaderCreated(String headerName, int headerIndex) {
        Cell headerCell = getHeaderRow().createCell(headerIndex, CellType.STRING);
        headerCell.setCellStyle(workbookStyle.getHeaderStyle());
        headerCell.setCellValue(headerName);
    }

    @Override
    protected void onWriteValue(int headerIndex, Object value, Class<?> type) {
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
