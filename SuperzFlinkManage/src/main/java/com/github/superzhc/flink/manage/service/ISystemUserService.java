package com.github.superzhc.flink.manage.service;

import com.github.superzhc.flink.manage.entity.SystemUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author superz
 * @since 2021-04-25
 */
public interface ISystemUserService extends IService<SystemUser> {
    SystemUser getByUsername(String username);

    boolean exist(String username);
}
