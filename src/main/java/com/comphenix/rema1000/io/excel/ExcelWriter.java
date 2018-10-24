package com.comphenix.rema1000.io.excel;

import com.comphenix.rema1000.io.DataTableConverter;
import com.comphenix.rema1000.io.DataWriter;
import com.comphenix.rema1000.io.TableWriter;
import com.comphenix.rema1000.model.DataRoot;
import com.comphenix.rema1000.model.TopListMetadata;
import com.comphenix.rema1000.model.Transaction;
import com.comphenix.rema1000.model.TransactionsInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

public class ExcelWriter extends DataWriter<DataRoot> {
    public enum Format {
        XLSX,
        XLS
    }

    private Format format;
    private DataTableConverter tableConverter = new DataTableConverter();

    public ExcelWriter(Format format) {
        this.format = Objects.requireNonNull(format, "format cannot be NULL");
    }

    @Override
    public void write(OutputStream output, DataRoot data) throws IOException {
        try (Workbook workbook = createWorkbook()) {
            WorkbookStyle workbookStyle = createWorkbookStyle(workbook);

            writeTransactionsInfo(workbookStyle, workbook.createSheet("Info"), data.getTransactionsInfo());

            writeTopList(workbookStyle, workbook.createSheet("TopList"),
                    data.getTopList());
            writeTransactions(workbookStyle, workbook.createSheet("Transactions"),
                    data.getTransactionsInfo().getTransactionList());
            writeTransactionsPayments(workbookStyle, workbook.createSheet("Transactions Payments"),
                    data.getTransactionsInfo().getTransactionList());
            writeTransactionUsedOffers(workbookStyle, workbook.createSheet("Used Offers"),
                    data.getTransactionsInfo().getTransactionList());

            workbook.write(output);
        }
    }

    private Workbook createWorkbook() {
        return format == Format.XLSX ? new XSSFWorkbook() : new HSSFWorkbook();
    }

    private void writeTransactionsInfo(WorkbookStyle workbookStyle, Sheet info, TransactionsInfo transactionsInfo) {
        writeInfoLine(workbookStyle, info, 0, "Bonus Total", transactionsInfo.getBonusTotal());
        writeInfoLine(workbookStyle, info, 1, "Discount Total", transactionsInfo.getDiscountTotal());
        writeInfoLine(workbookStyle, info, 2, "Purchase Total", transactionsInfo.getPurchaseTotal());
    }

    private void writeInfoLine(WorkbookStyle style, Sheet sheet, int rowIndex, String infoName, Object infoValue) {
        Row row = sheet.createRow(rowIndex);
        Cell headerCell = row.createCell(0);
        headerCell.setCellStyle(style.getHeaderStyle());
        headerCell.setCellValue(infoName);

        ExcelCells.writeCell(style, row.createCell(1), infoValue, infoValue != null ? infoValue.getClass() : Object.class);
    }

    private void writeTransactionsPayments(WorkbookStyle workbookStyle, Sheet sheet, List<Transaction> transactionList) throws IOException {
        if (transactionList == null || transactionList.isEmpty()) {
            return;
        }
        try (TableWriter writer = new ExcelTableWriter(workbookStyle, sheet)) {
            tableConverter.writeTableTransactionsPayments(writer, transactionList);
        }
    }

    private void writeTransactionUsedOffers(WorkbookStyle workbookStyle, Sheet sheet, List<Transaction> transactionList) throws IOException {
        if (transactionList == null || transactionList.isEmpty()) {
            return;
        }
        try (TableWriter writer = new ExcelTableWriter(workbookStyle, sheet)) {
            tableConverter.writeTableTransactionsUsedOffers(writer, transactionList);
        }
    }

    private void writeTopList(WorkbookStyle workbookStyle, Sheet sheet, TopListMetadata metadata) throws IOException {
        TableWriter writer = new ExcelTableWriter(workbookStyle, sheet);
        tableConverter.writeTableTopList(writer, metadata);
    }

    private void writeTransactions(WorkbookStyle workbookStyle, Sheet sheet, List<Transaction> transactionList) throws IOException {
        if (transactionList == null || transactionList.isEmpty()) {
            return;
        }
        try (TableWriter writer = new ExcelTableWriter(workbookStyle, sheet)) {
            tableConverter.writeJoinedTableTransactions(writer, transactionList);
        }
    }

    private WorkbookStyle createWorkbookStyle(Workbook workbook) {
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat((short)0x16);

        return new WorkbookStyle(headerStyle, dateStyle);
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        return headerStyle;
    }
}
