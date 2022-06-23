package com.github.superzhc.web.base.entity.animal;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author superz
 * @create 2022/6/22 15:12
 **/
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Dog extends Animal implements Serializable {
    private static final long serialVersionUID = 2389345272857602493L;

    private String attr1;
}
