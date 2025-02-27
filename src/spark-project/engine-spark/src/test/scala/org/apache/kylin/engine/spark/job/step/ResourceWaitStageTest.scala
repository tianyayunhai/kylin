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

package org.apache.kylin.engine.spark.job.step

import org.apache.kylin.common.KylinConfig
import org.apache.kylin.engine.spark.application.SparkApplication
import org.apache.kylin.metadata.streaming.ReflectionUtils
import org.apache.spark.SparkConf
import org.junit.Assert
import org.mockito.Mockito
import org.scalatest.funsuite.AnyFunSuite

class ResourceWaitStageTest extends AnyFunSuite {

  test("test ResourceWaitStage getStageName") {
    val sparkApplication = Mockito.mock(classOf[SparkApplication])

    val wfr = new ResourceWaitStage(sparkApplication)
    Assert.assertEquals("ResourceWaitStage", wfr.getStageName)
  }

  test("test ResourceWaitStage needCheckResource") {
    val sparkApplication = Mockito.mock(classOf[SparkApplication])
    val sparkConf = Mockito.mock(classOf[SparkConf])
    val kylinConfig = Mockito.mock(classOf[KylinConfig])

    val wfr = new ResourceWaitStage(sparkApplication)
    ReflectionUtils.setField(wfr, "sparkConf", sparkConf)
    ReflectionUtils.setField(wfr, "config", kylinConfig)

    Mockito.when(sparkApplication.isJobOnCluster(sparkConf)).thenReturn(true)
    Mockito.when(kylinConfig.getWaitResourceEnabled).thenReturn(true)
    Assert.assertTrue(wfr.needCheckResource)

    Mockito.when(sparkApplication.isJobOnCluster(sparkConf)).thenReturn(false)
    Assert.assertFalse(wfr.needCheckResource)

    Mockito.when(sparkApplication.isJobOnCluster(sparkConf)).thenReturn(true)
    Mockito.when(kylinConfig.getWaitResourceEnabled).thenReturn(false)
    Assert.assertFalse(wfr.needCheckResource)

    Mockito.when(sparkApplication.isJobOnCluster(sparkConf)).thenReturn(false)
    Assert.assertFalse(wfr.needCheckResource)
  }
}
