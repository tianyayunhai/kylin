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

package org.apache.kylin.job.constant;

/**
 */
public final class ExecutableConstants {

    private ExecutableConstants() {
    }

    public static final String SPARK_PLUGINS = "spark.plugins";
    public static final String GLUTEN_PREFIX = "spark.gluten.";
    public static final String GLUTEN_PLUGIN = "org.apache.gluten.GlutenPlugin";
    public static final String COLUMNAR_SHUFFLE_MANAGER = "org.apache.spark.shuffle.sort.ColumnarShuffleManager";
    public static final String SPARK_SHUFFLE_MANAGER = "spark.shuffle.manager";

    public static final String YARN_APP_ID = "yarn_application_id";
    public static final String YARN_APP_IDS = "yarn_application_ids";
    public static final String YARN_APP_IDS_DELIMITER = ",";
    public static final String YARN_APP_URL = "yarn_application_tracking_url";
    public static final String PROXY_APP_URL = "proxy_application_tracking_url";
    public static final String SPARK_HISTORY_APP_URL = "spark_history_application_tracking_url";
    public static final String YARN_JOB_WAIT_TIME = "yarn_job_wait_time";
    public static final String YARN_JOB_RUN_TIME = "yarn_job_run_time";
    public static final String MR_JOB_ID = "mr_job_id";
    public static final String QUEUE_NAME = "queue_name";
    public static final String CORES = "cores";
    public static final String MEMORY = "memory";

    public static final String HDFS_BYTES_WRITTEN = "hdfs_bytes_written";
    public static final String SOURCE_RECORDS_COUNT = "source_records_count";
    public static final String SOURCE_RECORDS_SIZE = "source_records_size";

    public static final String STEP_NAME_BUILD_DICTIONARY = "Build Dimension Dictionary";
    public static final String STEP_NAME_CREATE_FLAT_HIVE_TABLE = "Create Intermediate Flat Hive Table";
    public static final String STEP_NAME_MATERIALIZE_HIVE_VIEW_IN_LOOKUP = "Materialize Hive View in Lookup Tables";
    public static final String STEP_NAME_FACT_DISTINCT_COLUMNS = "Extract Fact Table Distinct Columns";
    public static final String STEP_NAME_BUILD_BASE_CUBOID = "Build Base Cuboid";
    public static final String STEP_NAME_BUILD_IN_MEM_CUBE = "Build Cube In-Mem";
    public static final String STEP_NAME_BUILD_SPARK_CUBE = "Load Data To Index";
    public static final String STEP_NAME_MERGER_SPARK_SEGMENT = "Merge Segment Data";
    public static final String STEP_NAME_BUILD_SEGMENT_BUCKET = "Build Segment Bucket";
    public static final String STEP_NAME_CLEANUP = "Clean Up Old Segment";
    public static final String STEP_INTERMEDIATE_TABLE_CLEANUP = "Clean Up Intermediate Table";
    public static final String STEP_UPDATE_METADATA = "Update Metadata";
    public static final String STEP_NAME_UPDATE_CUBE_INFO = "Refresh Index Information";
    public static final String STEP_NAME_BUILD_N_D_CUBOID = "Build N-Dimension Cuboid";
    public static final String STEP_NAME_GET_CUBOID_KEY_DISTRIBUTION = "Calculate HTable Region Splits";
    public static final String STEP_NAME_CONVERT_CUBOID_TO_HFILE = "Convert Cuboid Data to HFile";
    public static final String STEP_NAME_MERGE_DICTIONARY = "Merge Cuboid Dictionary";
    public static final String STEP_NAME_MERGE_STATISTICS = "Merge Cuboid Statistics";
    public static final String STEP_NAME_SAVE_STATISTICS = "Save Cuboid Statistics";
    public static final String STEP_NAME_MERGE_CUBOID = "Merge Cuboid Data";
    public static final String STEP_NAME_HIVE_CLEANUP = "Hive Cleanup";
    public static final String STEP_NAME_KAFKA_CLEANUP = "Kafka Intermediate File Cleanup";
    public static final String STEP_NAME_GARBAGE_COLLECTION = "Garbage Collection";
    public static final String STEP_NAME_GARBAGE_COLLECTION_HDFS = "Garbage Collection on HDFS";
    public static final String STEP_NAME_REDISTRIBUTE_FLAT_HIVE_TABLE = "Redistribute Flat Hive Table";
    public static final String STEP_NAME_DATA_PROFILING = "Data Profiling";
    public static final String STEP_NAME_DETECT_RESOURCE = "Detect Resource";
    public static final String STEP_NAME_TABLE_SAMPLING = "Table Sampling";
    public static final String STEP_NAME_BUILD_SNAPSHOT = "Build Snapshot";
    public static final String STEP_NAME_OPTIMIZE_LAYOUT_DATA = "Optimize layout data";
    public static final String STEP_NAME_LOAD_INTERNAL_TABLE = "Load Internal Table";
    public static final String STEP_NAME_DROP_INTERNAL_TABLE_PARTITION = "Drop Internal Table Partition";
    public static final String STEP_NAME_INDEX_PLAN_OPT = "IndexPlan Optimize";
    public static final String STAGE_NAME_COST_BASED_PLANNER = "Cost based planner";

    public static final String STAGE_NAME_WAIT_FOR_RESOURCE = "Waiting for yarn resources";
    public static final String STAGE_NAME_REFRESH_SNAPSHOT = "Build or refresh snapshot";
    public static final String STAGE_NAME_MATERIALIZED_FACT_TABLE = "Materialize fact table view";
    public static final String STAGE_NAME_BUILD_GLOBAL_DICT = "Generate global dictionary";
    public static final String STAGE_NAME_GENERATE_FLAT_TABLE = "Generate flat table";
    public static final String STAGE_NAME_OPTIMIZE_INDEX_PLAN = "Optimize index plan";
    public static final String STAGE_NAME_GATHER_FLAT_TABLE_STATS = "Get flat table statistics";
    public static final String STAGE_NAME_BUILD_LAYER = "Build indexes by layer";
    public static final String STAGE_NAME_REFRESH_COLUMN_BYTES = "Update flat table statistics";
    public static final String STAGE_NAME_MERGE_FLAT_TABLE = "Merge flat table";
    public static final String STAGE_NAME_MERGE_INDICES = "Merge indexes";
    public static final String STAGE_NAME_MERGE_COLUMN_BYTES = "Merge flat table statistics";
    public static final String STAGE_NAME_TABLE_SAMPLING = "Sample Table Data";
    public static final String STAGE_NAME_BUILD_SNAPSHOT = "Build Snapshot";
    public static final String STAGE_NAME_DELETE_USELESS_LAYOUT_DATA = "delete useless layout data";
    public static final String STAGE_NAME_OPTIMIZE_LAYOUT_DATA_REPARTITION = "Optimize layout data by repartition";
    public static final String STAGE_NAME_OPTIMIZE_LAYOUT_DATA_ZORDER = "Optimize layout data by zorder";
    public static final String STAGE_NAME_OPTIMIZE_LAYOUT_DATA_COMPACTION = "Optimize layout data by compaction";
    public static final String STAGE_NAME_INTERNAL_TABLE_LOAD = "Load Internal Table";
    public static final String STAGE_NAME_INTERNAL_TABLE_DROP_PARTITION = "Drop Internal Table Partition";
    public static final String LOAD_GLUTEN_CACHE = "Load Gluten Cache";
}
