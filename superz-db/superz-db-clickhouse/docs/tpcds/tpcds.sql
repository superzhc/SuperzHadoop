--
-- Legal Notice
--
-- This document and associated source code (the "Work") is a part of a
-- benchmark specification maintained by the TPC.
--
-- The TPC reserves all right, title, and interest to the Work as provided
-- under U.S. and international laws, including without limitation all patent
-- and trademark rights therein.
--
-- No Warranty
--
-- 1.1 TO THE MAXIMUM EXTENT PERMITTED BY APPLICABLE LAW, THE INFORMATION
--     CONTAINED HEREIN IS PROVIDED "AS IS" AND WITH ALL FAULTS, AND THE
--     AUTHORS AND DEVELOPERS OF THE WORK HEREBY DISCLAIM ALL OTHER
--     WARRANTIES AND CONDITIONS, EITHER EXPRESS, IMPLIED OR STATUTORY,
--     INCLUDING, BUT NOT LIMITED TO, ANY (IF ANY) IMPLIED WARRANTIES,
--     DUTIES OR CONDITIONS OF MERCHANTABILITY, OF FITNESS FOR A PARTICULAR
--     PURPOSE, OF ACCURACY OR COMPLETENESS OF RESPONSES, OF RESULTS, OF
--     WORKMANLIKE EFFORT, OF LACK OF VIRUSES, AND OF LACK OF NEGLIGENCE.
--     ALSO, THERE IS NO WARRANTY OR CONDITION OF TITLE, QUIET ENJOYMENT,
--     QUIET POSSESSION, CORRESPONDENCE TO DESCRIPTION OR NON-INFRINGEMENT
--     WITH REGARD TO THE WORK.
-- 1.2 IN NO EVENT WILL ANY AUTHOR OR DEVELOPER OF THE WORK BE LIABLE TO
--     ANY OTHER PARTY FOR ANY DAMAGES, INCLUDING BUT NOT LIMITED TO THE
--     COST OF PROCURING SUBSTITUTE GOODS OR SERVICES, LOST PROFITS, LOSS
--     OF USE, LOSS OF DATA, OR ANY INCIDENTAL, CONSEQUENTIAL, DIRECT,
--     INDIRECT, OR SPECIAL DAMAGES WHETHER UNDER CONTRACT, TORT, WARRANTY,
--     OR OTHERWISE, ARISING IN ANY WAY OUT OF THIS OR ANY OTHER AGREEMENT
--     RELATING TO THE WORK, WHETHER OR NOT SUCH AUTHOR OR DEVELOPER HAD
--     ADVANCE NOTICE OF THE POSSIBILITY OF SUCH DAMAGES.
--
-- Contributors:
-- Gradient Systems
--

/*create table dbgen_version
(
    dv_version                String                   ,
    dv_create_date            date                          ,
    dv_create_time            time                          ,
    dv_cmdline_args           String
)
    ENGINE = MergeTree();*/

drop table if exists customer_address;
create table customer_address
(
    ca_address_sk             Int32              ,
    ca_address_id             String             ,
    ca_street_number          Nullable(String)                      ,
    ca_street_name            Nullable(String)                   ,
    ca_street_type            Nullable(String)                      ,
    ca_suite_number           Nullable(String)                      ,
    ca_city                   Nullable(String)                   ,
    ca_county                 Nullable(String)                   ,
    ca_state                  Nullable(String)                       ,
    ca_zip                    Nullable(String)                      ,
    ca_country                Nullable(String)                   ,
    ca_gmt_offset             Nullable(Decimal(5,2))                  ,
    ca_location_type          Nullable(String)
)
    ENGINE = MergeTree()
    PRIMARY KEY ca_address_sk
;

drop table if exists customer_demographics;
create table customer_demographics
(
    cd_demo_sk                Int32               ,
    cd_gender                 Nullable(String)                       ,
    cd_marital_status         Nullable(String)                       ,
    cd_education_status       Nullable(String)                      ,
    cd_purchase_estimate      Nullable(Int32)                       ,
    cd_credit_rating          Nullable(String)                      ,
    cd_dep_count              Nullable(Int32)                       ,
    cd_dep_employed_count     Nullable(Int32)                       ,
    cd_dep_college_count      Nullable(Int32)
)
    ENGINE = MergeTree()
    PRIMARY KEY cd_demo_sk;

