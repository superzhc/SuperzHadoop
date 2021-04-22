package com.github.superzhc.flink.manage.mapper;

import com.github.superzhc.flink.manage.entity.JobInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 任务信息 Mapper 接口
 * </p>
 *
 * @author superz
 * @since 2021-04-12
 */
public interface JobInfoMapper extends BaseMapper<JobInfo> {
    @Select("select * from job_info where job_application_id=#{applicationId}")
    JobInfo selectByApplicationId(@Param("applicationId") String applicationId);

    @Select("select count(*) from job_info")
    @ResultType(int.class)
    int total();

    @Select("select count(*) from job_info where job_status='RUNNING'")
    @ResultType(int.class)
    int running();

    @Select("select count(*) from job_info where job_status='FAILED'")
    @ResultType(int.class)
    int failed();

    @Select("select count(*) from job_info where job_status='SUCCEEDED'")
    @ResultType(int.class)
    int succeeded();
}
