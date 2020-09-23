package com.github.superzhc.web.service.impl;

import com.github.superzhc.web.model.Book;
import com.github.superzhc.web.mapper.BookMapper;
import com.github.superzhc.web.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author superz
 * @since 2020-09-11
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

}