drop table if exists date_dim;
create table date_dim
(
    d_date_sk                 Int32               ,
    d_date_id                 String              ,
    d_date                    Nullable(Date)                          ,
    d_month_seq               Nullable(Int32)                       ,
    d_week_seq                Nullable(Int32)                       ,
    d_quarter_seq             Nullable(Int32)                       ,
    d_year                    Nullable(Int32)                       ,
    d_dow                     Nullable(Int32)                       ,
    d_moy                     Nullable(Int32)                       ,
    d_dom                     Nullable(Int32)                       ,
    d_qoy                     Nullable(Int32)                       ,
    d_fy_year                 Nullable(Int32)                       ,
    d_fy_quarter_seq          Nullable(Int32)                       ,
    d_fy_week_seq             Nullable(Int32)                       ,
    d_day_name                Nullable(String)                       ,
    d_quarter_name            Nullable(String)                       ,
    d_holiday                 Nullable(String)                       ,
    d_weekend                 Nullable(String)                       ,
    d_following_holiday       Nullable(String)                       ,
    d_first_dom               Nullable(Int32)                       ,
    d_last_dom                Nullable(Int32)                       ,
    d_same_day_ly             Nullable(Int32)                       ,
    d_same_day_lq             Nullable(Int32)                       ,
    d_current_day             Nullable(String)                       ,
    d_current_week            Nullable(String)                       ,
    d_current_month           Nullable(String)                       ,
    d_current_quarter         Nullable(String)                       ,
    d_current_year            Nullable(String)
)
    ENGINE = MergeTree()
        primary key (d_date_sk);

drop table if exists warehouse;
create table warehouse
(
    w_warehouse_sk            Int32               ,
    w_warehouse_id            String              ,
    w_warehouse_name          Nullable(String)                   ,
    w_warehouse_sq_ft         Nullable(Int32)                       ,
    w_street_number           Nullable(String)                      ,
    w_street_name             Nullable(String)                   ,
    w_street_type             Nullable(String)                      ,
    w_suite_number            Nullable(String)                      ,
    w_city                    Nullable(String)                   ,
    w_county                  Nullable(String)                   ,
    w_state                   Nullable(String)                       ,
    w_zip                     Nullable(String)                      ,
    w_country                 Nullable(String)                   ,
    w_gmt_offset              Nullable(Decimal(5,2))
)
    ENGINE = MergeTree()
        primary key (w_warehouse_sk);

drop table if exists ship_mode;
create table ship_mode
(
    sm_ship_mode_sk           Int32               ,
    sm_ship_mode_id           String              ,
    sm_type                   Nullable(String)                      ,
    sm_code                   Nullable(String)                      ,
    sm_carrier                Nullable(String)                      ,
    sm_contract               Nullable(String)
)
    ENGINE = MergeTree()
        primary key (sm_ship_mode_sk);

drop table if exists time_dim;
create table time_dim
(
    t_time_sk                 Int32               ,
    t_time_id                 String              ,
    t_time                    Nullable(Int32)                       ,
    t_hour                    Nullable(Int32)                       ,
    t_minute                  Nullable(Int32)                       ,
    t_second                  Nullable(Int32)                       ,
    t_am_pm                   Nullable(String)                       ,
    t_shift                   Nullable(String)                      ,
    t_sub_shift               Nullable(String)                      ,
    t_meal_time               Nullable(String)
)
    ENGINE = MergeTree()
        primary key (t_time_sk);

drop table if exists reason;
create table reason
(
    r_reason_sk               Int32               ,
    r_reason_id               String              ,
    r_reason_desc             Nullable(String)
)
    ENGINE = MergeTree()
        primary key (r_reason_sk);

drop table if exists income_band;
create table income_band
(
    ib_income_band_sk         Int32               ,
    ib_lower_bound            Nullable(Int32)                       ,
    ib_upper_bound            Nullable(Int32)
)
    ENGINE = MergeTree()
        primary key (ib_income_band_sk);

