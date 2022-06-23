package com.github.superzhc.web.base.entity.animal;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author superz
 * @create 2022/6/22 15:11
 **/
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Animal implements Serializable {
    private static final long serialVersionUID = -2523585063947024493L;

    private String name;
}
