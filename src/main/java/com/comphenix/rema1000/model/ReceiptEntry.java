package com.comphenix.rema1000.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class ReceiptEntry {
    @SerializedName("ProductCode")
    private String productCode;
    @SerializedName("ProductDescription")
    private String productDescription;
    @SerializedName("Prodtxt1")
    private String productText1;
    @SerializedName("Prodtxt2")
    private String productText2;
    @SerializedName("ProdTxt3")
    private String barcode;
    @SerializedName("ProductGroupCode")
    private String productGroupCode;
    @SerializedName("ProductGroupDesc")
    private String productGroupDesc;
    @SerializedName("BonusBased")
    private boolean bonusBased;
    @SerializedName("Pieces")
    private long pieces;
    @SerializedName("Amount")
    private double priceAmount;
    @SerializedName("Discount")
    private double priceDiscount;
    @SerializedName("Volume")
    private double volumeAmount;
    @SerializedName("Unit")
    private String volumeUnit;
    @SerializedName("Deposit")
    private double deposit;
    @SerializedName("UsedOffers")
    private List<OfferEntry> usedOffers;

    public ReceiptEntry(String productCode, String productDescription, String productText1, String productText2, String barcode,
                        String productGroupCode, String productGroupDesc, boolean bonusBased, long pieces,
                        double priceAmount, double priceDiscount, double volumeAmount, String volumeUnit, double deposit, List<OfferEntry> usedOffers) {
        this.productCode = productCode;
        this.productDescription = productDescription;
        this.productText1 = productText1;
        this.productText2 = productText2;
        this.barcode = barcode;
        this.productGroupCode = productGroupCode;
        this.productGroupDesc = productGroupDesc;
        this.bonusBased = bonusBased;
        this.pieces = pieces;
        this.priceAmount = priceAmount;
        this.priceDiscount = priceDiscount;
        this.volumeAmount = volumeAmount;
        this.volumeUnit = volumeUnit;
        this.deposit = deposit;
        this.usedOffers = usedOffers;
    }

    public static ReceiptEntryBuilder newBuilder() {
        return new ReceiptEntryBuilder();
    }

    public static final class ReceiptEntryBuilder {
        private String productCode;
        private String productDescription;
        private String productText1;
        private String productText2;
        private String barcode;
        private String productGroupCode;
        private String productGroupDesc;
        private boolean bonusBased;
        private long pieces;
        private double priceAmount;
        private double priceDiscount;
        private double volumeAmount;
        private String volumeUnit;
        private double deposit;
        private List<OfferEntry> usedOffers;

        private ReceiptEntryBuilder() {
        }

        public ReceiptEntryBuilder withProductCode(String productCode) {
            this.productCode = productCode;
            return this;
        }

        public ReceiptEntryBuilder withProductDescription(String productDescription) {
            this.productDescription = productDescription;
            return this;
        }

        public ReceiptEntryBuilder withProductText1(String productText1) {
            this.productText1 = productText1;
            return this;
        }

        public ReceiptEntryBuilder withProductText2(String productText2) {
            this.productText2 = productText2;
            return this;
        }

        public ReceiptEntryBuilder withBarcode(String barcode) {
            this.barcode = barcode;
            return this;
        }

        public ReceiptEntryBuilder withProductGroupCode(String productGroupCode) {
            this.productGroupCode = productGroupCode;
            return this;
        }

        public ReceiptEntryBuilder withProductGroupDesc(String productGroupDesc) {
            this.productGroupDesc = productGroupDesc;
            return this;
        }

        public ReceiptEntryBuilder withBonusBased(boolean bonusBased) {
            this.bonusBased = bonusBased;
            return this;
        }

        public ReceiptEntryBuilder withPieces(long pieces) {
            this.pieces = pieces;
            return this;
        }

        public ReceiptEntryBuilder withPriceAmount(double priceAmount) {
            this.priceAmount = priceAmount;
            return this;
        }

        public ReceiptEntryBuilder withPriceDiscount(double priceDiscount) {
            this.priceDiscount = priceDiscount;
            return this;
        }

        public ReceiptEntryBuilder withVolumeAmount(double volumeAmount) {
            this.volumeAmount = volumeAmount;
            return this;
        }

        public ReceiptEntryBuilder withVolumeUnit(String volumeUnit) {
            this.volumeUnit = volumeUnit;
            return this;
        }

        public ReceiptEntryBuilder withDeposit(double deposit) {
            this.deposit = deposit;
            return this;
        }

        public ReceiptEntryBuilder withUsedOffers(List<OfferEntry> usedOffers) {
            this.usedOffers = usedOffers;
            return this;
        }

        public ReceiptEntry build() {
            return new ReceiptEntry(productCode, productDescription, productText1, productText2, barcode,
                    productGroupCode, productGroupDesc, bonusBased, pieces,
                    priceAmount, priceDiscount, volumeAmount, volumeUnit, deposit, usedOffers);
        }
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductText1() {
        return productText1;
    }

    public String getProductText2() {
        return productText2;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getProductGroupCode() {
        return productGroupCode;
    }

    public String getProductGroupDesc() {
        return productGroupDesc;
    }

    public boolean isBonusBased() {
        return bonusBased;
    }

    public long getPieces() {
        return pieces;
    }

    public double getPriceAmount() {
        return priceAmount;
    }

    public double getPriceDiscount() {
        return priceDiscount;
    }

    public double getVolumeAmount() {
        return volumeAmount;
    }

    public String getVolumeUnit() {
        return volumeUnit;
    }

    public double getDeposit() {
        return deposit;
    }

    public List<OfferEntry> getUsedOffers() {
        return usedOffers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptEntry that = (ReceiptEntry) o;
        return bonusBased == that.bonusBased &&
                pieces == that.pieces &&
                Double.compare(that.priceAmount, priceAmount) == 0 &&
                Double.compare(that.priceDiscount, priceDiscount) == 0 &&
                Double.compare(that.volumeAmount, volumeAmount) == 0 &&
                Double.compare(that.deposit, deposit) == 0 &&
                Objects.equals(productCode, that.productCode) &&
                Objects.equals(productDescription, that.productDescription) &&
                Objects.equals(productText1, that.productText1) &&
                Objects.equals(productText2, that.productText2) &&
                Objects.equals(barcode, that.barcode) &&
                Objects.equals(productGroupCode, that.productGroupCode) &&
                Objects.equals(productGroupDesc, that.productGroupDesc) &&
                Objects.equals(volumeUnit, that.volumeUnit) &&
                Objects.equals(usedOffers, that.usedOffers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, productDescription, productText1, productText2, barcode,
                productGroupCode, productGroupDesc, bonusBased, pieces,
                priceAmount, priceDiscount, volumeAmount, volumeUnit, deposit, usedOffers);
    }

    @Override
    public String toString() {
        return "ReceiptEntry{" +
                "productCode='" + productCode + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productText1='" + productText1 + '\'' +
                ", productText2='" + productText2 + '\'' +
                ", barcode='" + barcode + '\'' +
                ", productGroupCode='" + productGroupCode + '\'' +
                ", productGroupDesc='" + productGroupDesc + '\'' +
                ", bonusBased=" + bonusBased +
                ", pieces=" + pieces +
                ", priceAmount=" + priceAmount +
                ", priceDiscount=" + priceDiscount +
                ", volumeAmount=" + volumeAmount +
                ", volumeUnit='" + volumeUnit + '\'' +
                ", deposit=" + deposit +
                ", usedOffers=" + usedOffers +
                '}';
    }
}
