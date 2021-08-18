package com.github.superzhc.reader.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.superzhc.reader.entity.DatasourceConfig;
import com.github.superzhc.reader.mapper.DatasourceConfigMapper;
import com.github.superzhc.reader.service.DatasourceConfigService;
import org.springframework.stereotype.Service;

/**
 * @author superz
 * @create 2021/8/17 20:44
 */
@Service
public class DatasourceConfigServiceImpl extends ServiceImpl<DatasourceConfigMapper, DatasourceConfig> implements DatasourceConfigService {
}
