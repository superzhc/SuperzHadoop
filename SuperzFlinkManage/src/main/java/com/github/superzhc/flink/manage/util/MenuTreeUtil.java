package com.github.superzhc.flink.manage.util;

import com.github.superzhc.flink.manage.entity.vo.MenuVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author superz
 * @create 2021/4/22 18:25
 */
public class MenuTreeUtil {
    public static List<MenuVO> toTree(List<MenuVO> treeList, Long pid) {
        List<MenuVO> retList = new ArrayList<MenuVO>();
        for (MenuVO parent : treeList) {
            if (pid.equals(parent.getPid())) {
                retList.add(findChildren(parent, treeList));
            }
        }
        return retList;
    }

    private static MenuVO findChildren(MenuVO parent, List<MenuVO> treeList) {
        for (MenuVO child : treeList) {
            if (parent.getId().equals(child.getPid())) {
                if (parent.getChild() == null) {
                    parent.setChild(new ArrayList<>());
                }
                parent.getChild().add(findChildren(child, treeList));
            }
        }
        return parent;
    }
}
