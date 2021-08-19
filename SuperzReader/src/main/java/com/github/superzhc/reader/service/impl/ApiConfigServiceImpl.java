package com.github.superzhc.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.superzhc.reader.entity.ApiConfig;
import com.github.superzhc.reader.mapper.ApiConfigMapper;
import com.github.superzhc.reader.service.ApiConfigService;
import org.springframework.stereotype.Service;

/**
 * @author superz
 * @create 2021/8/18 16:50
 */
@Service
public class ApiConfigServiceImpl extends ServiceImpl<ApiConfigMapper, ApiConfig> implements ApiConfigService {

    @Override
    public ApiConfig getByPath(String path) {
        return baseMapper.selectByPath(path);
    }

    @Override
    public boolean exist(String path) {
        QueryWrapper<ApiConfig> query = new QueryWrapper<>();
        query.eq("path", path);
        Integer count = baseMapper.selectCount(query);
        return count > 0;
    }
}
