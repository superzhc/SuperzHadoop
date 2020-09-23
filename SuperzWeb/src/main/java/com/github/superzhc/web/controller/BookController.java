package com.github.superzhc.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.superzhc.web.model.Book;
import com.github.superzhc.web.service.BookService;
import com.github.superzhc.web.utils.LayuiUtils;
import com.github.superzhc.web.utils.Result;

/**
 * 2020年09月06日 superz add
 */
@RestController
@RequestMapping("/book")
public class BookController
{
    @Autowired
    // BookMapper bookMapper;
    BookService service;

    @GetMapping("/list")
    public Result list(@RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer limit,
            @RequestParam(required = false) String searchParams) {

        Map<String, Object> params = null;
        if (null == searchParams || "".equals(searchParams))
            params = new HashMap<>();
        else
            params = JSON.parseObject(searchParams).getInnerMap();

        QueryWrapper query = new QueryWrapper();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if ("name".equalsIgnoreCase(entry.getKey()))
                query.like(entry.getKey(), entry.getValue());
            else
                query.eq(entry.getKey(), entry.getValue());
        }

        Page<Book> pageplus = new Page<>(page, limit);
        IPage<Book> pageinfo = service.page(pageplus, query);
        return LayuiUtils.table_ok(pageinfo);

        // 分页查询
        // PageHelper.startPage(page, limit);
        // List<Book> books = bookMapper.selectByCondition(params);
        // PageInfo<Book> pageInfo = new PageInfo<Book>(books);
        // return LayuiUtils.table_ok(pageInfo);
    }

    @GetMapping("/view/{bookId}")
    public Result view(@PathVariable String bookId) {
        Book book = service.getById(bookId);
        return LayuiUtils.data(book);
    }
}
