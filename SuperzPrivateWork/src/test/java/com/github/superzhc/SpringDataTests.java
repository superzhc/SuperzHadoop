package com.github.superzhc;

import com.github.superzhc.domain.Onetab;
import com.github.superzhc.service.OnetabRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 2021年01月22日 superz add
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringPrivateworkApplication.class })
public class SpringDataTests
{
    @Autowired
    private OnetabRepository onetabRepository;

    @Test
    public void query() {
        Onetab onetab = onetabRepository.findById(1).get();
        System.out.println(onetab.getTitle());
        System.out.println(onetab);
    }

    @Test
    public void likeQuery(){
        List<Onetab> lst=onetabRepository.findAllByTitleContaining("hive");
        System.out.println(lst.size());
        List<Onetab> customLst=onetabRepository.customFindByTitle("hive");
        System.out.println(customLst.size());
    }
}
