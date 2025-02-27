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

package org.apache.kylin.common;

import java.io.Closeable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.kylin.common.util.RandomUtil;
import org.apache.kylin.guava30.shaded.common.collect.Lists;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds per query information and statistics.
 */
@Slf4j
public class QueryContext implements Closeable {

    public static final String PUSHDOWN_RDBMS = "RDBMS";
    public static final String PUSHDOWN_HIVE = "HIVE";
    public static final String PUSHDOWN_MOCKUP = "MOCKUP";
    public static final String PUSHDOWN_OBJECT_STORAGE = "OBJECT STORAGE";

    public static final String PUSHDOWN_GLUTEN = "GLUTEN";

    public static final long DEFAULT_NULL_SCANNED_DATA = -1L;

    private static final TransmittableThreadLocal<QueryContext> contexts //
            = new TransmittableThreadLocal<QueryContext>() {
                @Override
                protected QueryContext initialValue() {
                    return new QueryContext();
                }
            };

    @Setter
    private String queryId;
    @Getter
    @Setter
    private String project;
    private long recordMillis;
    @Getter
    @Setter
    private String pushdownEngine;
    @Getter
    @Setter
    private String engineType;
    @Getter
    @Setter
    private int shufflePartitions;
    @Getter
    @Setter
    private int shufflePartitionsReset;
    @Getter
    @Setter
    // Spark execution ID
    private String executionID = "";
    @Getter
    @Setter
    private String userSQL;
    @Getter
    @Setter
    private Integer limit = 0;
    @Getter
    @Setter
    private Integer offset = 0;

    @Getter
    @Setter
    private String[] modelPriorities = new String[0];

    @Getter
    private final QueryTrace queryTrace = new QueryTrace();

    @Getter
    @Setter
    private boolean partialMatchIndex = false;

    @Getter
    @Setter
    private boolean isForModeling;

    @Getter
    @Setter
    private boolean forceTableIndex = false;
    @Getter
    @Setter
    private Map<String, Boolean> unmatchedJoinDigest = new ConcurrentHashMap<>();

    @Getter
    @Setter
    private boolean enhancedAggPushDown;

    /**
     * For debug purpose, will show RelNode
     * when dryRun is enabled
     */
    @Getter
    @Setter
    private String lastUsedRelNode;

    /**
     * For debug purpose, when dry run mode is enabled, the query
     * will be not executed, and debug message of this query will
     * show in the Insight page.
     */
    @Getter
    private boolean dryRun = false;

    @Getter
    @Setter
    private boolean ifBigQuery = false;

    @Getter
    @Setter
    private boolean isBigQuery = false;

    @Getter
    @Setter
    private boolean outOfSegmentRange = false;

    /** Record right after record `end` */
    @Getter
    @Setter
    private long responseStartTime = 0L;

    @Getter
    @Setter
    private String firstHintStr;

    @Getter
    @Setter
    private boolean isExplainSql;

    @Getter
    @Setter
    private QueryPlan queryPlan;

    private QueryContext() {
        // use QueryContext.current() instead
        queryId = RandomUtil.randomUUIDStr();
        recordMillis = System.currentTimeMillis();
        metrics.queryStartTime = recordMillis;
        queryPlan = new QueryPlan();
    }

    public static QueryContext current() {
        return contexts.get();
    }

    public static QueryTrace currentTrace() {
        return contexts.get().getQueryTrace();
    }

    public static void set(QueryContext queryContext) {
        contexts.set(queryContext);
    }

    public static void reset() {
        contexts.remove();
    }

    public String getQueryId() {
        return queryId == null ? "" : queryId;
    }

    LinkedHashMap<String, String> queryRecord = new LinkedHashMap<>();

    public String getSchema() {
        return String.join(",", queryRecord.keySet());
    }

    public String getTimeLine() {
        return String.join(",", queryRecord.values());
    }

    public void record(String message) {
        long current = System.currentTimeMillis();
        long takeTime = current - recordMillis;
        queryRecord.put(message, Long.toString(takeTime));
        recordMillis = current;
    }

    public void setDryRun(boolean flag) {
        dryRun = flag;
    }

    @Override
    public void close() {
        reset();
    }

    // ============================================================================
    @Getter
    @Setter
    private AclInfo aclInfo;

    @Getter
    @Setter
    private List<String> columnNames;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AclInfo {

        private String username;
        private Set<String> groups;
        private boolean hasAdminPermission;

        public AclInfo(String username, Set<String> groups, boolean hasAdminPermission) {
            this.username = username;
            this.groups = groups;
            this.hasAdminPermission = hasAdminPermission;
        }
    }

