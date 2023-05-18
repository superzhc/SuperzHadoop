package com.github.superzhc.hadoop.nifi.processors.lc;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.expression.ExpressionLanguageScope;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.util.StandardValidators;

import java.util.List;

/**
 * @author superz
 * @create 2023/5/16 9:27
 **/
public abstract class AbstractLCProcessor extends AbstractProcessor {
    public static final PropertyDescriptor INPUT_PROPERTY = new PropertyDescriptor.Builder()
            .name("输入")
            .description("输入是数组、列表，使用逗号进行分割")
            .expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES)
            .required(true)
            .defaultValue("")
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    protected List<PropertyDescriptor> descriptors;

    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }
}
