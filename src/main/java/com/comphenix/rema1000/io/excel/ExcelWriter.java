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
import java.util.Objects;

public class ExcelWriter extends DataWriter<DataRoot> {
    private DataTableConverter tableConverter = new DataTableConverter();

    @Override
    public void write(OutputStream output, DataRoot data) throws IOException {
        Workbook workbook = new Workbook(output, "RemaTransactionParser", "1.0");
        WorkbookStyle workbookStyle = createWorkbookStyle();

        writeTransactionsInfo(workbookStyle, workbook.newWorksheet("Info"), data.getTransactionsInfo());

        writeTopList(workbookStyle, workbook.newWorksheet("TopList"),
                data.getTopList());
        writeTransactions(workbookStyle, workbook.newWorksheet("Transactions"),
                data.getTransactionsInfo().getTransactionList());
        writeTransactionsPayments(workbookStyle, workbook.newWorksheet("Transactions Payments"),
                data.getTransactionsInfo().getTransactionList());
        writeTransactionUsedOffers(workbookStyle, workbook.newWorksheet("Used Offers"),
                data.getTransactionsInfo().getTransactionList());

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
        if (transactionList == null || transactionList.isEmpty()) {
            return;
        }
        try (TableWriter writer = new ExcelTableWriter(workbookStyle, sheet)) {
            tableConverter.writeTableTransactionsPayments(writer, transactionList);
        }
    }

    private void writeTransactionUsedOffers(WorkbookStyle workbookStyle, Worksheet sheet, List<Transaction> transactionList) throws IOException {
        if (transactionList == null || transactionList.isEmpty()) {
            return;
        }
        try (TableWriter writer = new ExcelTableWriter(workbookStyle, sheet)) {
            tableConverter.writeTableTransactionsUsedOffers(writer, transactionList);
        }
    }

    private void writeTopList(WorkbookStyle workbookStyle, Worksheet sheet, TopListMetadata metadata) throws IOException {
        TableWriter writer = new ExcelTableWriter(workbookStyle, sheet);
        tableConverter.writeTableTopList(writer, metadata);
    }

    private void writeTransactions(WorkbookStyle workbookStyle, Worksheet sheet, List<Transaction> transactionList) throws IOException {
        if (transactionList == null || transactionList.isEmpty()) {
            return;
        }
        try (TableWriter writer = new ExcelTableWriter(workbookStyle, sheet)) {
            tableConverter.writeJoinedTableTransactions(writer, transactionList);
        }
    }

    private WorkbookStyle createWorkbookStyle() {
        return new WorkbookStyle(
            (sheet, r, c) -> sheet.style(r, c).bold().set(),
            (sheet, r, c) -> sheet.style(r, c).format("yyyy-mm-dd hh:mm:ss").set()
        );
    }
}
