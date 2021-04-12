package com.github.superzhc.flink.manage.service.impl;

import com.github.superzhc.flink.manage.entity.JobInfoJobConfig;
import com.github.superzhc.flink.manage.mapper.JobInfoJobConfigMapper;
import com.github.superzhc.flink.manage.service.IJobInfoJobConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务和任务配置关联表 服务实现类
 * </p>
 *
 * @author superz
 * @since 2021-04-12
 */
@Service
public class JobInfoJobConfigServiceImpl extends ServiceImpl<JobInfoJobConfigMapper, JobInfoJobConfig> implements IJobInfoJobConfigService {

}
