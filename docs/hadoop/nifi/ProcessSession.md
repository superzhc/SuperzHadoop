# ProcessSession 接口解读

## 1. 简介

- ProcessSession 负责所有 FlowFile 对象的获取、克隆、读取、修改操作，且这些操作都是原子的。
- 在任意时刻，ProcessSession 只和**唯一**的 Processor 绑定，且 FlowFile 对象只能被**唯一**的 Processor 访问。
- ProcessSession 对象**不是线程安全**的，支持提交/回滚一组操作。
- 某些特定的方法或者某些特定的异常，会自动触发回滚，从而保证 repository 的一致性。但是很多情况下，需要开发者在代码中去进行回滚，从而保证 repository 的一致性。
- ProcessSession 的 commit/rollback 方法会彻底清空 ProcessSession 对象，相当于重新生成了一个新的 ProcessSession 对象。特别的，migrate 方法虽然表面上清空了 ProcessSession 对象，但是 ProcessSession 内部的引用计数器并不会被清空，这是 migrate 方法与 commit/rollback 方法间最大的区别。

## 2.API 介绍

### void commit()

> commit 方法保证 FlowFile 上的操作都被物化（attribute 写入到 FlowFileRepository 中，content 写入到 ContentRepository 中，事件写入到 ProvenanceRepository 中）。

> 在 commit 阶段，ProcessSession 中处理 FlowFile 对象要么被 transfer 到某个 RelationShip 中，要么被 remove，否则 commit 操作会失败。

> 只要 commit 完成后，ProcessSession 对象又可被使用。

> 抛出三类异常：（1）当 commit 方法是从 ProcessSession 对象的其他方法的 callback 接口（比如 write(FlowFile, OutputStreamCallback)）中被调用时，则会抛出**IllegalStateException**；（2）当 ProcessSession 中存在 FlowFile 没有被 transfer 到指定 RelationShip 中或者被 remove，则抛出**FlowFileHandlingException**；（3）commit 过程中发生其他错误，则抛出**ProcessException**，可通过 getCause 方法打印出异常信息。

### void rollback()/void rollback(boolean penalize)

> rollback 方法会将所有 FlowFile 对象回滚至刚进入当前 ProcessSession 对象时的状态，并返回给原始队列。

> 若当前 ProcessSession 已经提交或者回滚，再次调用回滚操作不会产生任何影响。

> rollback() == rollback(penalize == false)

### void migrate(ProcessSession newOwner, Collection<FlowFile> flowFiles)

> 将当前 ProcessSession 对象中的 flowFiles 转移给 newOwner。

> 调用此方法需要满足几个前提条件：（1）不能在 callback 接口中被调用；（2）所有被转移的 FlowFile 对象的输入/输出流都确保已经被关闭；（3）所有被转移的 FlowFile 对象的子 FlowFile 也必须被转移。

> 被转移走的 FlowFile 对象是最新的副本。这种情况发生在添加 attribute 的时候，也就是添加 attribute 后的 FlowFile 对象会被转移走。

### void adjustCounter(String name, long delta, boolean immediate)

> 只有当 ProcessSession 对象被 commit 后，才会调用该方法，用来调整引用计数。

### FlowFile get()

> 从 ProcessSession 中获取最高优先级的 FlowFile 对象，由于 ProcessSession 对象并不是线程安全的类，所以可能会得到**NULL**。

### List<FlowFile> get(int maxResults)

> 每次从 ProcessSession 对象中获取最多 maxResults 个 FlowFile 对象, 无 FlowFile 对象时，返回空集合，不会返回 null。

> 如果该 Processor 的上游有多条 Queue，那么 ProcessSession 是从所有 Queue 去取 FlowFile 对象还是从某个 Queue 中去取，行为是不确定的，必须去看具体的实现类。

### List<FlowFile> get(FlowFileFilter filter)

> 根据 FlowFileFilter 接口去获取 FlowFile 对象。

> 该方法是对当前 Processor 的上下游 Queue 进行独占访问。也就是说，在返回此方法调用之前，不允许其他线程从此 Processor 的队列中提取 FlowFiles 或将 FlowFiles 添加到此 Processor 的传入队列中。

### QueueSize getQueueSize()

> 获取当前 Processor 待处理的 FlowFile 个数以及总大小。

