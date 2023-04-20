package com.github.superzhc.common.base;

import com.github.superzhc.common.exception.EmptyValueException;

import javax.activation.UnsupportedDataTypeException;
import java.util.Collection;
import java.util.Map;

/**
 * 参考 guava 中的 {@class com.google.common.base.Preconditions}，并添加一些自定义工具
 *
 * @author superz
 * @create 2023/4/14 10:38
 **/
public final class Preconditions {
    private Preconditions() {
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @throws IllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression   a boolean expression
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @throws IllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression           a boolean expression
     * @param errorMessageTemplate a template for the exception message should the check fail. The
     *                             message is formed by replacing each {@code %s} placeholder in the template with an
     *                             argument. These are matched by position - the first {@code %s} gets {@code
     *                             errorMessageArgs[0]}, etc. Unmatched arguments will be appended to the formatted message in
     *                             square braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs     the arguments to be substituted into the message template. Arguments
     *                             are converted to strings using {@link String#valueOf(Object)}.
     * @throws IllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(
            boolean expression,
            String errorMessageTemplate,
            Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static <T> T checkNotEmpty(Object reference) {
        if (reference == null) {
            throw new NullPointerException();
        }

        if (reference instanceof String) {
            String s = (String) reference;
            if (s.trim().isEmpty()) {
                throw new EmptyValueException();
            }
        } else if (reference.getClass().isArray()) {
            Object[] array = (Object[]) reference;
            if (array.length == 0) {
                throw new EmptyValueException();
            }
        } else if (reference instanceof Collection) {
            Collection<?> c = (Collection<?>) reference;
            if (c.isEmpty()) {
                throw new EmptyValueException();
            }
        } else if (reference instanceof Map) {
            Map<?, ?> m = (Map<?, ?>) reference;
            if (m.isEmpty()) {
                throw new EmptyValueException();
            }
        } else {
            throw new UnsupportedOperationException("当前类型[" + reference.getClass().getName() + "]不支持检查是否为Empty");
        }

        return (T) reference;
    }

    public static <T> T checkNotEmpty(Object reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }

        if (reference instanceof String) {
            String s = (String) reference;
            if (s.trim().isEmpty()) {
                throw new EmptyValueException(String.valueOf(errorMessage));
            }
        } else if (reference.getClass().isArray()) {
            Object[] array = (Object[]) reference;
            if (array.length == 0) {
                throw new EmptyValueException(String.valueOf(errorMessage));
            }
        } else if (reference instanceof Collection) {
            Collection<?> c = (Collection<?>) reference;
            if (c.isEmpty()) {
                throw new EmptyValueException(String.valueOf(errorMessage));
            }
        } else if (reference instanceof Map) {
            Map<?, ?> m = (Map<?, ?>) reference;
            if (m.isEmpty()) {
                throw new EmptyValueException(String.valueOf(errorMessage));
            }
        } else {
            throw new UnsupportedOperationException("当前类型[" + reference.getClass().getName() + "]不支持检查是否为Empty");
        }

        return (T) reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference    an object reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference            an object reference
     * @param errorMessageTemplate a template for the exception message should the check fail. The
     *                             message is formed by replacing each {@code %s} placeholder in the template with an
     *                             argument. These are matched by position - the first {@code %s} gets {@code
     *                             errorMessageArgs[0]}, etc. Unmatched arguments will be appended to the formatted message in
     *                             square braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs     the arguments to be substituted into the message template. Arguments
     *                             are converted to strings using {@link String#valueOf(Object)}.
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(
            T reference,
            String errorMessageTemplate,
            Object... errorMessageArgs) {
        if (reference == null) {
            throw new NullPointerException(String.format(errorMessageTemplate, errorMessageArgs));
        }
        return reference;
    }

}
