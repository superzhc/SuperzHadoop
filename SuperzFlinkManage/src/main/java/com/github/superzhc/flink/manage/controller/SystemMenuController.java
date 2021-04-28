package com.github.superzhc.flink.manage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.superzhc.flink.manage.entity.SystemMenu;
import com.github.superzhc.flink.manage.entity.vo.MenuVO;
import com.github.superzhc.flink.manage.service.ISystemMenuService;
import com.github.superzhc.flink.manage.util.MenuTreeUtil;
import com.github.superzhc.flink.manage.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2021/4/23 13:44
 */
@RestController
@RequestMapping("/system-menu")
public class SystemMenuController {
    @Autowired
    private ISystemMenuService systemMenuService;

    @GetMapping
    public Result list() {
        QueryWrapper<SystemMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        queryWrapper.orderByAsc("create_time");
        List<SystemMenu> menuList = systemMenuService.list(queryWrapper);
        // 计算出子菜单
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
        List<MenuVO> lst = MenuTreeUtil.toTree(menuInfo, 0L);
        return Result.success(lst);
    }

    @GetMapping("/lazy")
    public Result lazyList(@RequestParam Long pid) {
        List<Map<String, Object>> lst = systemMenuService.lazyMenuTreeTable(pid);
        return Result.success(lst);
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable("id") Integer id) {
        SystemMenu systemMenu = systemMenuService.getById(id);
        return Result.success(systemMenu);
    }

    @PostMapping
    public Result add(SystemMenu menu) {
        boolean b = systemMenuService.save(menu);
        return b ? Result.success("新增成功") : Result.fail("新增失败");
    }

    @PutMapping
    public Result edit(SystemMenu menu){
        boolean b = systemMenuService.updateById(menu);
        return b ? Result.success("修改成功") : Result.fail("修改失败");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Long id) {
        if (systemMenuService.hasChild(id)) {
            return Result.fail("存在子菜单，无法删除");
        }
        boolean b = systemMenuService.removeById(id);
        return b ? Result.success("删除成功") : Result.fail("删除失败");
    }
}
