package com.github.superzhc.hadoop.nifi.processors;

import org.apache.nifi.annotation.behavior.InputRequirement;
import org.apache.nifi.annotation.behavior.SideEffectFree;
import org.apache.nifi.annotation.behavior.SupportsBatching;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnAdded;
import org.apache.nifi.annotation.lifecycle.OnRemoved;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.annotation.lifecycle.OnUnscheduled;
import org.apache.nifi.components.AllowableValue;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.expression.ExpressionLanguageScope;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.*;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.util.StandardValidators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author superz
 * @create 2023/5/15 14:25
 **/
//// 不需要关注上下文
//@SideEffectFree
//// 支持批量
//@SupportsBatching
// 用于标记这个Processor的标签，可以用于搜索
@Tags("sueprz,demo")
//// 声明允许输入
//@InputRequirement(InputRequirement.Requirement.INPUT_ALLOWED)
// Processor详细描述
@CapabilityDescription("我的第一个自定义Processor")
public class MyProcessor extends AbstractProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(MyProcessor.class);

    /*自定义Processor属性*/
    public static final PropertyDescriptor MY_PROPERTY = new PropertyDescriptor.Builder()
            .name("文本值")
            .description("文本输入")
            .expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES)
            .required(true)
            .defaultValue("")
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    // 属性选项值定义
    public static final AllowableValue V1 = new AllowableValue("0", "v1", "option value:value1");
    public static final AllowableValue V2 = new AllowableValue("1", "v2", "option value:value2");
    // 配置选项值
    public static final PropertyDescriptor MY_OPTION_PROPERTY = new PropertyDescriptor.Builder()
            .name("下拉选择")
            .description("下拉框选择值")
            .required(true)
            .allowableValues(V1, V2)
            .defaultValue(V1.getValue())
            .build();

    /*自定义Processor关系*/
    public static final Relationship SUCCESS = new Relationship.Builder()
            .name("success")
            .build();

    private List<PropertyDescriptor> descriptors;
    private Set<Relationship> relationships;

    @Override
    protected void init(ProcessorInitializationContext context) {
        final List<PropertyDescriptor> descriptors = new ArrayList<>();
        descriptors.add(MY_PROPERTY);
        descriptors.add(MY_OPTION_PROPERTY);
        this.descriptors = Collections.unmodifiableList(descriptors);

        final Set<Relationship> relationships = new HashSet<>();
        relationships.add(SUCCESS);
        this.relationships = Collections.unmodifiableSet(relationships);
    }

    /**
     * Processor 支持的属性
     *
     * @return
     */
    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }

    @Override
    public Set<Relationship> getRelationships() {
        return relationships;
    }

    /**
     * 动态属性
     *
     * @param propertyDescriptorName used to lookup if any property descriptors exist for that name
     * @return
     */
    @Override
    protected PropertyDescriptor getSupportedDynamicPropertyDescriptor(String propertyDescriptorName) {
        return new PropertyDescriptor.Builder()
                .name(propertyDescriptorName)
                .expressionLanguageSupported(ExpressionLanguageScope.NONE)
                // 属性验证
//                .addValidator(new XPathValidator())
                .required(false)
                .dynamic(true)
                .build();
    }

    @OnAdded
    public void addProcessor() {
    }

    @OnRemoved
    public void removeProcessor() {
    }

    /**
     * 主要是用于Processor的一些一次性工作，比如初始化连接等。所以，用户应该将资源的初始化工作放在@onScheduled注解修饰的方法中
     */
    @OnScheduled
    public void setUp() {
        LOG.info("当前Processor触发运行时间：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
    }

    @OnUnscheduled
    public void tearDown() {
        LOG.info("当前Processor运行结束时间：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
    }

    @Override
    public void onTrigger(ProcessContext processContext, ProcessSession processSession) throws ProcessException {
        FlowFile flowFile = processSession.get();

        // TODO：打印数据
        // 读取数据
        try (InputStream in = processSession.read(flowFile)) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 直接将原来的数据给转发出去
        processSession.transfer(flowFile, SUCCESS);
    }
}
