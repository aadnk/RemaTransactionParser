package com.comphenix.rema1000.model;

import com.comphenix.rema1000.beans.BeanMetadata;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class ScorecardEntry {
    @SerializedName("Rank")
    private int rank;

    @SerializedName("ProductId")
    private String productId;
    @SerializedName("ProductName")
    private String productName;
    @SerializedName("ProductDescription")
    private String productDescription;

    @SerializedName("Ctime")
    private long createdTimeUnix;
    @SerializedName("Mtime")
    private long modifiedTimeUnix;

    @SerializedName("AmountUsed")
    private double amountUsed;
    @SerializedName("AmountSaved")
    private double amountSaved;

    @SerializedName("ProdTxt3")
    private String barcode;
    @SerializedName("TimesBought")
    private int timesBought;
    @SerializedName("ItemsBought")
    private int itemsBought;
    @SerializedName("AccountId")
    private String accountId;
    @SerializedName("ProductGroupCode")
    private String productGroupCode;
    @SerializedName("ProductGroupDesc")
    private String productGroupDesc;
    @SerializedName("Volume")
    private double volume;

    public ScorecardEntry(String productId, String productName, String productDescription, int rank,
                          long createdTimeUnix, long modifiedTimeUnix, double amountUsed, double amountSaved,
                          String barcode, int timesBought, int itemsBought, String accountId, String productGroupCode,
                          String productGroupDesc, double volume) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.rank = rank;
        this.createdTimeUnix = createdTimeUnix;
        this.modifiedTimeUnix = modifiedTimeUnix;
        this.amountUsed = amountUsed;
        this.amountSaved = amountSaved;
        this.barcode = barcode;
        this.timesBought = timesBought;
        this.itemsBought = itemsBought;
        this.accountId = accountId;
        this.productGroupCode = productGroupCode;
        this.productGroupDesc = productGroupDesc;
        this.volume = volume;
    }

    public static ScorecardEntryBuilder newBuilder() {
        return new ScorecardEntryBuilder();
    }

    public static final class ScorecardEntryBuilder {
        private String productId;
        private String productName;
        private String productDescription;
        private int rank;
        private long createdTimeUnix;
        private long modifiedTimeUnix;
        private double amountUsed;
        private double amountSaved;
        private String barcode;
        private int timesBought;
        private int itemsBought;
        private String accountId;
        private String productGroupCode;
        private String productGroupDesc;
        private double volume;

        private ScorecardEntryBuilder() {
        }

        public ScorecardEntryBuilder withProductId(String productId) {
            this.productId = productId;
            return this;
        }

        public ScorecardEntryBuilder withProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public ScorecardEntryBuilder withProductDescription(String productDescription) {
            this.productDescription = productDescription;
            return this;
        }

        public ScorecardEntryBuilder withRank(int rank) {
            this.rank = rank;
            return this;
        }

        public ScorecardEntryBuilder withCreatedTimeUnix(long createdTimeUnix) {
            this.createdTimeUnix = createdTimeUnix;
            return this;
        }

        public ScorecardEntryBuilder withModifiedTimeUnix(long modifiedTimeUnix) {
            this.modifiedTimeUnix = modifiedTimeUnix;
            return this;
        }

        public ScorecardEntryBuilder withAmountUsed(double amountUsed) {
            this.amountUsed = amountUsed;
            return this;
        }

        public ScorecardEntryBuilder withAmountSaved(double amountSaved) {
            this.amountSaved = amountSaved;
            return this;
        }

        public ScorecardEntryBuilder withBarcode(String barcode) {
            this.barcode = barcode;
            return this;
        }

        public ScorecardEntryBuilder withTimesBought(int timesBought) {
            this.timesBought = timesBought;
            return this;
        }

        public ScorecardEntryBuilder withItemsBought(int itemsBought) {
            this.itemsBought = itemsBought;
            return this;
        }

        public ScorecardEntryBuilder withAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public ScorecardEntryBuilder withProductGroupCode(String productGroupCode) {
            this.productGroupCode = productGroupCode;
            return this;
        }

        public ScorecardEntryBuilder withProductGroupDesc(String productGroupDesc) {
            this.productGroupDesc = productGroupDesc;
            return this;
        }

        public ScorecardEntryBuilder withVolume(double volume) {
            this.volume = volume;
            return this;
        }

        public ScorecardEntry build() {
            return new ScorecardEntry(
                    productId, productName, productDescription, rank, createdTimeUnix, modifiedTimeUnix, amountUsed,
                    amountSaved, barcode, timesBought, itemsBought, accountId, productGroupCode, productGroupDesc, volume);
        }
    }

    @ModelField(order = 0)
    public int getRank() {
        return rank;
    }

    @ModelField(order = 1)
    public String getProductId() {
        return productId;
    }

    @ModelField(order = 2)
    public String getProductName() {
        return productName;
    }

    @ModelField(order = 3)
    public String getProductDescription() {
        return productDescription;
    }

    @ModelField(order = 4, customType = BeanMetadata.CustomType.UNIX_TIME)
    public long getCreatedTimeUnix() {
        return createdTimeUnix;
    }

    @ModelField(order = 5, customType = BeanMetadata.CustomType.UNIX_TIME)
    public long getModifiedTimeUnix() {
        return modifiedTimeUnix;
    }

    @ModelField(order = 6)
    public double getAmountUsed() {
        return amountUsed;
    }

    @ModelField(order = 7)
    public double getAmountSaved() {
        return amountSaved;
    }

    @ModelField(order = 8)
    public String getBarcode() {
        return barcode;
    }

    @ModelField(order = 9)
    public int getTimesBought() {
        return timesBought;
    }

    @ModelField(order = 10)
    public int getItemsBought() {
        return itemsBought;
    }

    @ModelField(order = 11)
    public String getAccountId() {
        return accountId;
    }

    @ModelField(order = 12)
    public String getProductGroupCode() {
        return productGroupCode;
    }

    @ModelField(order = 13)
    public String getProductGroupDesc() {
        return productGroupDesc;
    }

    @ModelField(order = 14)
    public double getVolume() {
        return volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScorecardEntry that = (ScorecardEntry) o;
        return rank == that.rank &&
                createdTimeUnix == that.createdTimeUnix &&
                modifiedTimeUnix == that.modifiedTimeUnix &&
                Double.compare(that.amountUsed, amountUsed) == 0 &&
                Double.compare(that.amountSaved, amountSaved) == 0 &&
                timesBought == that.timesBought &&
                itemsBought == that.itemsBought &&
                Double.compare(that.volume, volume) == 0 &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(productName, that.productName) &&
                Objects.equals(productDescription, that.productDescription) &&
                Objects.equals(barcode, that.barcode) &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(productGroupCode, that.productGroupCode) &&
                Objects.equals(productGroupDesc, that.productGroupDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, productDescription, rank, createdTimeUnix, modifiedTimeUnix, amountUsed,
                amountSaved, barcode, timesBought, itemsBought, accountId, productGroupCode, productGroupDesc, volume);
    }

    @Override
    public String toString() {
        return "ScorecardEntry{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", rank=" + rank +
                ", createdTimeUnix=" + createdTimeUnix +
                ", modifiedTimeUnix=" + modifiedTimeUnix +
                ", amountUsed=" + amountUsed +
                ", amountSaved=" + amountSaved +
                ", barcode='" + barcode + '\'' +
                ", timesBought=" + timesBought +
                ", itemsBought=" + itemsBought +
                ", accountId='" + accountId + '\'' +
                ", productGroupCode='" + productGroupCode + '\'' +
                ", productGroupDesc='" + productGroupDesc + '\'' +
                ", volume=" + volume +
                '}';
    }
}