drop table if exists item;
create table item
(
    i_item_sk                 Int32               ,
    i_item_id                 String              ,
    i_rec_start_date          Nullable(Date)                          ,
    i_rec_end_date            Nullable(Date)                          ,
    i_item_desc               Nullable(String)                  ,
    i_current_price           Nullable(Decimal(7,2))                  ,
    i_wholesale_cost          Nullable(Decimal(7,2))                  ,
    i_brand_id                Nullable(Int32)                       ,
    i_brand                   Nullable(String)                      ,
    i_class_id                Nullable(Int32)                       ,
    i_class                   Nullable(String)                      ,
    i_category_id             Nullable(Int32)                       ,
    i_category                Nullable(String)                      ,
    i_manufact_id             Nullable(Int32)                       ,
    i_manufact                Nullable(String)                      ,
    i_size                    Nullable(String)                      ,
    i_formulation             Nullable(String)                      ,
    i_color                   Nullable(String)                      ,
    i_units                   Nullable(String)                      ,
    i_container               Nullable(String)                      ,
    i_manager_id              Nullable(Int32)                       ,
    i_product_name            Nullable(String)
)
    ENGINE = MergeTree()
        primary key (i_item_sk);

drop table if exists store;
create table store
(
    s_store_sk                Int32               ,
    s_store_id                String              ,
    s_rec_start_date          Nullable(Date)                          ,
    s_rec_end_date            Nullable(Date)                          ,
    s_closed_date_sk          Nullable(Int32)                       ,
    s_store_name              Nullable(String)                   ,
    s_number_employees        Nullable(Int32)                       ,
    s_floor_space             Nullable(Int32)                       ,
    s_hours                   Nullable(String)                      ,
    s_manager                 Nullable(String)                   ,
    s_market_id               Nullable(Int32)                       ,
    s_geography_class         Nullable(String)                  ,
    s_market_desc             Nullable(String)                  ,
    s_market_manager          Nullable(String)                   ,
    s_division_id             Nullable(Int32)                       ,
    s_division_name           Nullable(String)                   ,
    s_company_id              Nullable(Int32)                       ,
    s_company_name            Nullable(String)                   ,
    s_street_number           Nullable(String)                   ,
    s_street_name             Nullable(String)                   ,
    s_street_type             Nullable(String)                      ,
    s_suite_number            Nullable(String)                      ,
    s_city                    Nullable(String)                   ,
    s_county                  Nullable(String)                   ,
    s_state                   Nullable(String)                       ,
    s_zip                     Nullable(String)                      ,
    s_country                 Nullable(String)                   ,
    s_gmt_offset              Nullable(Decimal(5,2))                  ,
    s_tax_precentage          Nullable(Decimal(5,2))
)
    ENGINE = MergeTree()
        primary key (s_store_sk);

drop table if exists call_center;
create table call_center
(
    cc_call_center_sk         Int32               ,
    cc_call_center_id         String              ,
    cc_rec_start_date         Nullable(Date)                          ,
    cc_rec_end_date           Nullable(Date)                          ,
    cc_closed_date_sk         Nullable(Int32)                       ,
    cc_open_date_sk           Nullable(Int32)                       ,
    cc_name                   Nullable(String)                   ,
    cc_class                  Nullable(String)                   ,
    cc_employees              Nullable(Int32)                       ,
    cc_sq_ft                  Nullable(Int32)                       ,
    cc_hours                  Nullable(String)                      ,
    cc_manager                Nullable(String)                   ,
    cc_mkt_id                 Nullable(Int32)                       ,
    cc_mkt_class              Nullable(String)                      ,
    cc_mkt_desc               Nullable(String)                  ,
    cc_market_manager         Nullable(String)                   ,
    cc_division               Nullable(Int32)                       ,
    cc_division_name          Nullable(String)                   ,
    cc_company                Nullable(Int32)                       ,
    cc_company_name           Nullable(String)                      ,
    cc_street_number          Nullable(String)                      ,
    cc_street_name            Nullable(String)                   ,
    cc_street_type            Nullable(String)                      ,
    cc_suite_number           Nullable(String)                      ,
    cc_city                   Nullable(String)                   ,
    cc_county                 Nullable(String)                   ,
    cc_state                  Nullable(String)                       ,
    cc_zip                    Nullable(String)                      ,
    cc_country                Nullable(String)                   ,
    cc_gmt_offset             Nullable(Decimal(5,2))                  ,
    cc_tax_percentage         Nullable(Decimal(5,2))
)
    ENGINE = MergeTree()
        primary key (cc_call_center_sk);

