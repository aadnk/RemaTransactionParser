package com.comphenix.rema1000.io;

import com.google.gson.internal.Primitives;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.time.Instant;
import java.util.Date;

class ExcelCells {
    private ExcelCells() {
        // Sealed
    }

    /**
     * Write the given value to the given cell.
     * @param workbookStyle the workbook style.
     * @param cell the target cell.
     * @param value the value to write.
     * @param type the value type.
     */
    public static void writeCell(ExcelWriter.WorkbookStyle workbookStyle, Cell cell, Object value, Class<?> type) {
        try {
            Class<?> wrappedType = Primitives.wrap(type);

            if (value == null) {
                cell.setCellType(CellType.BLANK);
                cell.setCellValue((String)null);
                return;
            }
            // Unix time?
            if (Instant.class.isAssignableFrom(type)) {
                // Convert from milliseconds unix time
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(Date.from((Instant) value));
                cell.setCellStyle(workbookStyle.getDateStyle());

            } else if (Date.class.equals(type)) {
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue((Date) value);
                cell.setCellStyle(workbookStyle.getDateStyle());

                // Fields representable as numbers
            } else if (Number.class.isAssignableFrom(wrappedType)) {
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(((Number) value).doubleValue());

            } else if (CharSequence.class.isAssignableFrom(type)) {
                cell.setCellType(CellType.STRING);
                cell.setCellValue(value.toString());

            } else if (Boolean.class.equals(wrappedType)) {
                cell.setCellType(CellType.BOOLEAN);
                cell.setCellValue((Boolean) value);
            } else {
                throw new IllegalArgumentException("Unknown property type " + type);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Unable to write cell with value " + value + " (type " + type + ")", e);
        }
    }
}
