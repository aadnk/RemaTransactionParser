package com.comphenix.rema1000.io;

import com.comphenix.rema1000.model.*;
import com.google.common.collect.ImmutableList;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

/**
 * Convert data to individual tables.
 */
public class DataTableConverter {
    public void writeTableTransactionsPayments(TableWriter writer, List<Transaction> transactionList) {
        for (Transaction transaction : transactionList) {
            // Skip empty receipts
            if (transaction == null || transaction.getReceiptEntries() == null || transaction.getReceiptEntries().isEmpty()) {
                continue;
            }
            for (TransactionPayment payment : transaction.getTransactionPayments()) {
                writer.incrementRow();
                writer.write("Transaction ID", transaction.getId());
                writer.write("Means Of Payment Desc", payment.getMeansOfPaymentDesc(), String.class);
                writer.write("Amount", payment.getAmount());
            }
        }
    }

    public void writeTableTransactionsUsedOffers(TableWriter writer, List<Transaction> transactionList) {
        for (Transaction transaction : transactionList) {
            // Skip empty receipts
            if (transaction == null || transaction.getReceiptEntries() == null || transaction.getReceiptEntries().isEmpty()) {
                continue;
            }
            for (ReceiptEntry receiptEntry : transaction.getReceiptEntries()) {
                List<OfferEntry> usedOffers = receiptEntry.getUsedOffers();

                if (usedOffers != null) {
                    for (OfferEntry offerEntry : usedOffers) {
                        writer.incrementRow();
                        writer.write("Transaction ID", transaction.getId());
                        writer.write("Receipt Entry ID", receiptEntry.getEntryId());
                        writer.write("Offer Code", offerEntry.getOfferCode());
                        writer.write("Offer Desc", offerEntry.getOfferDescription(), String.class);
                        writer.write("Discount Flat", offerEntry.getDiscountFlat());
                        writer.write("Discount Percent", offerEntry.getDiscountPercent());
                    }
                }
            }
        }
    }

    public void writeTableTopList(TableWriter writer, TopListMetadata metadata) {
        // Sort entries by rank
        ImmutableList<ScorecardEntry> entries = ImmutableList.sortedCopyOf(
                Comparator.comparing(ScorecardEntry::getRank), metadata.getScorecard());

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

    public void writeTableTransactions(List<Transaction> transactionList, TableWriter writer) {
        for (Transaction transaction : transactionList) {
            // Skip empty receipts
            if (transaction == null || transaction.getReceiptEntries() == null || transaction.getReceiptEntries().isEmpty()) {
                continue;
            }
            for (ReceiptEntry receiptEntry : transaction.getReceiptEntries()) {
                writer.incrementRow();
                writer.write("Transaction ID", transaction.getId());
                writer.write("Receipt Entry ID", receiptEntry.getEntryId());
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
                writer.write("Product Net Price", receiptEntry.getPriceAmount() + receiptEntry.getPriceDiscount());
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

}