### FlowFile create()/FlowFile create(FlowFile parent)/FlowFile create(Collection<FlowFile> parents)

> 无参方法会返回一个没有 content 和 attributes 的新的 FlowFile 对象，这个方法建议是从外部系统中获取数据时创建 FlowFile 对象时调用。

> 参数为 FlowFile 的方法，会生成 parent 的子 FlowFile 对象，该对象继承除 UUID 外 parent 的所有 attributes, 且 content 为空。

> 参数为 Collection 的方法，会生成 parents 的子 FlowFile 对象，该对象继承了除 UUID 外 parents 的公共 attributes，且 content 为空。

### FlowFile clone(FlowFile example)/FlowFile clone(FlowFile parent, long offset, long size)

> clone example 对象的 content 和 attributes，生成一个新的 FlowFile 对象。

> clone example 对象的 attribute 和 content 从 offset 开始的长度为 size 的部分内容

> 这个方法会抛出如下异常：（1）当此方法是从 ProcessSession 对象的其他方法的 callback 接口（比如 write(FlowFile, OutputStreamCallback)）中被调用时，则会抛出**IllegalStateException**；（2）当 example 对象已经被 transfer 或者被 removed 或者不属于当前 ProcessSession 对象时，ProcessSession 会自动 rollback，同时抛出**FlowFileHandlingException**；（3）如果 example 对象的 content 找不到，则 example 对象会被自动销毁，ProcessSession 自动回滚，并抛出**MissingFlowFileException**；（4）当读取 example 的 content 发生 IO 异常时，抛出**FlowFileAccessException**；（5）example 为空对象时，抛出**NullPointerException**。

### FlowFile penalize(FlowFile flowFile)

> 在 penalty 周期内，不允许对 FlowFile 有任何操作。

### FlowFile putAttribute(FlowFile flowFile, String key, String value) / FlowFile putAllAttributes(FlowFile flowFile, Map<String, String> attributes)

> 给 flowFile 对象添加 attribute 或者 attribute 集合， **key=UUID 时会被忽略**。

> 该方法会抛出以下异常：（1）当此方法是从 ProcessSession 对象的其他方法的 callback 接口（比如 write(FlowFile, OutputStreamCallback)）中被调用时，则会抛出**IllegalStateException**；（2）当 flowFile 对象已经被 transfer 或者被 removed 或者不属于当前 ProcessSession 对象时，ProcessSession 会自动 rollback，同时抛出**FlowFileHandlingException**；（3）flowFile 为空对象时，抛出**NullPointerException**。

### FlowFile removeAttribute(FlowFile flowFile, String key) / FlowFile removeAllAttributes(FlowFile flowFile, Set<String> keys) / FlowFile removeAllAttributes(FlowFile flowFile, Pattern keyPattern)

> 给 flowFile 对象删除 attribute 或者 attribute 集合 或者满足 keyPattern 的 attribute。

> 该方法会抛出以下异常：（1）当此方法是从 ProcessSession 对象的其他方法的 callback 接口（比如 write(FlowFile, OutputStreamCallback)）中被调用时，则会抛出**IllegalStateException**；（2）当 flowFile 对象已经被 transfer 或者被 removed 或者不属于当前 ProcessSession 对象时，ProcessSession 会自动 rollback，同时抛出**FlowFileHandlingException**；（3）flowFile 为空对象时，抛出**NullPointerException**。

### void transfer(FlowFile flowFile, Relationship relationship) / void transfer(FlowFile flowFile) / void transfer(Collection<FlowFile> flowFiles) / void transfer(Collection<FlowFile> flowFiles, Relationship relationship)

> 将 FlowFile 对象传递到指定 Relationship 中，如果该 Relationship 对应下游多个 Processor，则下游的多个 Processor 处理的是不同的 FlowFile 对象，实际存储时，FlowFile 对象的 UUID 不同，但 content 的引用是相同的。

> 不带 Relationship 参数的方法，会将 FlowFile 转移到原先获取该 FlowFile 对象的队列中。**但是，Processor 自己创建的 FlowFile 对象是不能通过此方法转移给自身的！**

