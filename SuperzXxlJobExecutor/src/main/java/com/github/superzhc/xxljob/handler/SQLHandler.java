package com.github.superzhc.xxljob.handler;

import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @author superz
 * @create 2021/7/26 11:02
 */
@Component
public class SQLHandler {

    // @XxlJob("sqlPreview")
    public void preview() throws Exception {
        // 获取任务参数
        String param = XxlJobContext.getXxlJobContext().getJobParam();

    }
}
