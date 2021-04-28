package com.github.superzhc.flink.manage.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.superzhc.flink.manage.entity.SystemUser;
import com.github.superzhc.flink.manage.service.ISystemUserService;
import com.github.superzhc.flink.manage.util.CurrentUserUtil;
import com.github.superzhc.flink.manage.util.FrontListParams;
import com.github.superzhc.flink.manage.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author superz
 * @since 2021-04-25
 */
@RestController
@RequestMapping("/system-user")
public class SystemUserController {
    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public Result list(FrontListParams params) {
        QueryWrapper<SystemUser> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(params.getParam("username"))) {
            queryWrapper.like("username", params.getParam("username"));
        }
        IPage<SystemUser> lst = systemUserService.page(params.page(), params.orderBy(queryWrapper));
        return Result.success(lst);
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable("id") Integer id) {
        SystemUser user = systemUserService.getById(id);
        //不回传密码到前端
        user.setPassword(null);
        return Result.success(user);
    }

    @PostMapping
    public Result add(SystemUser user) {
        // 判断当前用户是否存在
        if (systemUserService.exist(user.getUsername())) {
            return Result.fail("新增用户[{}]已存在", user.getUsername());
        }

        // 密码加密，且若未设置则使用初始化密码
        if (StrUtil.isBlank(user.getPassword())) {
            user.setPassword(passwordEncoder.encode("123456"));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        boolean b = systemUserService.save(user);
        return b ? Result.success("新增成功") : Result.fail("新增失败");
    }

    @PutMapping()
    public Result edit(SystemUser user) {
        boolean b = systemUserService.updateById(user);
        return b ? Result.success("修改成功") : Result.fail("修改失败");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        boolean b = systemUserService.removeById(id);
        return b ? Result.success("删除成功") : Result.fail("删除失败");
    }

    @GetMapping("/current")
    public Result getCurrentUser() {
        // 获取当前用户
        SystemUser user = systemUserService.getByUsername(CurrentUserUtil.username());
        user.setPassword(null);
        return Result.success(user);
    }

    @PostMapping("/setting")
    public Result setting(SystemUser user) {
        // 获取当前用户
        String currentUser = CurrentUserUtil.username();
        if (!currentUser.equals(user.getUsername())) {
            return Result.fail("用户名无法修改");
        }

        QueryWrapper<SystemUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", currentUser);
        boolean b = systemUserService.update(user, queryWrapper);
        return b ? Result.success("修改成功") : Result.fail("修改失败");
    }

    @PostMapping("/password")
    public Result password(@RequestParam("old_password") String oldPassword, @RequestParam("new_password") String newPassword) {
        // 获取当前用户
        String currentUser = CurrentUserUtil.username();
        SystemUser user=systemUserService.getByUsername(currentUser);
        if (!user.getPassword().equals(passwordEncoder.encode(oldPassword))) {
            return Result.fail("原密码输入错误");
        }

        //设置新的密码
        user.setPassword(passwordEncoder.encode(newPassword));
        boolean b = systemUserService.updateById(user);
        return b ? Result.success("修改成功") : Result.fail("修改失败");
    }
}

