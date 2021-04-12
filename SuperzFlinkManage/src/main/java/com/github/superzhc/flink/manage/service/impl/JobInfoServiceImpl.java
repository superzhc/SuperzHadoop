package com.github.superzhc.flink.manage.service.impl;

import com.github.superzhc.flink.manage.entity.JobInfo;
import com.github.superzhc.flink.manage.mapper.JobInfoMapper;
import com.github.superzhc.flink.manage.service.IJobInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务信息 服务实现类
 * </p>
 *
 * @author superz
 * @since 2021-04-12
 */
@Service
public class JobInfoServiceImpl extends ServiceImpl<JobInfoMapper, JobInfo> implements IJobInfoService {

}
