package com.github.superzhc.flink.manage.job.builder;

import com.github.superzhc.flink.manage.entity.vo.JobConfigVO;

import java.util.List;

/**
 * 任务的命令构建
 * @author superz
 * @create 2021/4/13 15:11
 */
public interface JobBuilder {
    List<String> build(JobConfigVO jobConfigVO);
}
