package com.github.superzhc.flink.manage.service;

import com.github.superzhc.flink.manage.entity.JobInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 任务信息 服务类
 * </p>
 *
 * @author superz
 * @since 2021-04-12
 */
public interface IJobInfoService extends IService<JobInfo> {
    boolean exist(String applicationId);

    Map<String,Integer> numByStatus();
}
