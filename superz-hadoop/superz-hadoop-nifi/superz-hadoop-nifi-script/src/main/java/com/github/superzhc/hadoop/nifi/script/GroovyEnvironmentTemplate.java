package com.github.superzhc.hadoop.nifi.script;

//==================================================================
/*ExecuteScript Processor Groovy引擎默认的依赖如下*/

import org.apache.nifi.components.*;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.*;
import org.apache.nifi.processor.exception.*;
import org.apache.nifi.processor.io.*;
import org.apache.nifi.processor.util.*;
import org.apache.nifi.processors.script.*;
import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.script.*;
import org.apache.nifi.record.sink.*;
import org.apache.nifi.lookup.*;

//==================================================================

/**
 * 继承该类只是将ExecuteScript提供的绑定对象封装过来了，子类直接使用，不需要重新在子类中进行定义
 */
public class GroovyEnvironmentTemplate {

    //=================================================================
    /*ExecuteScript Processor Groovy引擎注入的绑定项*/
    protected ProcessSession session;
    protected ProcessContext context;
    protected ComponentLog log;
    public static final Relationship REL_SUCCESS = new Relationship.Builder()
            .name("success")
            .description("FlowFiles that were successfully processed")
            .build();

    /**
     * A relationship indicating an error while processing flow files
     */
    public static final Relationship REL_FAILURE = new Relationship.Builder()
            .name("failure")
            .description("FlowFiles that failed to be processed")
            .build();
    //=================================================================

    // 参数构造函数会让子类重复引入依赖项，不够清晰
//    public GroovyEnvironmentTemplate(ProcessSession session, ProcessContext context, ComponentLog log) {
//        this.session = session;
//        this.context = context;
//        this.log = log;
//    }

    public void init(ProcessSession session, ProcessContext context, ComponentLog log) {
        this.session = session;
        this.context = context;
        this.log = log;
    }
}
