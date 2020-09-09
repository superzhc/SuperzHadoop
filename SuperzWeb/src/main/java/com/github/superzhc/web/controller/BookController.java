package com.github.superzhc.web.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.superzhc.web.mapper.BookMapper;
import com.github.superzhc.web.model.Book;
import com.github.superzhc.web.utils.LayuiUtils;
import com.github.superzhc.web.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2020年09月06日 superz add
 */
@RestController
@RequestMapping("/book")
public class BookController
{
    @Autowired
    BookMapper bookMapper;

    @GetMapping("/list")
    public Result list(@RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer limit,
            @RequestParam(required = false) String searchParams) {

        Map<String, Object> params = null;
        if (null == searchParams || "".equals(searchParams))
            params = new HashMap<>();
        else
            params = JSON.parseObject(searchParams).getInnerMap();

        // 分页查询
        PageHelper.startPage(page, limit);
        List<Book> books = bookMapper.selectByCondition(params);
        PageInfo<Book> pageInfo = new PageInfo<Book>(books);
        return LayuiUtils.table_ok(pageInfo);
    }

    @GetMapping("/view/{bookId}")
    public Result view(@PathVariable String bookId) {
        Book book = bookMapper.selectByPrimaryKey(bookId);
        return LayuiUtils.model_ok(book);
    }
}
