package com.github.superzhc.hadoop.nifi.processors;

import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.expression.ExpressionLanguageScope;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.util.StandardValidators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author superz
 * @create 2023/5/15 14:25
 **/
@Tags("sueprz,demo")
@CapabilityDescription("我的第一个自定义Processor")
public class MyProcessor extends AbstractProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(MyProcessor.class);
    /*自定义Processor属性*/
    public static final PropertyDescriptor MY_PROPERTY = new PropertyDescriptor.Builder()
            .name("标识")
            .description("自定义标识")
            .expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES)
            .required(true)
            .defaultValue("superz")
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    @Override
    protected void init(ProcessorInitializationContext context) {
        LOG.info("初始化");
    }

    @Override
    public void onTrigger(ProcessContext processContext, ProcessSession processSession) throws ProcessException {
        FlowFile flowFile = processSession.get();
        if (null == flowFile) {
            return;
        }

    }
}
