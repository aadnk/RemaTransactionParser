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
package com.comphenix.rema1000.io;

import java.io.IOException;
import java.util.Set;

public interface TableWriter extends AutoCloseable {
    /**
     * Increment to the next row.
     * <p>
     * This must be the first call to the writer.
     */
    void incrementRow() throws IOException;

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
    int createHeader(String headerName) throws IOException;

    /**
     * Write the given value to the column with the given header.
     * @param headerName the header name.
     * @param value the value.
     */
    void write(String headerName, Object value) throws IOException;

    /**
     * Write the given value to the column with the given header.
     * @param headerName the header name.
     * @param value the value.
     * @param type the object type.
     */
    void write(String headerName, Object value, Class<?> type) throws IOException;

    /**
     * Write the given value to the column with the given header.
     * @param headerIndex the header index.
     * @param value the value.
     */
    void write(int headerIndex, Object value) throws IOException;

    /**
     * Write the given value to the column with the given header.
     * @param headerIndex the header index.
     * @param value the value.
     * @param type the object type.
     */
    void write(int headerIndex, Object value, Class<?> type) throws IOException;

    /**
     * Close the current writer.
     */
    public void close() throws IOException;
}
