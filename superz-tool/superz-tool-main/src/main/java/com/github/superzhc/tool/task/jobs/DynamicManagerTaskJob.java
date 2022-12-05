package com.github.superzhc.tool.task.jobs;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.common.utils.PathUtils;
import com.github.superzhc.tool.task.MyTimingTaskNew;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.github.superzhc.tool.task.MyTimingTaskNew.*;

/**
 * @author superz
 * @create 2022/11/18 15:13
 **/
public class DynamicManagerTaskJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(DynamicManagerTaskJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try (JdbcHelper jdbc = new JdbcHelper(String.format(SQLITE_DB_URL_TEMPLATE, PathUtils.project(), TOOL_SQLITE_DB_PATH))) {
            List<Map<String, Object>> dynamicJobs = jdbc.query("select * from superz_quartz_dynamic_job where is_enable=1");
            log.debug("动态管理任务数量：{}", (null == dynamicJobs ? 0 : dynamicJobs.size()));
            if (null == dynamicJobs || dynamicJobs.size() == 0) {
                return;
            }

            for (Map<String, Object> dynamicJob : dynamicJobs) {
                int id = (int) dynamicJob.get("id");
                int jobId = (int) dynamicJob.get("job_id");
                String operate = (String) dynamicJob.get("operate");
                switch (operate) {
                    case "I":
                        addJob(jdbc, jobId);
                        break;
                    case "D":
                        deleteJob(jdbc, jobId);
                        break;
                    case "U":
                        updateJob(jdbc, jobId);
                        break;
                    default:
                        throw new JobExecutionException("尚不支持[" + operate + "]操作");
                }

                //将该条动态管理给置为无效
                jdbc.dmlExecute("update superz_quartz_dynamic_job set is_enable=0 where id=?", id);
            }
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    private void addJob(JdbcHelper jdbc, int jobId) {
        log.debug("新增任务[{}]开始...", jobId);
        Map<String, Object> job = jdbc.queryFirst("select * from superz_quartz_job_new where id=?", jobId);
        if (null != job || job.size() > 0) {
            String group = (String) job.get("_group");
            String name = (String) job.get("_name");
            if (!existsJob(group, name)) {
                // 获取系统公用参数
                List<Map<String, Object>> commonData = jdbc.query("select * from superz_quartz_common_data");
                // 用户自定义参数
                List<Map<String, Object>> jobData = jdbc.query("select * from superz_quartz_job_data where job_id=?", jobId);
                MyTimingTaskNew.addJob(job, commonData, jobData);
            } else {
                log.info("任务[{}]已存在，无需新增", jobId);
            }

            // 将任务的状态置为可用
            jdbc.dmlExecute("update superz_quartz_job_new set is_enable=1 where id=?", jobId);
        }

        log.debug("新增任务[{}]结束！", jobId);
    }

    private void deleteJob(JdbcHelper jdbc, int jobId) {
        log.debug("结束任务[{}]开始...", jobId);
        Map<String, Object> job = jdbc.queryFirst("select * from superz_quartz_job_new where id=?", jobId);
        if (null != job || job.size() > 0) {
            String group = (String) job.get("_group");
            String name = (String) job.get("_name");
            MyTimingTaskNew.deleteJob(group, name);

            // 将任务的状态置为不可用
            jdbc.dmlExecute("update superz_quartz_job_new set is_enable=0 where id=?", jobId);
        }

        log.debug("结束任务[{}]结束！", jobId);
    }

    private void updateJob(JdbcHelper jdbc, int jobId) {
        log.debug("更新任务[{}]开始...", jobId);
        Map<String, Object> job = jdbc.queryFirst("select * from superz_quartz_job_new where id=?", jobId);
        if (null != job || job.size() > 0) {
            String group = (String) job.get("_group");
            String name = (String) job.get("_name");
            JobKey jobKey = new JobKey(group, name);
            // 若任务存在，则先删除任务
            if (MyTimingTaskNew.existsJob(jobKey)) {
                MyTimingTaskNew.deleteJob(jobKey);
            }

            // 获取系统公用参数
            List<Map<String, Object>> commonData = jdbc.query("select * from superz_quartz_common_data");
            // 用户自定义参数
            List<Map<String, Object>> jobData = jdbc.query("select * from superz_quartz_job_data where job_id=?", jobId);
            MyTimingTaskNew.addJob(job, commonData, jobData);

            // 将任务的状态置为可用
            jdbc.dmlExecute("update superz_quartz_job_new set is_enable=1 where id=?", jobId);
        }

        log.debug("更新任务[{}]结束！", jobId);
    }
}
