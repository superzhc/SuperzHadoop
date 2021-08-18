package com.github.superzhc.reader.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.superzhc.reader.entity.ApiConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author superz
 * @create 2021/8/18 16:49
 */
@Mapper
public interface ApiConfigMapper extends BaseMapper<ApiConfig> {
    @Select("select * from api_config where path=#{path}")
    ApiConfig selectByPath(@Param("path") String path);
}
