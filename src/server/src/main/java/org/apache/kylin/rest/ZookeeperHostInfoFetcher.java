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
package org.apache.kylin.rest;

import org.apache.kylin.common.util.HostInfoFetcher;
import org.apache.kylin.rest.discovery.ConditionalOnNodeRegistryZookeeperEnabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.zookeeper.ConditionalOnZookeeperEnabled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@ConditionalOnZookeeperEnabled
@ConditionalOnNodeRegistryZookeeperEnabled
@Component
@Slf4j
public class ZookeeperHostInfoFetcher implements HostInfoFetcher {

    @Autowired
    InetUtils inetUtils;

    @Override
    public String getHostname() {
        return inetUtils.findFirstNonLoopbackHostInfo().getHostname();
    }
}
