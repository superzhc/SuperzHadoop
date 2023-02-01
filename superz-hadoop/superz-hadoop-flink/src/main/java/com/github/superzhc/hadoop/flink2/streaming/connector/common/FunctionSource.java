package com.github.superzhc.hadoop.flink2.streaming.connector.common;

import com.github.superzhc.common.utils.ReflectionUtils;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author superz
 * @create 2023/2/1 22:37
 */
public class FunctionSource<T> extends RichSourceFunction<T>
        implements ResultTypeQueryable<T> /*实现ResultTypeQueryable是因为RichSourceFunction不能直接为泛型需要指定返回类型*/ {

    private static final Logger LOG = LoggerFactory.getLogger(FunctionSource.class);

    private static final boolean IS_STATIC_METHOD = true;
    private static final boolean IS_NOT_STATIC_METHOD = false;

    private static final Object[] EMPTY_ARGS = null;

    private volatile boolean cancelled = false;

    private Class<?> clazz;

    private Object[] constructorArgs;

    private boolean isStaticMethod;

    private String methodName;

    private Object[] methodArgs;

    /*调用函数的周期，单位为秒*/
    private long period;

    public FunctionSource(Class<?> clazz, Object[] constructorArgs, boolean isStaticMethod, String methodName, Object[] methodArgs, long period) {
        this.clazz = clazz;
        this.isStaticMethod = isStaticMethod;
        this.constructorArgs = constructorArgs;
        this.methodName = methodName;
        this.methodArgs = methodArgs;
        this.period = period;
    }

    public static <K> FunctionSource<K> staticFunctionSource(Class<?> clazz, String methodName, long period) {
        return staticFunctionSource(clazz, methodName, EMPTY_ARGS, period);
    }

    public static <K> FunctionSource<K> staticFunctionSource(Class<?> clazz, String methodName, Object[] methodArgs, long period) {
        return new FunctionSource<K>(clazz, EMPTY_ARGS, IS_STATIC_METHOD, methodName, methodArgs, period);
    }

    public static <K> FunctionSource<K> instanceFunctionSource(Class<?> clazz, String methodName, long period) {
        return instanceFunctionSource(clazz, EMPTY_ARGS, methodName, EMPTY_ARGS, period);
    }

    public static <K> FunctionSource<K> instanceFunctionSource(Class<?> clazz, Object[] constructorArgs, String methodName, long period) {
        return instanceFunctionSource(clazz, constructorArgs, methodName, EMPTY_ARGS, period);
    }

    public static <K> FunctionSource<K> instanceFunctionSource(Class<?> clazz, String methodName, Object[] methodArgs, long period) {
        return instanceFunctionSource(clazz, EMPTY_ARGS, methodName, methodArgs, period);
    }

    public static <K> FunctionSource<K> instanceFunctionSource(Class<?> clazz, Object[] constructorArgs, String methodName, Object[] methodArgs, long period) {
        return new FunctionSource<K>(clazz, constructorArgs, IS_NOT_STATIC_METHOD, methodName, methodArgs, period);
    }

    @Override
    public void run(SourceContext<T> sourceContext) throws Exception {
        long nextReadTime = System.currentTimeMillis();
        while (!cancelled) {
            T data;
            if (isStaticMethod) {
                data = ReflectionUtils.invokeStaticMethod(clazz, methodName, methodArgs);
            } else {
                data = ReflectionUtils.invokeMethod(clazz, constructorArgs, methodName, methodArgs);
            }
            sourceContext.collect(data);

            nextReadTime += period * 1000;
            long toWaitMs = Math.max(0, nextReadTime - System.currentTimeMillis());
            Thread.sleep(toWaitMs);
        }
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    @Override
    public TypeInformation<T> getProducedType() {
        return TypeInformation.of(ReflectionUtils.methodReturnType(clazz, methodName, methodArgs));
    }
}
