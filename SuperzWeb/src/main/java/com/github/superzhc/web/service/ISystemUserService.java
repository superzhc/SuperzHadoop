package com.github.superzhc.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.superzhc.web.entity.SystemUser;

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
