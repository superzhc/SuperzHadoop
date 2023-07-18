--
-- 导入数据脚本
-- Apache Doris v1.2 不支持 MySql load 方式
--
-- 读取 MySQL 客户端本地文件进行导入
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/dbgen_version.dat' INTO TABLE dbgen_version COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/customer_address.dat' INTO TABLE customer_address COLUMNS TERMINATED BY '|'; 
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/customer_demographics.dat' INTO TABLE customer_demographics COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/date_dim.dat' INTO TABLE date_dim COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/warehouse.dat' INTO TABLE warehouse COLUMNS TERMINATED BY '|'; 
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/ship_mode.dat' INTO TABLE ship_mode COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/time_dim.dat' INTO TABLE time_dim COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/reason.dat' INTO TABLE reason COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/income_band.dat' INTO TABLE income_band COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/item.dat' INTO TABLE item COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/store.dat' INTO TABLE store COLUMNS TERMINATED BY '|'; 
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/call_center.dat' INTO TABLE call_center COLUMNS TERMINATED BY '|'; 
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/customer.dat' INTO TABLE customer COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/web_site.dat' INTO TABLE web_site COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/store_returns.dat' INTO TABLE store_returns COLUMNS TERMINATED BY '|'; 
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/household_demographics.dat' INTO TABLE household_demographics COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/web_page.dat' INTO TABLE web_page COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/promotion.dat' INTO TABLE promotion COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/catalog_page.dat' INTO TABLE catalog_page COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/inventory.dat' INTO TABLE inventory COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/catalog_returns.dat' INTO TABLE catalog_returns COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/web_returns.dat' INTO TABLE web_returns COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/web_sales.dat' INTO TABLE web_sales COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/catalog_sales.dat' INTO TABLE catalog_sales COLUMNS TERMINATED BY '|';
LOAD DATA LOCAL INFILE '/opt/tpcds-data/1g/store_sales.dat' INTO TABLE store_sales COLUMNS TERMINATED BY '|';