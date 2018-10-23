package com.comphenix.rema1000.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class TransactionsInfo {
    @SerializedName("BonusTotal")
    private double bonusTotal;
    @SerializedName("PurchaseTotal")
    private double purchaseTotal;
    @SerializedName("DiscountTotal")
    private double discountTotal;
    @SerializedName("Transactions")
    private List<Transaction> transactionList;
    
    public TransactionsInfo(double bonusTotal, double purchaseTotal, double discountTotal, List<Transaction> transactionList) {
        this.bonusTotal = bonusTotal;
        this.purchaseTotal = purchaseTotal;
        this.discountTotal = discountTotal;
        this.transactionList = transactionList;
    }

    public static TransactionsInfoBuilder newBuilder() {
        return new TransactionsInfoBuilder();
    }

    public static final class TransactionsInfoBuilder {
        private double bonusTotal;
        private double purchaseTotal;
        private double discountTotal;
        private List<Transaction> transactionList;

        private TransactionsInfoBuilder() {
        }

        public TransactionsInfoBuilder withBonusTotal(double bonusTotal) {
            this.bonusTotal = bonusTotal;
            return this;
        }

        public TransactionsInfoBuilder withPurchaseTotal(double purchaseTotal) {
            this.purchaseTotal = purchaseTotal;
            return this;
        }

        public TransactionsInfoBuilder withDiscountTotal(double discountTotal) {
            this.discountTotal = discountTotal;
            return this;
        }

        public TransactionsInfoBuilder withTransactionList(List<Transaction> transactionList) {
            this.transactionList = transactionList;
            return this;
        }

        public TransactionsInfo build() {
            return new TransactionsInfo(bonusTotal, purchaseTotal, discountTotal, transactionList);
        }
    }

    public double getBonusTotal() {
        return bonusTotal;
    }

    public double getPurchaseTotal() {
        return purchaseTotal;
    }

    public double getDiscountTotal() {
        return discountTotal;
    }

    public List<Transaction> getTransactionList() {
        if (transactionList == null) {
            throw new UnsupportedOperationException("Not directly available in streaming mode");
        }
        return transactionList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionsInfo that = (TransactionsInfo) o;
        return Double.compare(that.bonusTotal, bonusTotal) == 0 &&
                Double.compare(that.purchaseTotal, purchaseTotal) == 0 &&
                Double.compare(that.discountTotal, discountTotal) == 0 &&
                Objects.equals(transactionList, that.transactionList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bonusTotal, purchaseTotal, discountTotal, transactionList);
    }

    @Override
    public String toString() {
        return "TransactionsInfo{" +
                "bonusTotal=" + bonusTotal +
                ", purchaseTotal=" + purchaseTotal +
                ", discountTotal=" + discountTotal +
                ", transactionList=" + transactionList +
                '}';
    }
}
