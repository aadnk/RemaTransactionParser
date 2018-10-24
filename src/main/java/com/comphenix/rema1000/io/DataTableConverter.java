package com.comphenix.rema1000.io;

import com.comphenix.rema1000.model.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Convert data to individual tables.
 */
public class DataTableConverter {
    public void writeTableTransactionsPayments(TableWriter writer, List<Transaction> transactionList) throws IOException {
        int transactionId = createHeader(writer, "Transaction ID");
        int meansOfPaymentDesc = createHeader(writer, "Means Of Payment Desc");
        int amount = createHeader(writer, "Amount");

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
        int transactionId = createHeader(writer, "Transaction ID");
        int receiptEntryId = createHeader(writer, "Receipt Entry ID");
        int offerCode = createHeader(writer, "Offer Code");
        int offerDesc = createHeader(writer, "Offer Desc");
        int discountFlat = createHeader(writer, "Discount Flat");
        int discountPercent = createHeader(writer, "Discount Percent");

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
        int rank = createHeader(writer, "Rank");
        int productId = createHeader(writer, "Product ID");
        int productName = createHeader(writer, "Product Name");
        int productDescription = createHeader(writer, "Product Description");
        int productGroupCode = createHeader(writer, "Product Group Code");
        int productGroupDesc = createHeader(writer, "Product Group Desc");
        int createdTime = createHeader(writer, "Created Time");
        int modifiedTime = createHeader(writer, "Modified Time");
        int amountUsed = createHeader(writer, "Amount Used");
        int amountSaved = createHeader(writer, "Amount Saved");
        int barcode = createHeader(writer, "Barcode");
        int timesBought = createHeader(writer, "Times Bought");
        int itemsBought = createHeader(writer, "Items Bought");
        int accountId = createHeader(writer, "Account ID");
        int volume = createHeader(writer, "Volume");
        
        // Sort entries by rank
        List<ScorecardEntry> entries = metadata.getScorecard().stream().sorted(
                Comparator.comparing(ScorecardEntry::getRank)).
                collect(Collectors.toList());

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

    public void writeJoinedTableTransactions(TableWriter writer, List<Transaction> transactionList) throws IOException {
        int transactionId = createHeader(writer, "Transaction ID");
        int receiptEntryId = createHeader(writer, "Receipt Entry ID");
        int purchaseDate = createHeader(writer, "Purchase Date");
        int storeId = createHeader(writer, "Store ID");
        int storeName = createHeader(writer, "Store Name");

        int productCode = createHeader(writer, "Product Code");
        int productDescription = createHeader(writer, "Product Description");
        int productText1 = createHeader(writer, "Product Text1");
        int productText2 = createHeader(writer, "Product Text2");
        int headerName = createHeader(writer, "Barcode");
        int productGroupCode = createHeader(writer, "Product Group Code");
        int productGroupDesc = createHeader(writer, "Product Group Desc");
        int bonusBased = createHeader(writer, "Bonus Based");
        int pieces = createHeader(writer, "Pieces");
        int productPrice = createHeader(writer, "Product Price");
        int productDiscount = createHeader(writer, "Product Discount");
        int productNetPrice = createHeader(writer, "Product Net Price");
        int productDeposit = createHeader(writer, "Product Deposit");
        int volumeAmount = createHeader(writer, "Volume Amount");
        int volumeUnit = createHeader(writer, "Volume Unit");

        int transactionAmount = createHeader(writer, "Transaction Amount");
        int transactionBonusPoints = createHeader(writer, "Transaction Bonus Points");
        int transactionDiscount = createHeader(writer, "Transaction Discount");
        int transactionNetAmount = createHeader(writer, "Transaction Net Amount");

        for (Transaction transaction : transactionList) {
            // Skip empty receipts
            if (transaction == null || transaction.getReceiptEntries() == null || transaction.getReceiptEntries().isEmpty()) {
                continue;
            }
            
            for (ReceiptEntry receiptEntry : transaction.getReceiptEntries()) {
                writer.incrementRow();
                writer.write(transactionId, transaction.getId());
                writer.write(receiptEntryId, receiptEntry.getEntryId());
                writer.write(purchaseDate, Instant.ofEpochMilli(transaction.getPurchaseDateUnix()));
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


    public void writeTableTransactions(TableWriter writer, List<Transaction> transactionList) throws IOException {
        int transactionId = createHeader(writer, "Transaction ID");
        int purchaseDate = createHeader(writer, "Purchase Date");
        int storeId = createHeader(writer, "Store ID");
        int storeName = createHeader(writer, "Store Name");
        int transactionAmount = createHeader(writer, "Transaction Amount");
        int transactionBonusPoints = createHeader(writer, "Transaction Bonus Points");
        int transactionDiscount = createHeader(writer, "Transaction Discount");
        int transactionNetAmount = createHeader(writer, "Transaction Net Amount");

        for (Transaction transaction : transactionList) {
            // Skip empty receipts
            if (transaction == null || transaction.getReceiptEntries() == null || transaction.getReceiptEntries().isEmpty()) {
                continue;
            }
            writer.incrementRow();
            writer.write(transactionId, transaction.getId());
            writer.write(purchaseDate, Instant.ofEpochMilli(transaction.getPurchaseDateUnix()));
            writer.write(storeId, transaction.getStoreId());
            writer.write(storeName, transaction.getStoreName());
            writer.write(transactionAmount, transaction.getAmount());
            writer.write(transactionBonusPoints, transaction.getBonusPoints());
            writer.write(transactionDiscount, transaction.getDiscount());
            writer.write(transactionNetAmount, transaction.getAmount() - transaction.getDiscount());
        }
    }

    public void writeTableReceipts(TableWriter writer, List<Transaction> transactionList) throws IOException {
        int receiptEntryId = createHeader(writer, "Receipt Entry ID");
        int transactionId = createHeader(writer, "Transaction ID");
        int productCode = createHeader(writer, "Product Code");
        int productDescription = createHeader(writer, "Product Description");
        int productText1 = createHeader(writer, "Product Text1");
        int productText2 = createHeader(writer, "Product Text2");
        int headerName = createHeader(writer, "Barcode");
        int productGroupCode = createHeader(writer, "Product Group Code");
        int productGroupDesc = createHeader(writer, "Product Group Desc");
        int bonusBased = createHeader(writer, "Bonus Based");
        int pieces = createHeader(writer, "Pieces");
        int productPrice = createHeader(writer, "Product Price");
        int productDiscount = createHeader(writer, "Product Discount");
        int productNetPrice = createHeader(writer, "Product Net Price");
        int productDeposit = createHeader(writer, "Product Deposit");
        int volumeAmount = createHeader(writer, "Volume Amount");
        int volumeUnit = createHeader(writer, "Volume Unit");

        for (Transaction transaction : transactionList) {
            // Skip empty receipts
            if (transaction == null || transaction.getReceiptEntries() == null || transaction.getReceiptEntries().isEmpty()) {
                continue;
            }
            for (ReceiptEntry receiptEntry : transaction.getReceiptEntries()) {
                writer.incrementRow();
                writer.write(receiptEntryId, receiptEntry.getEntryId());
                writer.write(transactionId, transaction.getId());
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
            }
        }
    }

    private int createHeader(TableWriter writer, String headerName) throws IOException {
        return writer.createHeader(getHeaderName(writer, headerName));
    }

    /**
     * Retrieve the correct column header name.
     * @return The header name.
     */
    protected String getHeaderName(TableWriter writer, String headerName) {
        return headerName;
    }
}
