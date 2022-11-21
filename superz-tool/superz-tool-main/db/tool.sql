create table superz_quartz_job_new
(
    id integer not null
        constraint superz_quartz_job_new_pk
            primary key autoincrement,
    _group varchar(255),
    _name varchar(255) not null,
    _class text not null,
    _cron varchar(255),
    _description text,
    is_enable integer default 0 not null
);

INSERT INTO superz_quartz_job_new (id, _group, _name, _class, _cron, _description, is_enable) VALUES (1, 'quartz', 'jobmanager', 'com.github.superzhc.tool.task.DynamicManagerTaskJob', '0 0/5 * * * ? *', '每5分钟扫描动态任务管理表，实现任务的动态管理', 1);
INSERT INTO superz_quartz_job_new (id, _group, _name, _class, _cron, _description, is_enable) VALUES (2, 'guangdiu', 'all', 'com.github.superzhc.hadoop.kafka.data.GuangDiuJob', '45 0/5 * * * ? *', null, 0);
INSERT INTO superz_quartz_job_new (id, _group, _name, _class, _cron, _description, is_enable) VALUES (3, 'guangdiu', 'food', 'com.github.superzhc.hadoop.kafka.data.GuangDiuJob', '5 0/5 * * * ? *', null, 0);
INSERT INTO superz_quartz_job_new (id, _group, _name, _class, _cron, _description, is_enable) VALUES (4, 'guangdiu', 'daily', 'com.github.superzhc.hadoop.kafka.data.GuangDiuJob', '10 0/5 * * * ? *', null, 0);
INSERT INTO superz_quartz_job_new (id, _group, _name, _class, _cron, _description, is_enable) VALUES (5, 'guangdiu', 'electrical', 'com.github.superzhc.hadoop.kafka.data.GuangDiuJob', '15 0/5 * * * ? *', null, 0);
INSERT INTO superz_quartz_job_new (id, _group, _name, _class, _cron, _description, is_enable) VALUES (6, 'guangdiu', 'medical', 'com.github.superzhc.hadoop.kafka.data.GuangDiuJob', '20 0/5 * * * ? *', null, 0);
INSERT INTO superz_quartz_job_new (id, _group, _name, _class, _cron, _description, is_enable) VALUES (7, 'guangdiu', 'mensshoes', 'com.github.superzhc.hadoop.kafka.data.GuangDiuJob', '25 0/5 * * * ? *', null, 0);
INSERT INTO superz_quartz_job_new (id, _group, _name, _class, _cron, _description, is_enable) VALUES (8, 'guangdiu', 'menswear', 'com.github.superzhc.hadoop.kafka.data.GuangDiuJob', '30 0/5 * * * ? *', null, 0);
INSERT INTO superz_quartz_job_new (id, _group, _name, _class, _cron, _description, is_enable) VALUES (9, 'guangdiu', 'sport', 'com.github.superzhc.hadoop.kafka.data.GuangDiuJob', '35 0/5 * * * ? *', null, 0);
INSERT INTO superz_quartz_job_new (id, _group, _name, _class, _cron, _description, is_enable) VALUES (10, 'guangdiu', 'furniture', 'com.github.superzhc.hadoop.kafka.data.GuangDiuJob', '40 0/5 * * * ? *', null, 0);
INSERT INTO superz_quartz_job_new (id, _group, _name, _class, _cron, _description, is_enable) VALUES (11, 'eastmoney', 'report', 'com.github.superzhc.hadoop.kafka.data.FinanceReportJob', '0 0 10 * * ?', '每天早晨10点获取一次报告', 1);
INSERT INTO superz_quartz_job_new (id, _group, _name, _class, _cron, _description, is_enable) VALUES (12, 'eastmoney', 'fund_real_net', 'com.github.superzhc.hadoop.kafka.data.FundRealNetJob', '0/5 * 9-11,13-14 ? * MON-FRI;* 0-29 9 * * ? *;* 31-59 11 * * ? *', '周一到周五9:30-11:30,13:00-14:59每5秒执行一次', 1);
INSERT INTO superz_quartz_job_new (id, _group, _name, _class, _cron, _description, is_enable) VALUES (13, 'smzdm', 'pinlei', 'com.github.superzhc.hadoop.kafka.data.SMZDMJob', '0 1/5 * * * ? *', null, 0);
INSERT INTO superz_quartz_job_new (id, _group, _name, _class, _cron, _description, is_enable) VALUES (14, 'smzdm', 'dianshang', 'com.github.superzhc.hadoop.kafka.data.SMZDMJob', '5 1/5 * * * ? *', null, 0);
INSERT INTO superz_quartz_job_new (id, _group, _name, _class, _cron, _description, is_enable) VALUES (15, 'danjuan', 'eva', 'com.github.superzhc.hadoop.kafka.data.IndexEvaJob', '0 0 21 * * ?', '每天晚上11点获取指数估值【蛋卷】', 1);

