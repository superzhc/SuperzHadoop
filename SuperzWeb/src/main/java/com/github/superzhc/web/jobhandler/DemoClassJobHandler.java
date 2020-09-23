package com.github.superzhc.web.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;

/**
 * 2020年09月23日 superz add
 */
public class DemoClassJobHandler extends IJobHandler
{
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        System.out.println("hello demo class jobhandler");
        return null;
    }
}
