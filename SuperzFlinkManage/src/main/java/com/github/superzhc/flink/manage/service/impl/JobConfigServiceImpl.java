package com.github.superzhc.flink.manage.service.impl;

import com.github.superzhc.flink.manage.entity.JobConfig;
import com.github.superzhc.flink.manage.entity.vo.JobConfigVO;
import com.github.superzhc.flink.manage.mapper.JobConfigMapper;
import com.github.superzhc.flink.manage.service.IJobConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author superz
 * @since 2021-04-09
 */
@Service
public class JobConfigServiceImpl extends ServiceImpl<JobConfigMapper, JobConfig> implements IJobConfigService {

    @Override
    public JobConfigVO getJobConfig(Integer id) {
        return baseMapper.getJobConfigVO(id);
    }
}
