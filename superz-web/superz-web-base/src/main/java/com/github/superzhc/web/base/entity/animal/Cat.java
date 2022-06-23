package com.github.superzhc.web.base.entity.animal;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author superz
 * @create 2022/6/22 15:14
 **/
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Cat extends Animal implements Serializable {
    private static final long serialVersionUID = -2170177767784675077L;

    private String attr2;
}
