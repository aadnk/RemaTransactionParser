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

import com.comphenix.rema1000.io.DataTableConverter;
import com.comphenix.rema1000.io.DataWriter;
import com.comphenix.rema1000.io.TableWriter;
import com.comphenix.rema1000.model.DataRoot;
import com.comphenix.rema1000.model.TopListMetadata;
import com.comphenix.rema1000.model.Transaction;
import com.comphenix.rema1000.model.TransactionsInfo;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ExcelWriter extends DataWriter<DataRoot> {
    private DataTableConverter tableConverter = new DataTableConverter();

    @Override
    public void write(OutputStream output, DataRoot data) throws IOException {
        Workbook workbook = new Workbook(output, "RemaTransactionParser", "1.0");
        WorkbookStyle workbookStyle = createWorkbookStyle();

        TransactionsInfo transactionsInfo = data.getTransactionsInfo();

        if (transactionsInfo != null) {
            writeTransactionsInfo(workbookStyle, workbook.newWorksheet("Info"), transactionsInfo);
        }
        writeTopList(workbookStyle, workbook.newWorksheet("TopList"),
                data.getTopList());

        if (transactionsInfo == null) {
            return;
        }
        List<Transaction> transactions = transactionsInfo.getTransactionList();

        if (transactions == null || transactions.isEmpty()) {
            return;
        }
        writeTransactions(workbookStyle, workbook.newWorksheet("Transactions"),
                transactions);
        writeReceiptEntries(workbookStyle, workbook.newWorksheet("Receipt Entries"),
                transactions);
        writeTransactionsPayments(workbookStyle, workbook.newWorksheet("Transactions Payments"),
                transactions);
        writeTransactionUsedOffers(workbookStyle, workbook.newWorksheet("Used Offers"),
                transactions);

        workbook.finish();
    }

    private void writeTransactionsInfo(WorkbookStyle workbookStyle, Worksheet info, TransactionsInfo transactionsInfo) {
        writeInfoLine(workbookStyle, info, 0, "Bonus Total", transactionsInfo.getBonusTotal());
        writeInfoLine(workbookStyle, info, 1, "Discount Total", transactionsInfo.getDiscountTotal());
        writeInfoLine(workbookStyle, info, 2, "Purchase Total", transactionsInfo.getPurchaseTotal());
    }

    private void writeInfoLine(WorkbookStyle style, Worksheet sheet, int rowIndex, String infoName, Object infoValue) {
        CellStyle.writeStyled(sheet, rowIndex, 0, infoName, style.getHeaderStyle());
        CellStyle.writeStyled(sheet, rowIndex, 1, infoValue, null);
    }

    private void writeTransactionsPayments(WorkbookStyle workbookStyle, Worksheet sheet, List<Transaction> transactionList) throws IOException {
        try (TableWriter writer = new ExcelTableWriter(workbookStyle, sheet)) {
            tableConverter.writeTableTransactionsPayments(writer, transactionList);
        }
    }

    private void writeTransactionUsedOffers(WorkbookStyle workbookStyle, Worksheet sheet, List<Transaction> transactionList) throws IOException {
        try (TableWriter writer = new ExcelTableWriter(workbookStyle, sheet)) {
            tableConverter.writeTableTransactionsUsedOffers(writer, transactionList);
        }
    }

    private void writeTopList(WorkbookStyle workbookStyle, Worksheet sheet, TopListMetadata metadata) throws IOException {
        TableWriter writer = new ExcelTableWriter(workbookStyle, sheet);
        tableConverter.writeTableTopList(writer, metadata);
    }

    private void writeReceiptEntries(WorkbookStyle workbookStyle, Worksheet sheet, List<Transaction> transactionList) throws IOException {
        try (TableWriter writer = new ExcelTableWriter(workbookStyle, sheet)) {
            tableConverter.writeJoinedTableTransactions(writer, transactionList);
        }
    }

    private void writeTransactions(WorkbookStyle workbookStyle, Worksheet sheet, List<Transaction> transactionList) throws IOException {
        try (TableWriter writer = new ExcelTableWriter(workbookStyle, sheet)) {
            tableConverter.writeTableTransactions(writer, transactionList);
        }
    }

    private WorkbookStyle createWorkbookStyle() {
        return new WorkbookStyle(
            (sheet, r, c) -> sheet.style(r, c).bold().set(),
            (sheet, r, c) -> sheet.style(r, c).format("yyyy-mm-dd hh:mm:ss").set()
        );
    }
}
