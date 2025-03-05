# Source

## 示例代码

**`SourceConfig`**

```java
import lombok.Getter;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBSourceConfig implements Serializable {

    @Getter
    private String driver = null;
    @Getter
    private String url;
    @Getter
    private String username = null;
    @Getter
    private String password = null;
    @Getter
    private String query;

    public DBSourceConfig() {
    }

    public DBSourceConfig(String driver, String url, String username, String password, String query) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.query = query;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public DBSourceConfig withDriver(String driver) {
        this.driver = driver;
        return this;
    }

    public DBSourceConfig withUrl(String url) {
        this.url = url;
        return this;
    }

    public DBSourceConfig withUsername(String username) {
        this.username = username;
        return this;
    }

    public DBSourceConfig withPassword(String password) {
        this.password = password;
        return this;
    }

    public DBSourceConfig withQuery(String query) {
        this.query = query;
        return this;
    }
}
```

**`Source`**

```java
import com.flink.streaming.visualization.interpreter.connectors.db.checkpoint.DBCheckpoint;
import com.flink.streaming.visualization.interpreter.connectors.db.checkpoint.DBCheckpointSerializer;
import com.flink.streaming.visualization.interpreter.connectors.db.reader.DBSourceReader;
import com.flink.streaming.visualization.interpreter.connectors.db.split.DBSplit;
import com.flink.streaming.visualization.interpreter.connectors.db.split.DBSplitEnumerator;
import com.flink.streaming.visualization.interpreter.connectors.db.split.DBSplitSerializer;
import org.apache.flink.api.connector.source.*;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.types.Row;

public class DBSource implements Source<Row, DBSplit, DBCheckpoint> {

    private final DBSourceConfig config;

    public DBSource(DBSourceConfig config) {
        this.config = config;
    }

    /**
     * 定义数据源的流批性
     * @return
     */
    @Override
    public Boundedness getBoundedness() {
        return Boundedness.BOUNDED;
    }

    @Override
    public SourceReader<Row, DBSplit> createReader(SourceReaderContext readerContext) throws Exception {
        return new DBSourceReader(config);
    }

    @Override
    public SplitEnumerator<DBSplit, DBCheckpoint> createEnumerator(SplitEnumeratorContext<DBSplit> enumContext) throws Exception {
        return new DBSplitEnumerator(enumContext);
    }

    @Override
    public SplitEnumerator<DBSplit, DBCheckpoint> restoreEnumerator(SplitEnumeratorContext<DBSplit> enumContext, DBCheckpoint checkpoint) throws Exception {
        return new DBSplitEnumerator(enumContext);
    }

    @Override
    public SimpleVersionedSerializer<DBSplit> getSplitSerializer() {
        return new DBSplitSerializer();
    }

    @Override
    public SimpleVersionedSerializer<DBCheckpoint> getEnumeratorCheckpointSerializer() {
        return new DBCheckpointSerializer();
    }
}
```

**`Checkpoint`**

```java
import java.io.Serializable;

public class DBCheckpoint implements Serializable {
}
```

**`Split`**

```java
import org.apache.flink.api.connector.source.SourceSplit;

import java.io.Serializable;
import java.time.Instant;

/**
 * 分片是对一部分 source 数据的包装，如一个文件或者日志分区。分片是 source 进行任务分配和数据并行读取的基本粒度。
 */
public class DBSplit implements SourceSplit, Serializable {
    private static final long serialVersionUID = 1L;

    private final Instant timestamp = Instant.now();

    @Override
    public String splitId() {
        return timestamp.toString();
    }
}
```

**`SplitEnumerator`**

