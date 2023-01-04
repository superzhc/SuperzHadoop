package com.github.superzhc.spring.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.superzhc.spring.web.animal.Cat;
import com.github.superzhc.spring.web.animal.Dog;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author superz
 * @create 2022/6/22 15:17
 **/
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DemoDTO implements Serializable {
    private static final long serialVersionUID = 3120371782659124008L;

    private Integer id;
    private String text;

    /* 这种接收参数只会转换成 Animal，无法自动识别成 Dog、Cat 类型 */
    // private List<? extends Animal> animals;

    private List<Dog> dogs;
    private List<Cat> cats;
}