    // ============================================================================
    /**
     * query metrics, spark execution related
     */
    @Getter
    @Setter
    private Metrics metrics = new Metrics();

    public static Metrics currentMetrics() {
        return current().metrics;
    }

    @Getter
    @Setter
    public static class Metrics {
        private String correctedSql;
        private String sqlPattern;
        private Throwable finalCause;
        private Throwable olapCause;
        private boolean exactlyMatch;
        private boolean isException;
        private int segCount;
        public int fileCount;
        private long queryStartTime;
        private long queryEndTime;
        private String server;
        private long resultRowCount;
        private String queryMsg;
        private long queryJobCount;
        private long queryStageCount;
        private long queryTaskCount;
        private long cpuTime;
        private int retryTimes;
        private long dataFetchTime; // see doc in SQLResponse.dataFetchTime
        private String queryExecutedPlan;

        // sourceScanXxx from Parquet
        private AtomicLong sourceScanBytes = new AtomicLong();

        private AtomicLong sourceScanRows = new AtomicLong();

        private AtomicLong accumSourceScanRows = new AtomicLong();

        public long getSourceScanBytes() {
            return sourceScanBytes.get();
        }

        public long getSourceScanRows() {
            return sourceScanRows.get();
        }

        public void setSourceScanBytes(long bytes) {
            sourceScanBytes.set(bytes);
        }

        public void setSourceScanRows(long bytes) {
            sourceScanRows.set(bytes);
        }

        public void addAccumSourceScanRows(long rows) {
            accumSourceScanRows.addAndGet(rows);
        }

        public long getAccumSourceScanRows() {
            return accumSourceScanRows.get();
        }

        public void addRetryTimes() {
            retryTimes += 1;
        }

        public void addDataFetchTime(long dataFetchTime) {
            this.dataFetchTime = Long.max(this.dataFetchTime, dataFetchTime);
        }

        // scanXxx from SparkUI
        @Getter
        @Setter
        private List<Long> scanRows;
        @Getter
        @Setter
        private List<Long> scanBytes;

        @Getter
        @Setter
        private Boolean glutenFallback;

        public long getTotalScanBytes() {
            return calValueWithDefault(scanBytes);
        }

        public long getTotalScanRows() {
            return calValueWithDefault(scanRows);
        }

        public long duration() {
            if (queryStartTime == 0) {
                return 0;
            }
            return queryEndTime == 0 ? System.currentTimeMillis() - queryStartTime : queryEndTime - queryStartTime;
        }
    }

    /**
     * @return if scanList == null return default -1, else return sum of list
     */
    public static long calValueWithDefault(List<Long> scanList) {
        if (Objects.isNull(scanList)) {
            return DEFAULT_NULL_SCANNED_DATA;
        } else {
            return scanList.stream().mapToLong(Long::longValue).sum();
        }
    }

    public static void fillEmptyResultSetMetrics() {
        QueryContext.current().getMetrics().setScanRows(Lists.newArrayList());
        QueryContext.current().getMetrics().setScanBytes(Lists.newArrayList());
    }

    // ============================================================================
    /**
     * queryTagInfo
     */
    @Getter
    @Setter
    private QueryTagInfo queryTagInfo = new QueryTagInfo();

    @Getter
    @Setter
    public static class QueryTagInfo {

        private boolean isTimeout;
        private boolean hasRuntimeAgg;
        private boolean hasLike;
        private boolean isSparderUsed;
        private boolean isTableIndex;
        private boolean isHighPriorityQuery = false;
        private boolean withoutSyntaxError;
        private boolean isAsyncQuery;
        private boolean isPushdown;
        private boolean isPartial = false;
        private boolean isStorageCacheUsed = false;
        private String storageCacheType;
        private boolean hitExceptionCache = false;
        private boolean isConstantQuery = false;
        private String fileFormat;
        private String fileEncode;
        private String fileName;
        private String separator;
        private boolean isRefused;
        private boolean includeHeader;
        private boolean isVacant;
        private boolean isQueryDetect;
        private boolean isErrInterrupted;
        private String interruptReason;
    }

    @Getter
    @Setter
    private List<NativeQueryRealization> queryRealizations = Lists.newArrayList();

    @Getter
    @Setter
    @NoArgsConstructor
    public static class QueryPlan {
        @JsonProperty("calcite_plan")
        private String calcitePlan;
        @JsonProperty("spark_plan")
        private String sparkPlan;
    }
}
