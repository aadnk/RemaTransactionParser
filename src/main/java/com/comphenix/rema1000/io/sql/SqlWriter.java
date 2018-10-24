package com.comphenix.rema1000.io.sql;

import com.comphenix.rema1000.io.DataTableConverter;
import com.comphenix.rema1000.io.DataWriter;
import com.comphenix.rema1000.io.TableWriter;
import com.comphenix.rema1000.model.DataRoot;
import com.comphenix.rema1000.model.Transaction;
import com.comphenix.rema1000.model.TransactionsInfo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SqlWriter extends DataWriter<DataRoot> {
    private DataTableConverter tableConverter = new DataTableConverter() {
        @Override
        protected String getHeaderName(TableWriter writer, String headerName) {
            // Format header name
            return headerName.replace(" ", "_").toLowerCase();
        }
    };

    @Override
    public void write(OutputStream output, DataRoot data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8))) {

            try (SqlTableWriter tableWriter = new SqlTableWriter("TopList", writer)) {
                tableWriter.putTableColumn("rank", new SqlTableWriter.TableColumn(
                        "rank", int.class, true, null, null));
                tableConverter.writeTableTopList(tableWriter, data.getTopList());
            }
            TransactionsInfo transactionsInfo = data.getTransactionsInfo();
            List<Transaction> transactionList = transactionsInfo != null ? transactionsInfo.getTransactionList() : null;

            try (SqlTableWriter tableWriter = new SqlTableWriter("Transactions", writer)) {
                tableWriter.putTableColumn("transaction_id", new SqlTableWriter.TableColumn(
                        "transaction_id", String.class, true, null, null));

                if (transactionList != null) {
                    tableConverter.writeTableTransactions(tableWriter, transactionList);
                }
            }
            try (SqlTableWriter tableWriter = new SqlTableWriter("Receipts", writer)) {
                tableWriter.putTableColumn("receipt_entry_id", new SqlTableWriter.TableColumn(
                        "receipt_entry_id", int.class, true, null, null));
                tableWriter.putTableColumn("transaction_id", new SqlTableWriter.TableColumn(
                        "transaction_id", String.class, false, "Transactions", "transaction_id"));

                if (transactionList != null) {
                    tableConverter.writeTableReceipts(tableWriter, transactionList);
                }
            }
            try (SqlTableWriter tableWriter = new SqlTableWriter("TransactionsPayments", writer)) {
                tableWriter.putTableColumn("transaction_id", new SqlTableWriter.TableColumn(
                        "transaction_id", String.class, true, "Transactions", "transaction_id"));
                tableWriter.putTableColumn("means_of_payment_desc", new SqlTableWriter.TableColumn(
                        "means_of_payment_desc", String.class, true, null, null));

                if (transactionList != null) {
                    tableConverter.writeTableTransactionsPayments(tableWriter, transactionList);
                }
            }
            try (SqlTableWriter tableWriter = new SqlTableWriter("UsedOffers", writer)) {
                tableWriter.putTableColumn("transaction_id", new SqlTableWriter.TableColumn(
                        "transaction_id", String.class, true, "Transactions", "transaction_id"));
                tableWriter.putTableColumn("receipt_entry_id", new SqlTableWriter.TableColumn(
                        "receipt_entry_id", int.class, true, null, null));
                tableWriter.putTableColumn("offer_code", new SqlTableWriter.TableColumn(
                        "offer_code", String.class, true, null, null));

                if (transactionList != null) {
                    tableConverter.writeTableTransactionsUsedOffers(tableWriter, transactionList);
                }
            }
            writer.flush();
        }
    }
}
