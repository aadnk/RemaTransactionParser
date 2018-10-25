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
