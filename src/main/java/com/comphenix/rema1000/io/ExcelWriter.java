package com.comphenix.rema1000.io;

import com.comphenix.rema1000.beans.BeanMetadata;
import com.comphenix.rema1000.model.*;
import com.google.common.collect.ImmutableList;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

public class ExcelWriter extends DataWriter<DataRoot> {
    public static class WorkbookStyle {
        private CellStyle headerStyle;
        private CellStyle dateStyle;

        public WorkbookStyle(CellStyle headerStyle, CellStyle dateStyle) {
            this.headerStyle = headerStyle;
            this.dateStyle = dateStyle;
        }

        public CellStyle getHeaderStyle() {
            return headerStyle;
        }

        public CellStyle getDateStyle() {
            return dateStyle;
        }
    }

    @Override
    public void write(Path output, DataRoot data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            WorkbookStyle workbookStyle = createWorkbookStyle(workbook);
            writeTopList(workbookStyle, workbook.createSheet("TopList"),
                    data.getTopList());
            writeTransactions(workbookStyle, workbook.createSheet("Transactions"),
                    data.getTransactionsInfo().getTransactionList());

            try (OutputStream outputStream = Files.newOutputStream(output)) {
                workbook.write(outputStream);
            };
        }
    }

    private void writeTopList(WorkbookStyle workbookStyle, Sheet sheet, TopListMetadata metadata) {
        // Sort entries by rank
        ImmutableList<ScorecardEntry> entries = ImmutableList.sortedCopyOf(
                Comparator.comparing(ScorecardEntry::getRank), metadata.getScorecard());

        ExcelTableWriter writer = new ExcelTableWriter(workbookStyle, sheet);

        for (ScorecardEntry entry : entries) {
            writer.incrementRow();
            writer.write("Rank", entry.getRank());
            writer.write("Product ID", entry.getProductId(), String.class);
            writer.write("Product Name", entry.getProductName(), String.class);
            writer.write("Product Description", entry.getProductDescription(), String.class);
            writer.write("Product Group Code", entry.getProductGroupCode());
            writer.write("Product Group Desc", entry.getProductGroupDesc());
            writer.write("Created Time", Instant.ofEpochMilli(entry.getCreatedTimeUnix()));
            writer.write("Modified Time", Instant.ofEpochMilli(entry.getModifiedTimeUnix()));
            writer.write("Amount Used", entry.getAmountUsed());
            writer.write("Amount Saved", entry.getAmountSaved());
            writer.write("Barcode", entry.getBarcode(), String.class);
            writer.write("Times Bought", entry.getTimesBought());
            writer.write("Items Bought", entry.getItemsBought());
            writer.write("Account ID", entry.getAccountId());
            writer.write("Volume", entry.getVolume());
        }
    }

    private void writeTransactions(WorkbookStyle workbookStyle, Sheet sheet, List<Transaction> transactionList) {
        if (transactionList == null || transactionList.isEmpty()) {
            return;
        }
        ExcelTableWriter writer = new ExcelTableWriter(workbookStyle, sheet);

        for (Transaction transaction : transactionList) {
            // Skip empty receipts
            if (transaction == null || transaction.getReceiptEntries() == null || transaction.getReceiptEntries().isEmpty()) {
                continue;
            }
            for (ReceiptEntry receiptEntry : transaction.getReceiptEntries()) {
                writer.incrementRow();
                writer.write("Transaction ID", transaction.getId());
                writer.write("Purchase Date", Instant.ofEpochSecond(transaction.getPurchaseDateUnix()));
                writer.write("Store ID", transaction.getStoreId());
                writer.write("Store Name", transaction.getStoreName());

                writer.write("Product Code", receiptEntry.getProductCode(), String.class);
                writer.write("Product Description", receiptEntry.getProductDescription(), String.class);
                writer.write("Product Text1", receiptEntry.getProductText1(), String.class);
                writer.write("Product Text2", receiptEntry.getProductText2(), String.class);
                writer.write("Barcode", receiptEntry.getBarcode(), String.class);
                writer.write("Product Group Code", receiptEntry.getProductGroupCode(), String.class);
                writer.write("Product Group Desc", receiptEntry.getProductGroupDesc(), String.class);
                writer.write("Bonus Based", receiptEntry.isBonusBased());
                writer.write("Pieces", receiptEntry.getPieces());

                writer.write("Product Price", receiptEntry.getPriceAmount());
                writer.write("Product Discount", receiptEntry.getPriceDiscount());
                writer.write("Product Net Price", receiptEntry.getPriceAmount() - receiptEntry.getPriceDiscount());
                writer.write("Product Deposit", receiptEntry.getDeposit());

                writer.write("Volume Amount", receiptEntry.getVolumeAmount());
                writer.write("Volume Unit", receiptEntry.getVolumeUnit());

                writer.write("Transaction Amount", transaction.getAmount());
                writer.write("Transaction Bonus Points", transaction.getBonusPoints());
                writer.write("Transaction Discount", transaction.getDiscount());
                writer.write("Transaction Net Amount", transaction.getAmount() - transaction.getDiscount());
            }
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
