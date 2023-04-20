package com.github.superzhc.common.exception;

/**
 * @author superz
 * @create 2023/4/19 9:36
 **/
public class EmptyValueException extends RuntimeException {
    private static final long serialVersionUID = -6528090680103562308L;

    public EmptyValueException() {
        super();
    }

    public EmptyValueException(String s) {
        super(s);
    }
}
