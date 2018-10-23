package com.comphenix.rema1000.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class TransactionPayment {
    @SerializedName("MeansOfPaymentDesc")
    private String meansOfPaymentDesc;

    @SerializedName("Amount")
    private double amount;

    public TransactionPayment(String meansOfPaymentDesc, double amount) {
        this.meansOfPaymentDesc = meansOfPaymentDesc;
        this.amount = amount;
    }

    public static TransactionPaymentBuilder newBuilder() {
        return new TransactionPaymentBuilder();
    }

    public static final class TransactionPaymentBuilder {
        private String meansOfPaymentDesc;
        private double amount;

        private TransactionPaymentBuilder() {
        }

        public TransactionPaymentBuilder withMeansOfPaymentDesc(String meansOfPaymentDesc) {
            this.meansOfPaymentDesc = meansOfPaymentDesc;
            return this;
        }

        public TransactionPaymentBuilder withAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public TransactionPayment build() {
            return new TransactionPayment(meansOfPaymentDesc, amount);
        }
    }

    public String getMeansOfPaymentDesc() {
        return meansOfPaymentDesc;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionPayment that = (TransactionPayment) o;
        return Double.compare(that.amount, amount) == 0 &&
                Objects.equals(meansOfPaymentDesc, that.meansOfPaymentDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meansOfPaymentDesc, amount);
    }

    @Override
    public String toString() {
        return "TransactionPayment{" +
                "meansOfPaymentDesc='" + meansOfPaymentDesc + '\'' +
                ", amount=" + amount +
                '}';
    }
}
