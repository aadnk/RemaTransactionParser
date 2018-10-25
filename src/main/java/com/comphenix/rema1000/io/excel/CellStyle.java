/*
 *  RemaTransactionParser - Utility program for converting Rema 1000 GDRP data JSON export files
 *  Copyright (C) 2018 Kristian S. Stangeland
 *
 *  This program is free software; you can redistribute it and/or modify it under the terms of the
 *  GNU General Public License as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program;
 *  if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 *  02111-1307 USA
 */
package com.comphenix.rema1000.io.excel;

import org.dhatim.fastexcel.Worksheet;

import java.sql.Date;
import java.time.Instant;

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
