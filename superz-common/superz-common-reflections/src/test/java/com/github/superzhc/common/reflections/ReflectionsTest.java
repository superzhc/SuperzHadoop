package com.github.superzhc.common.reflections;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static org.reflections.ReflectionUtils.*;

public class ReflectionsTest {

    public void test() {
        Reflections reflections = new Reflections("com.github.superzhc");

        // 获取子包
        Set<Class<?>> subTypes = reflections.getSubTypesOf(Object.class);

        // 获取指定注解的类
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Deprecated.class);

        //ResourcesScanner
        Set<String> properties =
                reflections.getResources(Pattern.compile(".*\\.properties"));

        //MethodAnnotationsScanner
        Set<Method> resources =
                reflections.getMethodsAnnotatedWith(Deprecated.class);
        Set<Constructor> injectables =
                reflections.getConstructorsAnnotatedWith(Deprecated.class);

        //FieldAnnotationsScanner
        Set<Field> ids =
                reflections.getFieldsAnnotatedWith(Deprecated.class);

        //MethodParameterScanner
        Set<Method> someMethods =
                reflections.getMethodsMatchParams(long.class, int.class);
        Set<Method> voidMethods =
                reflections.getMethodsReturn(void.class);
//        Set<Method> pathParamMethods =
//                reflections.getMethodsWithAnyParamAnnotated(PathParam.class);

//        //MethodParameterNamesScanner
//        List<String> parameterNames =
//                reflections.getMethodParamNames(Method.class)
    }

    public void test2() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com.github.superzhc"))
                .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
                .filterInputsBy(new FilterBuilder().includePackage("com.github.superzhc"))
        );
    }

    public void test3() {
        Set<Method> getters = getAllMethods(Object.class,
                withModifier(Modifier.PUBLIC), withPrefix("get"), withParametersCount(0));

        //or
        Set<Method> listMethodsFromCollectionToBoolean =
                getAllMethods(List.class,
                        withParametersAssignableTo(Collection.class), withReturnType(boolean.class));

//        Set<Field> fields = getAllFields(Object.class, withAnnotation(Deprecated.class), withTypeAssignableTo(type));
    }
}
