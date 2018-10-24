package com.comphenix.rema1000.io.excel;

import org.dhatim.fastexcel.Worksheet;

import java.sql.Date;
import java.time.Instant;
import java.time.ZonedDateTime;

@FunctionalInterface
public interface CellStyle {
    /**
     * Apply the current excel style to the given work sheet.
     * @param worksheet the work sheet.
     * @param row the row.
     * @param column the column.
     */
    public void apply(Worksheet worksheet, int row, int column);

    /**
     * Write the given (optionally) styled value to the cell.
     * @param worksheet the worksheet.
     * @param row the row.
     * @param column the column.
     * @param value the value to write.
     * @param style the style.
     */
    public static void writeStyled(Worksheet worksheet, int row, int column, Object value, CellStyle style) {
        if (value instanceof Instant) {
            worksheet.value(row, column, Date.from((Instant) value));
        } else if (value instanceof Boolean) {
            worksheet.value(row, column, (Boolean)value ? "True" : "False");
        } else {
            worksheet.value(row, column, value);
        }

        if (style != null) {
            style.apply(worksheet, row, column);
        }
    }
}
