package com.github.superzhc.xxljob.handler;

import com.github.superzhc.xxljob.handler.util.CommonConsumer;
import com.github.superzhc.xxljob.util.ProcessUtils;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @author superz
 * @create 2021/7/27 19:22
 */
@Component
public class CommnadHandler {
    @XxlJob("cmd")
    public void executor() throws Exception{
        String param= XxlJobHelper.getJobParam();
        int exitValue=ProcessUtils.execCommand(param,new CommonConsumer());
        if (exitValue == 0) {
            XxlJobHelper.handleSuccess();
        } else {
            XxlJobHelper.handleFail("command exit value("+exitValue+") is failed");
        }
    }
}