drop table if exists customer;
create table customer
(
    c_customer_sk             Int32               ,
    c_customer_id             String              ,
    c_current_cdemo_sk        Nullable(Int32)                       ,
    c_current_hdemo_sk        Nullable(Int32)                       ,
    c_current_addr_sk         Nullable(Int32)                       ,
    c_first_shipto_date_sk    Nullable(Int32)                       ,
    c_first_sales_date_sk     Nullable(Int32)                       ,
    c_salutation              Nullable(String)                      ,
    c_first_name              Nullable(String)                      ,
    c_last_name               Nullable(String)                      ,
    c_preferred_cust_flag     Nullable(String)                       ,
    c_birth_day               Nullable(Int32)                       ,
    c_birth_month             Nullable(Int32)                       ,
    c_birth_year              Nullable(Int32)                       ,
    c_birth_country           Nullable(String)                   ,
    c_login                   Nullable(String)                      ,
    c_email_address           Nullable(String)                      ,
    c_last_review_date_sk     Nullable(Int32)
)
    ENGINE = MergeTree()
        primary key (c_customer_sk);

drop table if exists web_site;
create table web_site
(
    web_site_sk               Int32               ,
    web_site_id               String              ,
    web_rec_start_date        Nullable(Date)                          ,
    web_rec_end_date          Nullable(Date)                          ,
    web_name                  Nullable(String)                   ,
    web_open_date_sk          Nullable(Int32)                       ,
    web_close_date_sk         Nullable(Int32)                       ,
    web_class                 Nullable(String)                   ,
    web_manager               String                   ,
    web_mkt_id                Nullable(Int32)                       ,
    web_mkt_class             Nullable(String)                   ,
    web_mkt_desc              Nullable(String)                  ,
    web_market_manager        Nullable(String)                   ,
    web_company_id            Nullable(Int32)                       ,
    web_company_name          Nullable(String)                      ,
    web_street_number         Nullable(String)                      ,
    web_street_name           Nullable(String)                   ,
    web_street_type           Nullable(String)                      ,
    web_suite_number          Nullable(String)                      ,
    web_city                  Nullable(String)                   ,
    web_county                Nullable(String)                   ,
    web_state                 Nullable(String)                       ,
    web_zip                   Nullable(String)                      ,
    web_country               Nullable(String)                   ,
    web_gmt_offset            Nullable(Decimal(5,2))                  ,
    web_tax_percentage        Nullable(Decimal(5,2))
)
    ENGINE = MergeTree()
        primary key (web_site_sk);

drop table if exists store_returns;
create table store_returns
(
    sr_returned_date_sk       Nullable(Int32)                       ,
    sr_return_time_sk         Nullable(Int32)                       ,
    sr_item_sk                Int32                                 ,
    sr_customer_sk            Nullable(Int32)                       ,
    sr_cdemo_sk               Nullable(Int32)                       ,
    sr_hdemo_sk               Nullable(Int32)                       ,
    sr_addr_sk                Nullable(Int32)                       ,
    sr_store_sk               Nullable(Int32)                       ,
    sr_reason_sk              Nullable(Int32)                       ,
    sr_ticket_number          Int32               ,
    sr_return_quantity        Nullable(Int32)                       ,
    sr_return_amt             Nullable(Decimal(7,2))                  ,
    sr_return_tax             Nullable(Decimal(7,2))                  ,
    sr_return_amt_inc_tax     Nullable(Decimal(7,2))                  ,
    sr_fee                    Nullable(Decimal(7,2))                  ,
    sr_return_ship_cost       Nullable(Decimal(7,2))                  ,
    sr_refunded_cash          Nullable(Decimal(7,2))                  ,
    sr_reversed_charge        Nullable(Decimal(7,2))                  ,
    sr_store_credit           Nullable(Decimal(7,2))                  ,
    sr_net_loss               Nullable(Decimal(7,2))
)
    ENGINE = MergeTree()
        primary key (sr_item_sk, sr_ticket_number);

drop table if exists household_demographics;
create table household_demographics
(
    hd_demo_sk                Int32               ,
    hd_income_band_sk         Nullable(Int32)                       ,
    hd_buy_potential          Nullable(String)                      ,
    hd_dep_count              Nullable(Int32)                       ,
    hd_vehicle_count          Nullable(Int32)
)
    ENGINE = MergeTree()
        primary key (hd_demo_sk);

