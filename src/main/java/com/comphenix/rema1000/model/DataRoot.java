package com.comphenix.rema1000.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
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


