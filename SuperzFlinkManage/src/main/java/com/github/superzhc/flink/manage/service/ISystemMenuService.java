package com.github.superzhc.flink.manage.service;

import com.github.superzhc.flink.manage.entity.SystemMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.superzhc.flink.manage.entity.vo.MenuVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统菜单表 服务类
 * </p>
 *
 * @author superz
 * @since 2021-04-22
 */
public interface ISystemMenuService extends IService<SystemMenu> {
    List<MenuVO> menu();

    List<Map<String, Object>> lazyMenuTreeTable(Long pid);

    boolean hasChild(Long pid);
}