drop table if exists web_page;
create table web_page
(
    wp_web_page_sk            Int32               ,
    wp_web_page_id            String              ,
    wp_rec_start_date         Nullable(Date)                          ,
    wp_rec_end_date           Nullable(Date)                          ,
    wp_creation_date_sk       Nullable(Int32)                       ,
    wp_access_date_sk         Nullable(Int32)                       ,
    wp_autogen_flag           Nullable(String)                       ,
    wp_customer_sk            Nullable(Int32)                       ,
    wp_url                    Nullable(String)                  ,
    wp_type                   Nullable(String)                      ,
    wp_char_count             Nullable(Int32)                       ,
    wp_link_count             Nullable(Int32)                       ,
    wp_image_count            Nullable(Int32)                       ,
    wp_max_ad_count           Nullable(Int32)
)
    ENGINE = MergeTree()
        primary key (wp_web_page_sk);

drop table if exists promotion;
create table promotion
(
    p_promo_sk                Int32               ,
    p_promo_id                String              ,
    p_start_date_sk           Nullable(Int32)                       ,
    p_end_date_sk             Nullable(Int32)                       ,
    p_item_sk                 Nullable(Int32)                       ,
    p_cost                    Nullable(Decimal(15,2))                 ,
    p_response_target         Nullable(Int32)                       ,
    p_promo_name              Nullable(String)                      ,
    p_channel_dmail           Nullable(String)                       ,
    p_channel_email           Nullable(String)                       ,
    p_channel_catalog         Nullable(String)                       ,
    p_channel_tv              Nullable(String)                       ,
    p_channel_radio           Nullable(String)                       ,
    p_channel_press           Nullable(String)                       ,
    p_channel_event           Nullable(String)                       ,
    p_channel_demo            Nullable(String)                       ,
    p_channel_details         Nullable(String)                  ,
    p_purpose                 Nullable(String)                      ,
    p_discount_active         Nullable(String)
)
    ENGINE = MergeTree()
        primary key (p_promo_sk);

drop table if exists catalog_page;
create table catalog_page
(
    cp_catalog_page_sk        Int32               ,
    cp_catalog_page_id        String              ,
    cp_start_date_sk          Nullable(Int32)                       ,
    cp_end_date_sk            Nullable(Int32)                       ,
    cp_department             Nullable(String)                   ,
    cp_catalog_number         Nullable(Int32)                       ,
    cp_catalog_page_number    Nullable(Int32)                       ,
    cp_description            Nullable(String)                  ,
    cp_type                   Nullable(String)
)
    ENGINE = MergeTree()
        primary key (cp_catalog_page_sk);

drop table if exists inventory;
create table inventory
(
    inv_date_sk               Int32               ,
    inv_item_sk               Int32               ,
    inv_warehouse_sk          Int32               ,
    inv_quantity_on_hand      Nullable(Int32)
)
    ENGINE = MergeTree()
        primary key (inv_date_sk, inv_item_sk, inv_warehouse_sk);

drop table if exists catalog_returns;
create table catalog_returns
(
    cr_returned_date_sk       Nullable(Int32)                       ,
    cr_returned_time_sk       Nullable(Int32)                       ,
    cr_item_sk                Int32               ,
    cr_refunded_customer_sk   Nullable(Int32)                       ,
    cr_refunded_cdemo_sk      Nullable(Int32)                       ,
    cr_refunded_hdemo_sk      Nullable(Int32)                       ,
    cr_refunded_addr_sk       Nullable(Int32)                       ,
    cr_returning_customer_sk  Nullable(Int32)                       ,
    cr_returning_cdemo_sk     Nullable(Int32)                       ,
    cr_returning_hdemo_sk     Nullable(Int32)                       ,
    cr_returning_addr_sk      Nullable(Int32)                       ,
    cr_call_center_sk         Nullable(Int32)                       ,
    cr_catalog_page_sk        Nullable(Int32)                       ,
    cr_ship_mode_sk           Nullable(Int32)                       ,
    cr_warehouse_sk           Nullable(Int32)                       ,
    cr_reason_sk              Nullable(Int32)                       ,
    cr_order_number           Int32               ,
    cr_return_quantity        Nullable(Int32)                       ,
    cr_return_amount          Nullable(Decimal(7,2))                  ,
    cr_return_tax             Nullable(Decimal(7,2))                  ,
    cr_return_amt_inc_tax     Nullable(Decimal(7,2))                  ,
    cr_fee                    Nullable(Decimal(7,2))                  ,
    cr_return_ship_cost       Nullable(Decimal(7,2))                  ,
    cr_refunded_cash          Nullable(Decimal(7,2))                  ,
    cr_reversed_charge        Nullable(Decimal(7,2))                  ,
    cr_store_credit           Nullable(Decimal(7,2))                  ,
    cr_net_loss               Nullable(Decimal(7,2))
)
    ENGINE = MergeTree()
        primary key (cr_item_sk, cr_order_number);

