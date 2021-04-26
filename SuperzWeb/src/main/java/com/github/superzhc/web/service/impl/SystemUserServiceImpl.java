package com.github.superzhc.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.superzhc.web.entity.SystemUser;
import com.github.superzhc.web.mapper.SystemUserMapper;
import com.github.superzhc.web.service.ISystemUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author superz
 * @since 2021-04-25
 */
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements ISystemUserService {

    @Override
    public SystemUser getByUsername(String username) {
        QueryWrapper<SystemUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        SystemUser systemUser = baseMapper.selectOne(queryWrapper);
        return systemUser;
    }

    @Override
    public boolean exist(String username) {
        QueryWrapper<SystemUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }
}
