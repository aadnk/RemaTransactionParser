package com.comphenix.rema1000.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class TopListMetadata {
    @SerializedName("Scorecard")
    private List<ScorecardEntry> scorecard;

    // Also ExchangePairs

    public TopListMetadata(List<ScorecardEntry> scorecard) {
        this.scorecard = scorecard;
    }

    public static TopListMetadataBuilder newBuilder() {
        return new TopListMetadataBuilder();
    }

    public static final class TopListMetadataBuilder {
        private List<ScorecardEntry> scorecard;

        private TopListMetadataBuilder() {
        }

        public TopListMetadataBuilder withScorecard(List<ScorecardEntry> scorecard) {
            this.scorecard = scorecard;
            return this;
        }

        public TopListMetadata build() {
            return new TopListMetadata(scorecard);
        }
    }

    public List<ScorecardEntry> getScorecard() {
        return scorecard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopListMetadata that = (TopListMetadata) o;
        return Objects.equals(scorecard, that.scorecard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scorecard);
    }

    @Override
    public String toString() {
        return "TopListMetadata{" +
                "scorecard=" + scorecard +
                '}';
    }
}
