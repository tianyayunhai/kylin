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

package org.apache.kylin.query.optrule;

import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.rel.core.Window;
import org.apache.kylin.query.relnode.OlapRel;
import org.apache.kylin.query.relnode.OlapWindowRel;

public class OlapWindowRule extends ConverterRule {

    public static final OlapWindowRule INSTANCE = new OlapWindowRule();

    public OlapWindowRule() {
        super(Window.class, Convention.NONE, OlapRel.CONVENTION, "OlapWindowRule");
    }

    @Override
    public RelNode convert(RelNode rel) {
        final Window window = (Window) rel;
        final RelTraitSet traitSet = window.getTraitSet().replace(OlapRel.CONVENTION);
        final RelNode input = window.getInput();
        return new OlapWindowRel(rel.getCluster(), traitSet,
                convert(input, input.getTraitSet().replace(OlapRel.CONVENTION)), window.constants, window.getRowType(),
                window.groups);
    }
}