drop table if exists web_returns;
create table web_returns
(
    wr_returned_date_sk       Nullable(Int32)                       ,
    wr_returned_time_sk       Nullable(Int32)                       ,
    wr_item_sk                Int32               ,
    wr_refunded_customer_sk   Nullable(Int32)                       ,
    wr_refunded_cdemo_sk      Nullable(Int32)                       ,
    wr_refunded_hdemo_sk      Nullable(Int32)                       ,
    wr_refunded_addr_sk       Nullable(Int32)                       ,
    wr_returning_customer_sk  Nullable(Int32)                       ,
    wr_returning_cdemo_sk     Nullable(Int32)                       ,
    wr_returning_hdemo_sk     Nullable(Int32)                       ,
    wr_returning_addr_sk      Nullable(Int32)                       ,
    wr_web_page_sk            Nullable(Int32)                       ,
    wr_reason_sk              Nullable(Int32)                       ,
    wr_order_number           Int32               ,
    wr_return_quantity        Nullable(Int32)                       ,
    wr_return_amt             Nullable(Decimal(7,2))                  ,
    wr_return_tax             Nullable(Decimal(7,2))                  ,
    wr_return_amt_inc_tax     Nullable(Decimal(7,2))                  ,
    wr_fee                    Nullable(Decimal(7,2))                  ,
    wr_return_ship_cost       Nullable(Decimal(7,2))                  ,
    wr_refunded_cash          Nullable(Decimal(7,2))                  ,
    wr_reversed_charge        Nullable(Decimal(7,2))                  ,
    wr_account_credit         Nullable(Decimal(7,2))                  ,
    wr_net_loss               Nullable(Decimal(7,2))
)
    ENGINE = MergeTree()
        primary key (wr_item_sk, wr_order_number);

drop table if exists web_sales;
create table web_sales
(
    ws_sold_date_sk           Nullable(Int32)                       ,
    ws_sold_time_sk           Nullable(Int32)                       ,
    ws_ship_date_sk           Nullable(Int32)                       ,
    ws_item_sk                Int32               ,
    ws_bill_customer_sk       Nullable(Int32)                       ,
    ws_bill_cdemo_sk          Nullable(Int32)                       ,
    ws_bill_hdemo_sk          Nullable(Int32)                       ,
    ws_bill_addr_sk           Nullable(Int32)                       ,
    ws_ship_customer_sk       Nullable(Int32)                       ,
    ws_ship_cdemo_sk          Nullable(Int32)                       ,
    ws_ship_hdemo_sk          Nullable(Int32)                       ,
    ws_ship_addr_sk           Nullable(Int32)                       ,
    ws_web_page_sk            Nullable(Int32)                       ,
    ws_web_site_sk            Nullable(Int32)                       ,
    ws_ship_mode_sk           Nullable(Int32)                       ,
    ws_warehouse_sk           Nullable(Int32)                       ,
    ws_promo_sk               Nullable(Int32)                       ,
    ws_order_number           Int32               ,
    ws_quantity               Nullable(Int32)                       ,
    ws_wholesale_cost         Nullable(Decimal(7,2))                  ,
    ws_list_price             Nullable(Decimal(7,2))                  ,
    ws_sales_price            Nullable(Decimal(7,2))                  ,
    ws_ext_discount_amt       Nullable(Decimal(7,2))                  ,
    ws_ext_sales_price        Nullable(Decimal(7,2))                  ,
    ws_ext_wholesale_cost     Nullable(Decimal(7,2))                  ,
    ws_ext_list_price         Nullable(Decimal(7,2))                  ,
    ws_ext_tax                Nullable(Decimal(7,2))                  ,
    ws_coupon_amt             Nullable(Decimal(7,2))                  ,
    ws_ext_ship_cost          Nullable(Decimal(7,2))                  ,
    ws_net_paid               Nullable(Decimal(7,2))                  ,
    ws_net_paid_inc_tax       Nullable(Decimal(7,2))                  ,
    ws_net_paid_inc_ship      Nullable(Decimal(7,2))                  ,
    ws_net_paid_inc_ship_tax  Nullable(Decimal(7,2))                  ,
    ws_net_profit             Nullable(Decimal(7,2))
)
    ENGINE = MergeTree()
        primary key (ws_item_sk, ws_order_number);

