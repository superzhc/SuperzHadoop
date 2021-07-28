package com.github.superzhc.xxljob.handler;

import com.beust.jcommander.JCommander;
import com.github.superzhc.xxljob.handler.param.KafkaParam;
import com.github.superzhc.xxljob.handler.util.CommonConsumer;
import com.github.superzhc.xxljob.util.CommandLineUtils;
import com.github.superzhc.xxljob.util.ProcessUtils;
import com.github.superzhc.xxljob.util.StrFormat;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author superz
 * @create 2021/7/27 17:23
 */
//@Component
public class KafkaHandler {
    /* 获取 kafka 主题列表 */
    public static final String KAFKA_TOPIC_LIST = "{KAFKA_HOME}/bin/kafka-topics.sh --bootstrap-server {bootstrap-server} --list";
    /* 获取 kafka 指定主题的列表 */
    public static final String KAFKA_TOPIC_DESCRIBE = "{KAFKA_HOME}/bin/kafka-topics.sh --bootstrap-server {bootstrap-server} --describe --topic {topic}";

    @XxlJob("kafkaInfos")
    public void execute() throws Exception {
        String param = XxlJobHelper.getJobParam();
        String[] args = CommandLineUtils.translateCommandline(param);

        KafkaParam kafkaParam = new KafkaParam();
        JCommander.newBuilder().addObject(kafkaParam).build().parse(args);

        String cmd;
        if (kafkaParam.isList()) {
            cmd = StrFormat.format(KAFKA_TOPIC_LIST, kafkaParam);
        } else if (StringUtils.isBlank(kafkaParam.getTopic())) {
            XxlJobHelper.handleFail("参数-t(--topic)不能为空");
            return;
        } else {
            cmd = StrFormat.format(KAFKA_TOPIC_DESCRIBE, kafkaParam);
        }

        int code = ProcessUtils.execCommand(cmd, new CommonConsumer());
        XxlJobHelper.log("code={}", code);
    }

}
