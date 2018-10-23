package com.comphenix.rema1000.model;

import com.comphenix.rema1000.beans.BeanMetadata;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Transaction {
    @SerializedName("Id")
    private String id;
    @SerializedName("PurchaseDate")
    private long purchaseDateUnix;
    @SerializedName("StoreId")
    private String storeId;
    @SerializedName("StoreName")
    private String storeName;
    @SerializedName("Amount")
    private double amount;
    @SerializedName("BonusPoints")
    private int bonusPoints;
    @SerializedName("Discount")
    private double discount;
    @SerializedName("TransactionPayments")
    private List<TransactionPayment> transactionPayments;
    @SerializedName("Receipt")
    private List<ReceiptEntry> receiptEntries;

    public Transaction(String id, long purchaseDateUnix, String storeId, String storeName, double amount, int bonusPoints,
                       double discount, List<TransactionPayment> transactionPayments, List<ReceiptEntry> receiptEntries) {
        this.id = id;
        this.purchaseDateUnix = purchaseDateUnix;
        this.storeId = storeId;
        this.storeName = storeName;
        this.amount = amount;
        this.bonusPoints = bonusPoints;
        this.discount = discount;
        this.transactionPayments = transactionPayments;
        this.receiptEntries = receiptEntries;
    }

    public static TransactionBuilder newBuilder() {
        return new TransactionBuilder();
    }

    public static final class TransactionBuilder {
        private String id;
        private long purchaseDateUnix;
        private String storeId;
        private String storeName;
        private double amount;
        private int bonusPoints;
        private double discount;
        private List<TransactionPayment> transactionPayments;
        private List<ReceiptEntry> receiptEntries;

        private TransactionBuilder() {
        }

        public TransactionBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public TransactionBuilder withPurchaseDateUnix(long purchaseDateUnix) {
            this.purchaseDateUnix = purchaseDateUnix;
            return this;
        }

        public TransactionBuilder withStoreId(String storeId) {
            this.storeId = storeId;
            return this;
        }

        public TransactionBuilder withStoreName(String storeName) {
            this.storeName = storeName;
            return this;
        }

        public TransactionBuilder withAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public TransactionBuilder withBonusPoints(int bonusPoints) {
            this.bonusPoints = bonusPoints;
            return this;
        }

        public TransactionBuilder withDiscount(double discount) {
            this.discount = discount;
            return this;
        }

        public TransactionBuilder withTransactionPayments(List<TransactionPayment> transactionPayments) {
            this.transactionPayments = transactionPayments;
            return this;
        }

        public TransactionBuilder withReceiptEntries(List<ReceiptEntry> receiptEntries) {
            this.receiptEntries = receiptEntries;
            return this;
        }

        public Transaction build() {
            return new Transaction(id, purchaseDateUnix, storeId, storeName, amount, bonusPoints, discount, transactionPayments, receiptEntries);
        }
    }

    public String getId() {
        return id;
    }

    @ModelField(customType = BeanMetadata.CustomType.UNIX_TIME)
    public long getPurchaseDateUnix() {
        return purchaseDateUnix;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public double getAmount() {
        return amount;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

    public double getDiscount() {
        return discount;
    }

    public List<TransactionPayment> getTransactionPayments() {
        return transactionPayments;
    }

    public List<ReceiptEntry> getReceiptEntries() {
        return receiptEntries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return purchaseDateUnix == that.purchaseDateUnix &&
                Double.compare(that.amount, amount) == 0 &&
                bonusPoints == that.bonusPoints &&
                Double.compare(that.discount, discount) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(storeId, that.storeId) &&
                Objects.equals(storeName, that.storeName) &&
                Objects.equals(transactionPayments, that.transactionPayments) &&
                Objects.equals(receiptEntries, that.receiptEntries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, purchaseDateUnix, storeId, storeName, amount, bonusPoints, discount, transactionPayments, receiptEntries);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", purchaseDateUnix=" + purchaseDateUnix +
                ", storeId='" + storeId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", amount=" + amount +
                ", bonusPoints=" + bonusPoints +
                ", discount=" + discount +
                ", transactionPayments=" + transactionPayments +
                ", receiptEntries=" + receiptEntries +
                '}';
    }
}