> （1）当此方法是从 ProcessSession 对象的其他方法的 callback 接口（比如 write(FlowFile, OutputStreamCallback)）中被调用时，则会抛出**IllegalStateException**；（2）当 flowFile 对象已经被 transfer 或者被 removed 或者不属于当前 ProcessSession 对象时，ProcessSession 会自动 rollback，同时抛出**FlowFileHandlingException**；（3）flowFile 为空对象时，抛出**NullPointerException**；（4）当 Relationship 未知或者未添加到 Processor 中或者该 FlowFile 对象是由当前 Processor 创建时，会抛出**IllegalArgumentException**。

### void remove(FlowFile flowFile) / void remove(Collection<FlowFile> flowFiles)

> flowFile 对象的 attributes 和 content 引用都会被清除。

> 该方法会抛出以下异常：（1）当此方法是从 ProcessSession 对象的其他方法的 callback 接口（比如 write(FlowFile, OutputStreamCallback)）中被调用时，则会抛出**IllegalStateException**；（2）当 flowFile 对象已经被 transfer 或者被 removed 或者不属于当前 ProcessSession 对象时，ProcessSession 会自动 rollback，同时抛出**FlowFileHandlingException**；

### void read(FlowFile source, InputStreamCallback reader) throws FlowFileAccessException / void read(FlowFile source, boolean allowSessionStreamManagement, InputStreamCallback reader) throws FlowFileAccessException

> 对 FlowFile 的 content 内容执行 InputStreamCallback 接口的读取操作，没有 allowSessionStreamManagement 参数或者值为 false 的情况下，操作完毕后会自动关闭文件句柄。出于性能的考虑，allowSessionStreamManagement 参数允许 ProcessSession 一直持有文件句柄。

> 这个方法会抛出如下异常：（1）当此方法是从 ProcessSession 对象的其他方法的 callback 接口（比如 write(FlowFile, OutputStreamCallback)）中被调用时，则会抛出**IllegalStateException**；（2）当 source 对象已经被 transfer 或者被 removed 或者不属于当前 ProcessSession 对象时，ProcessSession 会自动 rollback，同时抛出**FlowFileHandlingException**；（3）如果 source 对象的 content 找不到，则 source 对象会被自动销毁，ProcessSession 自动回滚，并抛出**MissingFlowFileException**；（4）当读取 source 的 content 发生 IO 异常时，抛出**FlowFileAccessException**；

### InputStream read(FlowFile flowFile)

> 和带有 callback 接口读取放大最大的区别在于：**调用者来管理 InputStream 的生命周期**。

> 这个方法会抛出如下异常：（1）当此方法是从 ProcessSession 对象的其他方法的 callback 接口（比如 write(FlowFile, OutputStreamCallback)）中被调用时，则会抛出**IllegalStateException**；（2）当 flowFile 对象已经被 transfer 或者被 removed 或者不属于当前 ProcessSession 对象时，ProcessSession 会自动 rollback，同时抛出**FlowFileHandlingException**；（3）如果 flowFile 对象的 content 找不到，则 source 对象会被自动销毁，ProcessSession 自动回滚，并抛出**MissingFlowFileException**。

### FlowFile merge(Collection<FlowFile> sources, FlowFile destination) / FlowFile merge(Collection<FlowFile> sources, FlowFile destination, byte[] header, byte[] footer, byte[] demarcator)

> 将 sources 中所有内容 merge 到 destination 中。

> header 将被写到 destination 的首部，footer 将被写到 destination 的结尾，demarcator 将被写到 sources 的分割处。header/footer/demarcator 都可以为空/null。

> 这个方法会抛出如下异常：（1）当此方法是从 ProcessSession 对象的其他方法的 callback 接口（比如 write(FlowFile, OutputStreamCallback)）中被调用时，则会抛出**IllegalStateException**；（2）当 source 对象已经被 transfer 或者被 removed 或者不属于当前 ProcessSession 对象时，ProcessSession 会自动 rollback，同时抛出**FlowFileHandlingException**；（3）如果 source 对象的 content 找不到，则 source 对象会被自动销毁，ProcessSession 自动回滚，并抛出**MissingFlowFileException**；（4）当读取 source 的 content 发生 IO 异常时，抛出**FlowFileAccessException**；（5）如果 destination 包含在 sources 集合中，则抛出**IllegalArgumentException**。

### FlowFile write(FlowFile source, OutputStreamCallback writer) throws FlowFileAccessException / FlowFile write(FlowFile source, StreamCallback writer) throws FlowFileAccessException

