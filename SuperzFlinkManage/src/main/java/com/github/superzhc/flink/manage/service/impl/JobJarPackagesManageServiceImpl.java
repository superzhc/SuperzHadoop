package com.github.superzhc.flink.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.superzhc.flink.manage.entity.JobJarPackagesManage;
import com.github.superzhc.flink.manage.mapper.JobJarPackagesManageMapper;
import com.github.superzhc.flink.manage.service.IJobJarPackagesManageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Jar包管理 服务实现类
 * </p>
 *
 * @author superz
 * @since 2021-04-09
 */
@Service
public class JobJarPackagesManageServiceImpl extends ServiceImpl<JobJarPackagesManageMapper, JobJarPackagesManage>
    implements IJobJarPackagesManageService {

    @Override
    public boolean exist(String packageName, String version) {
        QueryWrapper<JobJarPackagesManage> query = new QueryWrapper<>();
        query.eq("package_name", packageName);
        query.eq("version", version);
        int count = count(query);
        return count > 0;
    }
}