drop table if exists catalog_sales;
create table catalog_sales
(
    cs_sold_date_sk           Nullable(Int32)                       ,
    cs_sold_time_sk           Nullable(Int32)                       ,
    cs_ship_date_sk           Nullable(Int32)                       ,
    cs_bill_customer_sk       Nullable(Int32)                       ,
    cs_bill_cdemo_sk          Nullable(Int32)                       ,
    cs_bill_hdemo_sk          Nullable(Int32)                       ,
    cs_bill_addr_sk           Nullable(Int32)                       ,
    cs_ship_customer_sk       Nullable(Int32)                       ,
    cs_ship_cdemo_sk          Nullable(Int32)                       ,
    cs_ship_hdemo_sk          Nullable(Int32)                       ,
    cs_ship_addr_sk           Nullable(Int32)                       ,
    cs_call_center_sk         Nullable(Int32)                       ,
    cs_catalog_page_sk        Nullable(Int32)                       ,
    cs_ship_mode_sk           Nullable(Int32)                       ,
    cs_warehouse_sk           Nullable(Int32)                       ,
    cs_item_sk                Int32               ,
    cs_promo_sk               Nullable(Int32)                       ,
    cs_order_number           Int32               ,
    cs_quantity               Nullable(Int32)                       ,
    cs_wholesale_cost         Nullable(Decimal(7,2))                  ,
    cs_list_price             Nullable(Decimal(7,2))                  ,
    cs_sales_price            Nullable(Decimal(7,2))                  ,
    cs_ext_discount_amt       Nullable(Decimal(7,2))                  ,
    cs_ext_sales_price        Nullable(Decimal(7,2))                  ,
    cs_ext_wholesale_cost     Nullable(Decimal(7,2))                  ,
    cs_ext_list_price         Nullable(Decimal(7,2))                  ,
    cs_ext_tax                Nullable(Decimal(7,2))                  ,
    cs_coupon_amt             Nullable(Decimal(7,2))                  ,
    cs_ext_ship_cost          Nullable(Decimal(7,2))                  ,
    cs_net_paid               Nullable(Decimal(7,2))                  ,
    cs_net_paid_inc_tax       Nullable(Decimal(7,2))                  ,
    cs_net_paid_inc_ship      Nullable(Decimal(7,2))                  ,
    cs_net_paid_inc_ship_tax  Nullable(Decimal(7,2))                  ,
    cs_net_profit             Nullable(Decimal(7,2))
)
    ENGINE = MergeTree()
        primary key (cs_item_sk, cs_order_number);

drop table if exists store_sales;
create table store_sales
(
    ss_sold_date_sk           Nullable(Int32)                       ,
    ss_sold_time_sk           Nullable(Int32)                       ,
    ss_item_sk                Int32               ,
    ss_customer_sk            Nullable(Int32)                       ,
    ss_cdemo_sk               Nullable(Int32)                       ,
    ss_hdemo_sk               Nullable(Int32)                       ,
    ss_addr_sk                Nullable(Int32)                       ,
    ss_store_sk               Nullable(Int32)                       ,
    ss_promo_sk               Nullable(Int32)                       ,
    ss_ticket_number          Int32               ,
    ss_quantity               Nullable(Int32)                       ,
    ss_wholesale_cost         Nullable(Decimal(7,2))                  ,
    ss_list_price             Nullable(Decimal(7,2))                  ,
    ss_sales_price            Nullable(Decimal(7,2))                  ,
    ss_ext_discount_amt       Nullable(Decimal(7,2))                  ,
    ss_ext_sales_price        Nullable(Decimal(7,2))                  ,
    ss_ext_wholesale_cost     Nullable(Decimal(7,2))                  ,
    ss_ext_list_price         Nullable(Decimal(7,2))                  ,
    ss_ext_tax                Nullable(Decimal(7,2))                  ,
    ss_coupon_amt             Nullable(Decimal(7,2))                  ,
    ss_net_paid               Nullable(Decimal(7,2))                  ,
    ss_net_paid_inc_tax       Nullable(Decimal(7,2))                  ,
    ss_net_profit             Nullable(Decimal(7,2))
)
    ENGINE = MergeTree()
        primary key (ss_item_sk, ss_ticket_number);