```java
import com.flink.streaming.visualization.interpreter.connectors.db.checkpoint.DBCheckpoint;
import org.apache.flink.api.connector.source.SplitEnumerator;
import org.apache.flink.api.connector.source.SplitEnumeratorContext;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

/**
 * 分片枚举器会生成分片并将它们分配给 SourceReader。该组件在 JobManager 上以单并行度运行，负责对未分配的分片进行维护，并以均衡的方式将其分配给 reader。
 */
public class DBSplitEnumerator implements SplitEnumerator<DBSplit, DBCheckpoint> {

    private final SplitEnumeratorContext<DBSplit> context;

    private int readerIndex = -1;

    public DBSplitEnumerator(SplitEnumeratorContext<DBSplit> context){
        this.context = context;
    }

    @Override
    public void start() {
        context.callAsync(this::fetchSplit,(split,error)->{
            if (readerIndex >= 0){
                context.assignSplit(split, readerIndex);
            }
        });
    }

    @Override
    public void handleSplitRequest(int subtaskId, @Nullable String requesterHostname) {

    }

    /**
     * SourceReader 失败时会调用 addSplitsBack() 方法。SplitEnumerator应当收回已经被分配，但尚未被该 SourceReader 确认（acknowledged）的分片
     * @param splits The split to add back to the enumerator for reassignment.
     * @param subtaskId The id of the subtask to which the returned splits belong.
     */
    @Override
    public void addSplitsBack(List<DBSplit> splits, int subtaskId) {

    }

    @Override
    public void addReader(int subtaskId) {
        readerIndex=subtaskId;
    }

    @Override
    public DBCheckpoint snapshotState(long l) throws Exception {
        return new DBCheckpoint();
    }

    @Override
    public void close() throws IOException {

    }

    private DBSplit fetchSplit() {
        return new DBSplit();
    }
}
```

**`SourceReader`**

```java
import com.flink.streaming.visualization.interpreter.connectors.db.DBSourceConfig;
import com.flink.streaming.visualization.interpreter.connectors.db.split.DBSplit;
import org.apache.flink.api.connector.source.ReaderOutput;
import org.apache.flink.api.connector.source.SourceReader;
import org.apache.flink.core.io.InputStatus;
import org.apache.flink.types.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 源阅读器会请求分片并进行处理，例如读取分片所表示的文件或日志分区。
 * SourceReader 在 TaskManagers 上的 SourceOperators 并行运行，并产生并行的事件流/记录流。
 */
public class DBSourceReader implements SourceReader<Row, DBSplit> {

    private final Logger LOG = LoggerFactory.getLogger(DBSourceReader.class);
    private final DBSourceConfig config;

    public DBSourceReader(DBSourceConfig config) {
        this.config = config;
    }

    @Override
    public void start() {

    }

    /**
     * SourceReader 提供了一个拉动式（pull-based）处理接口
     * Flink 任务会在循环中不断调用 pollNext(ReaderOutput) 轮询来自 SourceReader 的记录
     * pollNext(ReaderOutput) 方法的返回值指示 SourceReader 的状态
     * - MORE_AVAILABLE - SourceReader 有可用的记录。
     * - NOTHING_AVAILABLE - SourceReader 现在没有可用的记录，但是将来可能会有记录可用。
     * - END_OF_INPUT - SourceReader 已经处理完所有记录，到达数据的尾部。这意味着 SourceReader 可以终止任务了
     * @param readerOutput
     * @return
     * @throws Exception
     */
    @Override
    public InputStatus pollNext(ReaderOutput<Row> readerOutput) throws Exception {
        return null;
    }

    @Override
    public List<DBSplit> snapshotState(long l) {
        return List.of();
    }

    @Override
    public CompletableFuture<Void> isAvailable() {
        return null;
    }

    @Override
    public void addSplits(List<DBSplit> splits) {
        if (splits.size() > 0) {
            try(Connection connection=config.getConnection()){
                try(Statement stmt=connection.createStatement()){
                    try(ResultSet rs=stmt.executeQuery(config.getQuery())){
                        // TODO 获取数据集的元数据信息
                        while (rs.next()){
                            // 处理数据
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void notifyNoMoreSplits() {

    }

    @Override
    public void close() throws Exception {

    }
}
```

**`SimpleVersionedSerializer`**

```java
import org.apache.commons.lang3.SerializationUtils;
import org.apache.flink.core.io.SimpleVersionedSerializer;

import java.io.IOException;

public class DBSplitSerializer implements SimpleVersionedSerializer<DBSplit> {
    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public byte[] serialize(DBSplit dbSplit) throws IOException {
        return SerializationUtils.serialize(dbSplit);
    }

    @Override
    public DBSplit deserialize(int version, byte[] bytes) throws IOException {
        if (version != getVersion())
            throw new IOException(String.format("Version mismatch, expected %d, actual %d", getVersion(), version));

        return SerializationUtils.deserialize(bytes);
    }
}
```