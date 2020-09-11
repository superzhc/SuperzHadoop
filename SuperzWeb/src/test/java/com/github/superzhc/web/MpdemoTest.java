package com.github.superzhc.web;

import com.github.superzhc.web.mapper.MpdemoMapper;
import com.github.superzhc.web.model.Mpdemo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * 2020年09月10日 superz add
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MpdemoTest
{

    @Autowired
    private MpdemoMapper mpdemoMapper;

    @Test
    public void testSelect() {
        // 插入数据
        Mpdemo entity = new Mpdemo();
        entity.setCol("1");
        entity.setCol3(123.4);
        entity.setCol5("str");
        entity.setDt(new Date());
        entity.setT2(new Date());
        mpdemoMapper.insert(entity);

        // 读取数据
        int count = mpdemoMapper.selectCount(null);
        System.out.println(count);

        List<Mpdemo> lst = mpdemoMapper.selectList(null);
        lst.forEach(System.out::println);
    }
}
