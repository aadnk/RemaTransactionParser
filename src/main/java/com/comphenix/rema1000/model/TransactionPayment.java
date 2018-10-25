/**
 *  RemaTransactionParser - Utility program for converting Rema 1000 GDRP data JSON export files
 *  Copyright (C) 2018 Kristian S. Stangeland
 *
 *  This program is free software; you can redistribute it and/or modify it under the terms of the
 *  GNU General Public License as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program;
 *  if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 *  02111-1307 USA
 */
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
