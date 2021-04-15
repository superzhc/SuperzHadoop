package com.github.superzhc.flink.manage.service;

import com.github.superzhc.flink.manage.entity.JobConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.superzhc.flink.manage.entity.vo.JobConfigVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author superz
 * @since 2021-04-09
 */
public interface IJobConfigService extends IService<JobConfig> {
    JobConfigVO getJobConfig(Integer id);
}