create table superz_quartz_job_data
(
    id integer not null
        constraint superz_quartz_job_data_pk
            primary key autoincrement,
    job_id integer not null,
    _key varchar(255) not null,
    _value text,
    _description text
);

INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (1, 2, 'brokers', '127.0.0.1:19092', 'Kafka Brokers 地址');
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (2, 2, 'type', 'all', null);
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (3, 3, 'brokers', '127.0.0.1:19092', 'Kafka Brokers 地址');
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (4, 3, 'type', 'food', null);
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (5, 4, 'brokers', '127.0.0.1:19092', 'Kafka Brokers 地址');
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (6, 4, 'type', 'daily', null);
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (7, 5, 'brokers', '127.0.0.1:19092', 'Kafka Brokers 地址');
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (8, 5, 'type', 'electrical', null);
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (9, 6, 'brokers', '127.0.0.1:19092', 'Kafka Brokers 地址');
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (10, 6, 'type', 'medical', null);
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (11, 7, 'brokers', '127.0.0.1:19092', 'Kafka Brokers 地址');
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (12, 7, 'type', 'mensshoes', null);
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (13, 8, 'brokers', '127.0.0.1:19092', 'Kafka Brokers 地址');
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (14, 8, 'type', 'menswear', null);
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (15, 9, 'brokers', '127.0.0.1:19092', 'Kafka Brokers 地址');
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (16, 9, 'type', 'sport', null);
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (17, 10, 'brokers', '127.0.0.1:19092', 'Kafka Brokers 地址');
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (18, 10, 'type', 'furniture', null);
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (19, 11, 'brokers', '127.0.0.1:19092', 'Kafka Brokers 地址');
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (20, 11, 'topic', 'finance_report_eastmoney', '存储的主题名称');
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (21, 12, 'brokers', '127.0.0.1:19092', 'Kafka Brokers 地址');
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (22, 12, 'topic', 'fund_eastmoney_real_net', null);
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (23, 12, 'codes', '000478,160119,519671,001594,001595,160716,501050,004752,501009,012820,519915,001180,000071,012348,090010,164402,006327,164906', 'fund codes');
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (24, 13, 'brokers', '127.0.0.1:19092', null);
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (25, 13, 'topic', 'shop_smzdm_pinlei', null);
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (26, 13, 'types', '好价品类榜_全部,好价品类榜_电脑数码,好价品类榜_家用电器,好价品类榜_日用百货,好价品类榜_服饰鞋包,好价品类榜_白菜,好价品类榜_运动户外,好价品类榜_食品生鲜', '好价品类的类型，多个之间使用英文逗号（,）隔开');
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (27, 14, 'brokers', '127.0.0.1:19092', null);
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (28, 14, 'topic', 'shop_smzdm_dianshang', null);
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (29, 14, 'types', '好价电商榜_天猫,好价电商榜_京东,好价电商榜_网易', '好价电商的类型，多个之间使用英文逗号（,）隔开');
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (30, 15, 'brokers', '127.0.0.1:19092', null);
INSERT INTO superz_quartz_job_data (id, job_id, _key, _value, _description) VALUES (31, 15, 'topic', 'index_danjuan_eva', null);

create table superz_quartz_dynamic_job
(
    id integer not null
        constraint superz_quartz_dynamic_job_pk
            primary key autoincrement,
    job_id integer not null,
    operate varchar(10),
    is_enable integer default 0 not null
);

INSERT INTO superz_quartz_dynamic_job (id, job_id, operate, is_enable) VALUES (1, 2, 'I', 0);
INSERT INTO superz_quartz_dynamic_job (id, job_id, operate, is_enable) VALUES (2, 2, 'D', 0);
INSERT INTO superz_quartz_dynamic_job (id, job_id, operate, is_enable) VALUES (3, 7, 'I', 0);
INSERT INTO superz_quartz_dynamic_job (id, job_id, operate, is_enable) VALUES (4, 11, 'I', 0);

