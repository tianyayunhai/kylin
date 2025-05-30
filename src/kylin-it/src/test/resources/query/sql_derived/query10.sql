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

SELECT
 test_cal_dt.week_beg_dt
 ,test_category_groupings.meta_categ_name
 ,test_category_groupings.upd_user
 ,test_category_groupings.upd_date
 ,test_kylin_fact.leaf_categ_id
 ,test_category_groupings.leaf_categ_id as leaf_categ_id2
 ,test_kylin_fact.lstg_site_id
 ,test_category_groupings.site_id as site_id2
 ,sum(price) as GMV, count(*) as TRANS_CNT
 FROM test_kylin_fact
  inner JOIN edw.test_cal_dt as test_cal_dt
 ON test_kylin_fact.cal_dt = test_cal_dt.cal_dt
 inner JOIN test_category_groupings
 ON test_kylin_fact.leaf_categ_id = test_category_groupings.leaf_categ_id AND test_kylin_fact.lstg_site_id = test_category_groupings.site_id
 inner JOIN edw.test_sites as test_sites
 ON test_kylin_fact.lstg_site_id = test_sites.site_id
 inner JOIN edw.test_seller_type_dim as test_seller_type_dim
 ON test_kylin_fact.slr_segment_cd = test_seller_type_dim.seller_type_cd
 INNER JOIN TEST_ORDER as TEST_ORDER
 ON TEST_KYLIN_FACT.ORDER_ID = TEST_ORDER.ORDER_ID
inner join test_account
on TEST_KYLIN_FACT.seller_id = test_account.account_id
inner join test_country
on test_account.account_country = test_country.country
 where
 test_category_groupings.upd_date='2012-09-11 20:26:04'
 group by test_cal_dt.week_beg_dt
 ,test_category_groupings.meta_categ_name
 ,test_category_groupings.upd_user
 ,test_category_groupings.upd_date
 ,test_kylin_fact.leaf_categ_id
 ,test_category_groupings.leaf_categ_id
 ,test_kylin_fact.lstg_site_id
 ,test_category_groupings.site_id
