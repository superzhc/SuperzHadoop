package com.github.superzhc.flink.manage.mapper;

import com.github.superzhc.flink.manage.entity.JobConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.superzhc.flink.manage.entity.vo.JobConfigVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author superz
 * @since 2021-04-09
 */
public interface JobConfigMapper extends BaseMapper<JobConfig> {
    @Select("SELECT job_name,job_main_class,job_mode,package_path as job_package_path,job_options,job_arguments FROM job_config,job_jar_packages_manage WHERE job_config.job_jar_package=job_jar_packages_manage.id AND job_config.id=${id}")
    JobConfigVO getJobConfigVO(@Param("id") Integer id);
}
