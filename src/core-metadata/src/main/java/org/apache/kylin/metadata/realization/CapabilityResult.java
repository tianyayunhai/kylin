/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.kylin.metadata.realization;

import java.util.Collection;
import java.util.List;

import org.apache.kylin.guava30.shaded.common.collect.Lists;
import org.apache.kylin.metadata.cube.cuboid.IndexMatcher;
import org.apache.kylin.metadata.cube.cuboid.NLayoutCandidate;
import org.apache.kylin.metadata.model.FunctionDesc;
import org.apache.kylin.metadata.model.MeasureDesc;
import org.apache.kylin.metadata.model.TblColRef;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CapabilityResult {

    public CapabilityResult(IndexMatcher.MatchResult matchResult) {
        this.layoutUnmatchedColsSize = matchResult.getPenalty();
        this.influences = matchResult.getInfluences();
        this.capable = matchResult.isMatched();
    }

    /**
     * Is capable or not
     */
    private boolean capable = false;

    /**
     * selected capable candidate, like Lookup or layout
     */
    private IRealizationCandidate selectedCandidate;

    private IRealizationCandidate selectedStreamCandidate;

    private int layoutUnmatchedColsSize;

    /**
     * The smaller the cost, the more capable the realization
     */
    private double cost = Integer.MAX_VALUE;

    /**
     * reason of incapable
     */
    public IncapableCause incapableCause;

    /**
     * The layout has not yet been built in any segment or is currently under building.
     */
    private boolean isVacant;

    /**
     * Match partial segments.
     */
    private boolean isPartialResult;

    /**
     * Marker objects to indicate all special features
     * (dimension-as-measure, topN etc.) that have influenced the capability check.
     */
    public List<CapabilityInfluence> influences = Lists.newArrayListWithCapacity(1);

    public double getCost(boolean isStreaming) {
        return isStreaming ? selectedStreamCandidate.getCost() : selectedCandidate.getCost();
    }

    public IRealizationCandidate getSelectedCandidate() {
        return selectedCandidate == null ? NLayoutCandidate.ofEmptyCandidate() : selectedCandidate;
    }

    public IRealizationCandidate getSelectedStreamCandidate() {
        return selectedStreamCandidate == null ? NLayoutCandidate.ofEmptyCandidate() : selectedStreamCandidate;
    }

    public void setCandidate(boolean isStreaming, CapabilityResult result) {
        if (isStreaming) {
            setSelectedStreamCandidate(result.getSelectedStreamCandidate());
        } else {
            setSelectedCandidate(result.getSelectedCandidate());
        }
    }

    public void setCandidate(boolean isStreaming, IRealizationCandidate candidate) {
        if (isStreaming) {
            setSelectedStreamCandidate(candidate);
        } else {
            setSelectedCandidate(candidate);
        }
    }

    public interface CapabilityInfluence {
        /**
         * Suggest a multiplier to influence query cost
         */
        double suggestCostMultiplier();

        MeasureDesc getInvolvedMeasure();
    }

    public static class DimensionAsMeasure implements CapabilityInfluence {

        final FunctionDesc function;

        public DimensionAsMeasure(FunctionDesc function) {
            this.function = function;
        }

        @Override
        public double suggestCostMultiplier() {
            return 1;
        }

        @Override
        public MeasureDesc getInvolvedMeasure() {
            return null;
        }

        public FunctionDesc getMeasureFunction() {
            return function;
        }
    }

    public enum IncapableType {
        UNMATCHED_DIMENSION,

        LIMIT_PRECEDE_AGGR,

        TABLE_INDEX_MISSING_COLS,

        NOT_EXIST_SNAPSHOT
    }

    @Getter
    @Setter
    public static class IncapableCause {

        private IncapableType incapableType;
        private Collection<TblColRef> unmatchedDimensions;
        private Collection<FunctionDesc> unmatchedAggregations;

        public static IncapableCause unmatchedDimensions(Collection<TblColRef> unmatchedDimensions) {
            IncapableCause incapableCause = new IncapableCause();
            incapableCause.setIncapableType(IncapableType.UNMATCHED_DIMENSION);
            incapableCause.setUnmatchedDimensions(unmatchedDimensions);
            return incapableCause;
        }

        public static IncapableCause create(IncapableType incapableType) {
            IncapableCause incapableCause = new IncapableCause();
            incapableCause.setIncapableType(incapableType);
            return incapableCause;
        }
    }
}
