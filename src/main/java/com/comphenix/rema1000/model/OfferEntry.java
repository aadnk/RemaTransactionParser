package com.comphenix.rema1000.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class OfferEntry {
    @SerializedName("OfferCode")
    private String offerCode;
    @SerializedName("OfferDesc")
    private String offerDescription;
    @SerializedName("Discount")
    private double discountFlat;
    @SerializedName("DiscountPercent")
    private double discountPercent;

    public OfferEntry(String offerCode, String offerDescription, double discountFlat, double discountPercent) {
        this.offerCode = offerCode;
        this.offerDescription = offerDescription;
        this.discountFlat = discountFlat;
        this.discountPercent = discountPercent;
    }

    public static OfferEntryBuilder newBuilder() {
        return new OfferEntryBuilder();
    }

    public static final class OfferEntryBuilder {
        private String offerCode;
        private String offerDescription;
        private double discountFlat;
        private double discountPercent;

        private OfferEntryBuilder() {
        }

        public OfferEntryBuilder withOfferCode(String offerCode) {
            this.offerCode = offerCode;
            return this;
        }

        public OfferEntryBuilder withOfferDescription(String offerDescription) {
            this.offerDescription = offerDescription;
            return this;
        }

        public OfferEntryBuilder withDiscountFlat(double discountFlat) {
            this.discountFlat = discountFlat;
            return this;
        }

        public OfferEntryBuilder withDiscountPercent(double discountPercent) {
            this.discountPercent = discountPercent;
            return this;
        }

        public OfferEntry build() {
            return new OfferEntry(offerCode, offerDescription, discountFlat, discountPercent);
        }
    }

    public String getOfferCode() {
        return offerCode;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public double getDiscountFlat() {
        return discountFlat;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfferEntry that = (OfferEntry) o;
        return Double.compare(that.discountFlat, discountFlat) == 0 &&
                Double.compare(that.discountPercent, discountPercent) == 0 &&
                Objects.equals(offerCode, that.offerCode) &&
                Objects.equals(offerDescription, that.offerDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offerCode, offerDescription, discountFlat, discountPercent);
    }

    @Override
    public String toString() {
        return "OfferEntry{" +
                "offerCode='" + offerCode + '\'' +
                ", offerDescription='" + offerDescription + '\'' +
                ", discountFlat=" + discountFlat +
                ", discountPercent=" + discountPercent +
                '}';
    }
}
