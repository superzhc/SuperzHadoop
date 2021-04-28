package com.github.superzhc.flink.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.superzhc.flink.manage.entity.SystemBasic;
import com.github.superzhc.flink.manage.entity.SystemMenu;
import com.github.superzhc.flink.manage.entity.vo.MenuVO;
import com.github.superzhc.flink.manage.mapper.SystemMenuMapper;
import com.github.superzhc.flink.manage.service.ISystemBasicService;
import com.github.superzhc.flink.manage.service.ISystemMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.superzhc.flink.manage.util.MenuTreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author superz
 * @since 2021-04-22
 */
@Service
public class SystemMenuServiceImpl extends ServiceImpl<SystemMenuMapper, SystemMenu> implements ISystemMenuService {

    @Override
    public List<MenuVO> menu() {
        Map<String, Object> map = new HashMap<>(16);
        QueryWrapper<SystemMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "1");
        queryWrapper.orderByAsc("sort");
        List<SystemMenu> menuList = baseMapper.selectList(queryWrapper);
        List<MenuVO> menuInfo = new ArrayList<>();
        for (SystemMenu e : menuList) {
            MenuVO menuVO = new MenuVO();
            menuVO.setId(e.getId());
            menuVO.setPid(e.getPid());
            menuVO.setHref(e.getHref());
            menuVO.setTitle(e.getTitle());
            menuVO.setIcon(e.getIcon());
            menuVO.setTarget(e.getTarget());
            menuInfo.add(menuVO);
        }
        return MenuTreeUtil.toTree(menuInfo, 0L);
    }

    @Override
    public List<Map<String, Object>> lazyMenuTreeTable(Long pid) {
        List<Map<String, Object>> lst = baseMapper.selectChildByPid(pid);
        for (Map<String, Object> item : lst) {
            item.put("haveChild", ((Long) item.get("num")) > 0);
        }
        return lst;
    }

    @Override
    public boolean hasChild(Long pid) {
        return baseMapper.countByPid(pid) > 0;
    }
}
