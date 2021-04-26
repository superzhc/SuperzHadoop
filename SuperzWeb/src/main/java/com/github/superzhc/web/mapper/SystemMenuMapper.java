package com.github.superzhc.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.superzhc.web.entity.SystemMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统菜单表 Mapper 接口
 * </p>
 *
 * @author superz
 * @since 2021-04-22
 */
public interface SystemMenuMapper extends BaseMapper<SystemMenu> {
    @Select("select a.id,a.pid,a.title,a.href,a.sort,(select count(*) from system_menu b where b.pid=a.id) as num from system_menu a where a.pid=#{pid} order by sort,create_time")
    List<Map<String, Object>> selectChildByPid(@Param("pid") Long pid);

    @Select("select count(*) from system_menu where pid=#{pid}")
    Long countByPid(@Param("pid") Long pid);
}
