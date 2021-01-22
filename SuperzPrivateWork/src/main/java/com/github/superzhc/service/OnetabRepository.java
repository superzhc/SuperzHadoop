package com.github.superzhc.service;

import com.github.superzhc.domain.Onetab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 编写一个Dao接口来操作实体类对应的数据表
 * JpaRepository 泛型1 被操作的实体类 泛型2 主键属性的类型
 * 2021年01月22日 superz add
 */
public interface OnetabRepository extends JpaRepository<Onetab,Integer> {
    List<Onetab> findAllByTitleContaining(String title);

    @Query(value = "select * from onetab where title like concat('%',?1,'%')",nativeQuery = true)
    List<Onetab> customFindByTitle(String title);
}