> 对 FlowFile 的 content 内容执行 OutputStreamCallback 接口的写入操作，操作完毕后会自动关闭文件句柄。

> 对 FlowFile 的 content 内容执行 StreamCallback 接口的写入/读取操作，操作完毕后会自动关闭文件句柄。

> 这个方法会抛出如下异常：（1）当此方法是从 ProcessSession 对象的其他方法的 callback 接口（比如 write(FlowFile, OutputStreamCallback)）中被调用时，则会抛出**IllegalStateException**；（2）当 source 对象已经被 transfer 或者被 removed 或者不属于当前 ProcessSession 对象时，ProcessSession 会自动 rollback，同时抛出**FlowFileHandlingException**；（3）如果 source 对象的 content 找不到，则 source 对象会被自动销毁，ProcessSession 自动回滚，并抛出**MissingFlowFileException**；（4）当读取 source 的 content 发生 IO 异常时，抛出**FlowFileAccessException**。

### OutputStream write(FlowFile source)

> 和带有 callback 接口读取放大最大的区别在于：**调用者来管理 OutputStream 的生命周期**。

> 这个方法会抛出如下异常：（1）当此方法是从 ProcessSession 对象的其他方法的 callback 接口（比如 write(FlowFile, OutputStreamCallback)）中被调用时，则会抛出**IllegalStateException**；（2）当 flowFile 对象已经被 transfer 或者被 removed 或者不属于当前 ProcessSession 对象时，ProcessSession 会自动 rollback，同时抛出**FlowFileHandlingException**；（3）如果 flowFile 对象的 content 找不到，则 source 对象会被自动销毁，ProcessSession 自动回滚，并抛出**MissingFlowFileException**。

### FlowFile append(FlowFile source, OutputStreamCallback writer) throws FlowFileAccessException

> 对 FlowFile 的 content 内容执行 OutputStreamCallback 接口的追加操作，追加操作会追加到 source 的 content 尾部。操作完毕后会自动关闭文件句柄。

> 这个方法会抛出如下异常：（1）当此方法是从 ProcessSession 对象的其他方法的 callback 接口（比如 write(FlowFile, OutputStreamCallback)）中被调用时，则会抛出**IllegalStateException**；（2）在此操作完成之前，想要从 OutputStreamCallback 获取 OutputStream，则会抛出**FlowFileAccessException**。

### FlowFile importFrom(Path source, boolean keepSourceFile, FlowFile destination) / FlowFile importFrom(InputStream source, FlowFile destination)

> 将 source 中的内容写入到 FlowFile 的 content 中，keepSourceFile 用于控制是否删除源文件。

> 这个方法会抛出如下异常：（1）当此方法是从 ProcessSession 对象的其他方法的 callback 接口（比如 write(FlowFile, OutputStreamCallback)）中被调用时，则会抛出**IllegalStateException**；（2）当 flowFile 对象已经被 transfer 或者被 removed 或者不属于当前 ProcessSession 对象时，ProcessSession 会自动 rollback，同时抛出**FlowFileHandlingException**；（3）如果 flowFile 对象的 content 找不到，则 flowFile 对象会被自动销毁，ProcessSession 自动回滚，并抛出**MissingFlowFileException**；（4）当读取 flowFile 的 content 发生 IO 异常时，抛出**FlowFileAccessException**。

### void exportTo(FlowFile flowFile, Path destination, boolean append) / void exportTo(FlowFile flowFile, OutputStream destination)

> 将 flowFile 的内容写到 destination 中。append 决定是以追加的模式还是以覆盖的模式。

> 这个方法会抛出如下异常：（1）当此方法是从 ProcessSession 对象的其他方法的 callback 接口（比如 write(FlowFile, OutputStreamCallback)）中被调用时，则会抛出**IllegalStateException**；（2）当 flowFile 对象已经被 transfer 或者被 removed 或者不属于当前 ProcessSession 对象时，ProcessSession 会自动 rollback，同时抛出**FlowFileHandlingException**；（3）如果 flowFile 对象的 content 找不到，则 flowFile 对象会被自动销毁，ProcessSession 自动回滚，并抛出**MissingFlowFileException**；（4）当读取 flowFile 的 content 发生 IO 异常时，抛出**FlowFileAccessException**。

### ProvenanceReporter getProvenanceReporter()

> 获得和 ProcessSession 相关的事件报告对象，用于生成 FlowFile 事件。