package com.github.superzhc.reader.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.superzhc.reader.entity.ApiConfig;

/**
 * @author superz
 * @create 2021/8/18 16:49
 */
public interface ApiConfigService extends IService<ApiConfig> {
    ApiConfig getByPath(String path);

    /**
     * 判断数据服务地址是否已经存在
     * @param path
     * @return
     */
    boolean exist(String path);
}
