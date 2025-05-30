--
-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements.  See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership.  The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License.  You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

select cal_dt, lstg_format_name, sum(price) as GMV,
first_value(sum(price)) over(partition by lstg_format_name order by cal_dt) as "first",
last_value(sum(price)) over(partition by lstg_format_name order by cal_dt) as "current",
round(lead(sum(price), 1) over(partition by lstg_format_name order by cal_dt), 4) as "prev",
round(lead(sum(price)) over(partition by lstg_format_name order by cal_dt), 4) as "next",
ntile(4) over (partition by lstg_format_name order by cal_dt) as "quarter"
from test_kylin_fact
where cal_dt < '2012-02-01'
group by cal_dt, lstg_format_name
order by cal_dt, lstg_format_name,GMV,"first","current","prev","next","quarter"
