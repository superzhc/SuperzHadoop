# Hudi

## Spark 数据源

### 写选项

#### TABLE_NAME_OPT_KEY

属性：`hoodie.datasource.write.table.name` [必须]

Hive表名，用于将数据集注册到其中。

#### OPERATION_OPT_KEY 

属性：`hoodie.datasource.write.operation`, 默认值：`upsert`

是否为写操作进行插入更新、插入或批量插入。

使用`bulkinsert`将新数据加载到表中，之后使用`upsert`或`insert`。 

批量插入使用基于磁盘的写入路径来扩展以加载大量输入，而无需对其进行缓存。

#### STORAGE_TYPE_OPT_KEY 

属性：`hoodie.datasource.write.storage.type`, 默认值：`COPY_ON_WRITE`

此写入的基础数据的存储类型。两次写入之间不能改变。

#### PRECOMBINE_FIELD_OPT_KEY 

属性：`hoodie.datasource.write.precombine.field`, 默认值：`ts`

实际写入之前在preCombining中使用的字段。 

当两个记录具有相同的键值时，将使用Object.compareTo(..)从precombine字段中选择一个值最大的记录。

#### PAYLOAD_CLASS_OPT_KEY

属性：`hoodie.datasource.write.payload.class`, 默认值：`org.apache.hudi.OverwriteWithLatestAvroPayload`

使用的有效载荷类。如果您想在插入更新或插入时使用自己的合并逻辑，请重写此方法。 这将使得`PRECOMBINE_FIELD_OPT_VAL`设置的任何值无效

#### RECORDKEY_FIELD_OPT_KEY 

属性：`hoodie.datasource.write.recordkey.field`, 默认值：`uuid`

记录键字段。用作`HoodieKey`中`recordKey`部分的值。 实际值将通过在字段值上调用`.toString()`来获得。可以使用点符号指定嵌套字段，例如：`a.b.c`

#### PARTITIONPATH_FIELD_OPT_KEY

属性：`hoodie.datasource.write.partitionpath.field`, 默认值：`partitionpath`

分区路径字段。用作`HoodieKey`中`partitionPath`部分的值。 通过调用`.toString()`获得实际的值

#### HIVE_STYLE_PARTITIONING_OPT_KEY

属性：`hoodie.datasource.write.hive_style_partitioning`, 默认值：`false`

如果设置为true，则生成基于Hive格式的partition目录：`partition_column_name=partition_value`

#### KEYGENERATOR_CLASS_OPT_KEY

属性：`hoodie.datasource.write.keygenerator.class`

键生成器类，实现从输入的`Row`对象中提取键。该配置优先级大于 `hoodie.datasource.write.keygenerator.type`, 用于使用用户自定义键生成器

#### KEYGENERATOR_TYPE_OPT_KEY

属性: `hoodie.datasource.write.keygenerator.type`, 默认值: `SIMPLE`

键生成器类型，默认 `SIMPLE` 类型，该配置优先级低于 `hoodie.datasource.write.keygenerator.class`, 是推荐使用的配置方式

#### COMMIT_METADATA_KEYPREFIX_OPT_KEY 

属性：`hoodie.datasource.write.commitmeta.key.prefix`, 默认值：`_`

以该前缀开头的选项键会自动添加到提交/增量提交的元数据中。 这对于与hudi时间轴一致的方式存储检查点信息很有用

#### INSERT_DROP_DUPS_OPT_KEY

属性：`hoodie.datasource.write.insert.drop.duplicates`, 默认值：`false`

如果设置为true，则在插入操作期间从传入DataFrame中过滤掉所有重复记录。

#### HIVE_SYNC_ENABLED_OPT_KEY 

属性：`hoodie.datasource.hive_sync.enable`, 默认值：`false`

设置为true时，将数据集注册并同步到Apache Hive Metastore

#### HIVE_DATABASE_OPT_KEY 

属性：`hoodie.datasource.hive_sync.database`, 默认值：`default`

要同步到的数据库

#### HIVE_TABLE_OPT_KEY 

属性：`hoodie.datasource.hive_sync.table`, [Required]

要同步到的表

#### HIVE_USER_OPT_KEY 

属性：`hoodie.datasource.hive_sync.username`, 默认值：`hive`

要使用的Hive用户名

#### HIVE_PASS_OPT_KEY 

属性：`hoodie.datasource.hive_sync.password`, 默认值：`hive`

要使用的Hive密码

#### HIVE_URL_OPT_KEY 

属性：`hoodie.datasource.hive_sync.jdbcurl`, 默认值：`jdbc:hive2://localhost:10000`

Hive metastore url

#### HIVE_PARTITION_FIELDS_OPT_KEY 

属性：`hoodie.datasource.hive_sync.partition_fields`, 默认值：` `

数据集中用于确定Hive分区的字段。

#### HIVE_PARTITION_EXTRACTOR_CLASS_OPT_KEY 

属性：`hoodie.datasource.hive_sync.partition_extractor_class`, 默认值：`org.apache.hudi.hive.SlashEncodedDayPartitionValueExtractor`

用于将分区字段值提取到Hive分区列中的类。

#### HIVE_ASSUME_DATE_PARTITION_OPT_KEY 

属性：`hoodie.datasource.hive_sync.assume_date_partitioning`, 默认值：`false`

假设分区格式是yyyy/mm/dd

### 读选项

#### VIEW_TYPE_OPT_KEY

属性：`hoodie.datasource.view.type`, 默认值：`read_optimized`

是否需要以某种模式读取数据，增量模式（自InstantTime以来的新数据） （或）读优化模式（基于列数据获取最新视图） （或）实时模式（基于行和列数据获取最新视图）

#### BEGIN_INSTANTTIME_OPT_KEY

属性：`hoodie.datasource.read.begin.instanttime`, [在增量模式下必须]

开始增量提取数据的即时时间。这里的instanttime不必一定与时间轴上的即时相对应。 取出以`instant_time > BEGIN_INSTANTTIME`写入的新数据。 例如：'20170901080000'将获取2017年9月1日08:00 AM之后写入的所有新数据。#### END_INSTANTTIME_OPT_KEY 属性：`hoodie.datasource.read.end.instanttime`, 默认值：最新即时（即从开始即时获取所有新数据）
限制增量提取的数据的即时时间。取出以`instant_time <= END_INSTANTTIME`写入的新数据。