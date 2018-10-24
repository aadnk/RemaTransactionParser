package com.comphenix.rema1000.io.sql;

import com.comphenix.rema1000.io.DataTableConverter;
import com.comphenix.rema1000.io.DataWriter;
import com.comphenix.rema1000.io.TableWriter;
import com.comphenix.rema1000.io.excel.WorkbookStyle;
import com.comphenix.rema1000.model.DataRoot;
import com.comphenix.rema1000.model.Transaction;
import com.comphenix.rema1000.model.TransactionsInfo;
import com.google.common.base.CaseFormat;
import com.google.common.base.Charsets;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, Charsets.UTF_8))) {

            try (TableWriter tableWriter = new SqlTableWriter("TopList", writer)) {
                tableConverter.writeTableTopList(tableWriter, data.getTopList());
            }
            TransactionsInfo transactionsInfo = data.getTransactionsInfo();
            List<Transaction> transactionList = transactionsInfo != null ? transactionsInfo.getTransactionList() : null;

            try (TableWriter tableWriter = new SqlTableWriter("Transactions", writer)) {
                if (transactionList != null) {
                    tableConverter.writeTableTransactions(tableWriter, transactionList);
                }
            }
            try (TableWriter tableWriter = new SqlTableWriter("TransactionsPayments", writer)) {
                if (transactionList != null) {
                    tableConverter.writeTableTransactionsPayments(tableWriter, transactionList);
                }
            }
            try (TableWriter tableWriter = new SqlTableWriter("UsedOffers", writer)) {
                if (transactionList != null) {
                    tableConverter.writeTableTransactionsUsedOffers(tableWriter, transactionList);
                }
            }
            writer.flush();
        }
    }
}
