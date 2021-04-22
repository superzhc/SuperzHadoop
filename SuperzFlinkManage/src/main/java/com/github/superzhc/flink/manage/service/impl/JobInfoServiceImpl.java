package com.github.superzhc.flink.manage.service.impl;

import com.github.superzhc.flink.manage.entity.JobInfo;
import com.github.superzhc.flink.manage.mapper.JobInfoMapper;
import com.github.superzhc.flink.manage.service.IJobInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    @Override
    public boolean exist(String applicationId) {
        JobInfo jobInfo = baseMapper.selectByApplicationId(applicationId);
        return null != jobInfo;
    }

    @Override
    public Map<String, Integer> numByStatus() {
        int total = baseMapper.total();
        int running = baseMapper.running();
        int failed = baseMapper.failed();
        int succeeded = baseMapper.succeeded();
        Map<String, Integer> result = new HashMap<>();
        result.put("TOTAL", total);
        result.put("RUNNING", running);
        result.put("FAILED", failed);
        result.put("SUCCEEDED", succeeded);
        return result;
    }
}
