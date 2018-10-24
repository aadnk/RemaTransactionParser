package com.comphenix.rema1000.io;

import com.comphenix.rema1000.model.*;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

/**
 * Convert data to individual tables.
 */
public class DataTableConverter {
    public void writeTableTransactionsPayments(TableWriter writer, List<Transaction> transactionList) throws IOException {
        int transactionId = writer.createHeader(getHeaderName(writer, "Transaction ID"));
        int meansOfPaymentDesc = writer.createHeader(getHeaderName(writer, "Means Of Payment Desc"));
        int amount = writer.createHeader(getHeaderName(writer, "Amount"));

        for (Transaction transaction : transactionList) {
            // Skip empty receipts
            if (transaction == null || transaction.getReceiptEntries() == null || transaction.getReceiptEntries().isEmpty()) {
                continue;
            }
            for (TransactionPayment payment : transaction.getTransactionPayments()) {
                writer.incrementRow();
                writer.write(transactionId, transaction.getId());
                writer.write(meansOfPaymentDesc, payment.getMeansOfPaymentDesc(), String.class);
                writer.write(amount, payment.getAmount());
            }
        }
    }

    public void writeTableTransactionsUsedOffers(TableWriter writer, List<Transaction> transactionList) throws IOException {
        int transactionId = writer.createHeader(getHeaderName(writer, "Transaction ID"));
        int receiptEntryId = writer.createHeader(getHeaderName(writer, "Receipt Entry ID"));
        int offerCode = writer.createHeader(getHeaderName(writer, "Offer Code"));
        int offerDesc = writer.createHeader(getHeaderName(writer, "Offer Desc"));
        int discountFlat = writer.createHeader(getHeaderName(writer, "Discount Flat"));
        int discountPercent = writer.createHeader(getHeaderName(writer, "Discount Percent"));

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
                        writer.write(transactionId, transaction.getId());
                        writer.write(receiptEntryId, receiptEntry.getEntryId());
                        writer.write(offerCode, offerEntry.getOfferCode());
                        writer.write(offerDesc, offerEntry.getOfferDescription(), String.class);
                        writer.write(discountFlat, offerEntry.getDiscountFlat());
                        writer.write(discountPercent, offerEntry.getDiscountPercent());
                    }
                }
            }
        }
    }

    public void writeTableTopList(TableWriter writer, TopListMetadata metadata) throws IOException {
        int rank = writer.createHeader(getHeaderName(writer, "Rank"));
        int productId = writer.createHeader(getHeaderName(writer, "Product ID"));
        int productName = writer.createHeader(getHeaderName(writer, "Product Name"));
        int productDescription = writer.createHeader(getHeaderName(writer, "Product Description"));
        int productGroupCode = writer.createHeader(getHeaderName(writer, "Product Group Code"));
        int productGroupDesc = writer.createHeader(getHeaderName(writer, "Product Group Desc"));
        int createdTime = writer.createHeader(getHeaderName(writer, "Created Time"));
        int modifiedTime = writer.createHeader(getHeaderName(writer, "Modified Time"));
        int amountUsed = writer.createHeader(getHeaderName(writer, "Amount Used"));
        int amountSaved = writer.createHeader(getHeaderName(writer, "Amount Saved"));
        int barcode = writer.createHeader(getHeaderName(writer, "Barcode"));
        int timesBought = writer.createHeader(getHeaderName(writer, "Times Bought"));
        int itemsBought = writer.createHeader(getHeaderName(writer, "Items Bought"));
        int accountId = writer.createHeader(getHeaderName(writer, "Account ID"));
        int volume = writer.createHeader(getHeaderName(writer, "Volume"));
        
        // Sort entries by rank
        ImmutableList<ScorecardEntry> entries = ImmutableList.sortedCopyOf(
                Comparator.comparing(ScorecardEntry::getRank), metadata.getScorecard());

        for (ScorecardEntry entry : entries) {
            writer.incrementRow();
            writer.write(rank, entry.getRank());
            writer.write(productId, entry.getProductId(), String.class);
            writer.write(productName, entry.getProductName(), String.class);
            writer.write(productDescription, entry.getProductDescription(), String.class);
            writer.write(productGroupCode, entry.getProductGroupCode());
            writer.write(productGroupDesc, entry.getProductGroupDesc());
            writer.write(createdTime, Instant.ofEpochMilli(entry.getCreatedTimeUnix()));
            writer.write(modifiedTime, Instant.ofEpochMilli(entry.getModifiedTimeUnix()));
            writer.write(amountUsed, entry.getAmountUsed());
            writer.write(amountSaved, entry.getAmountSaved());
            writer.write(barcode, entry.getBarcode(), String.class);
            writer.write(timesBought, entry.getTimesBought());
            writer.write(itemsBought, entry.getItemsBought());
            writer.write(accountId, entry.getAccountId());
            writer.write(volume, entry.getVolume());
        }
    }

    public void writeTableTransactions(TableWriter writer, List<Transaction> transactionList) throws IOException {
        int transactionId = writer.createHeader(getHeaderName(writer,"Transaction ID"));
        int receiptEntryId = writer.createHeader(getHeaderName(writer, "Receipt Entry ID"));
        int purchaseDate = writer.createHeader(getHeaderName(writer, "Purchase Date"));
        int storeId = writer.createHeader(getHeaderName(writer, "Store ID"));
        int storeName = writer.createHeader(getHeaderName(writer, "Store Name"));
        int productCode = writer.createHeader(getHeaderName(writer, "Product Code"));
        int productDescription = writer.createHeader(getHeaderName(writer, "Product Description"));
        int productText1 = writer.createHeader(getHeaderName(writer, "Product Text1"));
        int productText2 = writer.createHeader(getHeaderName(writer, "Product Text2"));
        int headerName = writer.createHeader(getHeaderName(writer, "Barcode"));
        int productGroupCode = writer.createHeader(getHeaderName(writer, "Product Group Code"));
        int productGroupDesc = writer.createHeader(getHeaderName(writer, "Product Group Desc"));
        int bonusBased = writer.createHeader(getHeaderName(writer, "Bonus Based"));
        int pieces = writer.createHeader(getHeaderName(writer, "Pieces"));
        int productPrice = writer.createHeader(getHeaderName(writer, "Product Price"));
        int productDiscount = writer.createHeader(getHeaderName(writer, "Product Discount"));
        int productNetPrice = writer.createHeader(getHeaderName(writer, "Product Net Price"));
        int productDeposit = writer.createHeader(getHeaderName(writer, "Product Deposit"));
        int volumeAmount = writer.createHeader(getHeaderName(writer, "Volume Amount"));
        int volumeUnit = writer.createHeader(getHeaderName(writer, "Volume Unit"));
        int transactionAmount = writer.createHeader(getHeaderName(writer, "Transaction Amount"));
        int transactionBonusPoints = writer.createHeader(getHeaderName(writer, "Transaction Bonus Points"));
        int transactionDiscount = writer.createHeader(getHeaderName(writer, "Transaction Discount"));
        int transactionNetAmount = writer.createHeader(getHeaderName(writer, "Transaction Net Amount"));
        
        for (Transaction transaction : transactionList) {
            // Skip empty receipts
            if (transaction == null || transaction.getReceiptEntries() == null || transaction.getReceiptEntries().isEmpty()) {
                continue;
            }
            
            for (ReceiptEntry receiptEntry : transaction.getReceiptEntries()) {
                writer.incrementRow();
                writer.write(transactionId, transaction.getId());
                writer.write(receiptEntryId, receiptEntry.getEntryId());
                writer.write(purchaseDate, Instant.ofEpochSecond(transaction.getPurchaseDateUnix()));
                writer.write(storeId, transaction.getStoreId());
                writer.write(storeName, transaction.getStoreName());

                writer.write(productCode, receiptEntry.getProductCode(), String.class);
                writer.write(productDescription, receiptEntry.getProductDescription(), String.class);
                writer.write(productText1, receiptEntry.getProductText1(), String.class);
                writer.write(productText2, receiptEntry.getProductText2(), String.class);
                writer.write(headerName, receiptEntry.getBarcode(), String.class);
                writer.write(productGroupCode, receiptEntry.getProductGroupCode(), String.class);
                writer.write(productGroupDesc, receiptEntry.getProductGroupDesc(), String.class);
                writer.write(bonusBased, receiptEntry.isBonusBased());
                writer.write(pieces, receiptEntry.getPieces());

                writer.write(productPrice, receiptEntry.getPriceAmount());
                writer.write(productDiscount, receiptEntry.getPriceDiscount());
                writer.write(productNetPrice, receiptEntry.getPriceAmount() + receiptEntry.getPriceDiscount());
                writer.write(productDeposit, receiptEntry.getDeposit());

                writer.write(volumeAmount, receiptEntry.getVolumeAmount());
                writer.write(volumeUnit, receiptEntry.getVolumeUnit());

                writer.write(transactionAmount, transaction.getAmount());
                writer.write(transactionBonusPoints, transaction.getBonusPoints());
                writer.write(transactionDiscount, transaction.getDiscount());
                writer.write(transactionNetAmount, transaction.getAmount() - transaction.getDiscount());
            }
        }
    }

    /**
     * Retrieve the correct column header name.
     * @return The header name.
     */
    protected String getHeaderName(TableWriter writer, String headerName) {
        return headerName;
    }
}
