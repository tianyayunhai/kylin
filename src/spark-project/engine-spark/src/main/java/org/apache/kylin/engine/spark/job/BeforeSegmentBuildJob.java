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

package org.apache.kylin.engine.spark.job;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.kylin.engine.spark.job.step.ParamPropagation;
import org.apache.kylin.metadata.cube.model.NDataSegment;
import org.apache.spark.sql.hive.utils.ResourceDetectUtils;

import lombok.val;

public class BeforeSegmentBuildJob extends SegmentJob implements ResourceDetect {

    public static void main(String[] args) {
        BeforeSegmentBuildJob segmentBuildJob = new BeforeSegmentBuildJob();
        segmentBuildJob.execute(args);
    }

    @Override
    protected final void doExecute() throws Exception {
        writeCountDistinct();
        if (isPartitioned()) {
            detectPartition();
        } else {
            detect();
        }
    }

    @Override
    protected void waitForResourceSuccess() {
        // do nothing 
    }

    private void detectPartition() throws IOException {
        for (NDataSegment dataSegment : readOnlySegments) {
            val params = new ParamPropagation();
            val exec = new BeforePartitionBuildExec(this, dataSegment, params);
            exec.detectResource();
        }
    }

    private void detect() throws IOException {
        for (NDataSegment dataSegment : readOnlySegments) {
            val params = new ParamPropagation();
            val exec = new BeforeSegmentBuildExec(this, dataSegment, params);
            exec.detectResource();
        }
    }

    @Override
    protected String generateInfo() {
        return LogJobInfoUtils.resourceDetectBeforeCubingJobInfo();
    }

    private void writeCountDistinct() {
        ResourceDetectUtils.write(new Path(rdSharedPath, ResourceDetectUtils.countDistinctSuffix()), //
                ResourceDetectUtils.findCountDistinctMeasure(getReadOnlyLayouts()));
    }
}
