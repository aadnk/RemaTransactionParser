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

public class DataRoot {
    @SerializedName("TopList")
    private TopListMetadata topList;

    @SerializedName("TransactionsInfo")
    private TransactionsInfo transactionsInfo;

    public DataRoot(TopListMetadata topList, TransactionsInfo transactionsInfo) {
        this.topList = topList;
        this.transactionsInfo = transactionsInfo;
    }

    public TopListMetadata getTopList() {
        return topList;
    }

    public TransactionsInfo getTransactionsInfo() {
        return transactionsInfo;
    }

    public static DataRootBuilder newBuilder() {
        return new DataRootBuilder();
    }

    public static final class DataRootBuilder {
        private TopListMetadata topList;
        private TransactionsInfo transactionsInfo;

        private DataRootBuilder() {
        }

        public DataRootBuilder withTopList(TopListMetadata topList) {
            this.topList = topList;
            return this;
        }

        public DataRootBuilder withTransactionsInfo(TransactionsInfo transactionsInfo) {
            this.transactionsInfo = transactionsInfo;
            return this;
        }

        public DataRoot build() {
            return new DataRoot(topList, transactionsInfo);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataRoot dataRoot = (DataRoot) o;
        return Objects.equals(topList, dataRoot.topList) &&
                Objects.equals(transactionsInfo, dataRoot.transactionsInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topList, transactionsInfo);
    }

    @Override
    public String toString() {
        return "DataRoot{" +
                "topList=" + topList +
                ", transactionsInfo=" + transactionsInfo +
                '}';
    }
}


