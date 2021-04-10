package com.github.superzhc.flink.manage.service;

import com.github.superzhc.flink.manage.entity.JobJarPackagesManage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * Jar包管理 服务类
 * </p>
 *
 * @author superz
 * @since 2021-04-09
 */
public interface IJobJarPackagesManageService extends IService<JobJarPackagesManage> {
    /**
     * 判断包是否已经存在
     * @param packageName
     * @param version
     * @return
     */
    boolean exist(String packageName, String version);
}
