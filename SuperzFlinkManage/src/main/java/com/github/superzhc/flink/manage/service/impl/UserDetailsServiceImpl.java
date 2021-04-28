package com.github.superzhc.flink.manage.service.impl;

import com.github.superzhc.flink.manage.entity.SystemUser;
import com.github.superzhc.flink.manage.service.ISystemUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;

/**
 * @author superz
 * @create 2021/4/25 16:24
 */
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private ISystemUserService systemUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUser systemUser = systemUserService.getByUsername(username);
        if (null == systemUser) {
            log.info("登录用户[{}]没注册!", username);
            throw new UsernameNotFoundException("登录用户[" + username + "]没注册!");
        }
        return new User(systemUser.getUsername(), systemUser.getPassword(), getAuthority());
    }

    private List getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
