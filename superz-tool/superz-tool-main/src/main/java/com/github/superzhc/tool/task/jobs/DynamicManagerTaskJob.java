package com.github.superzhc.tool.task.jobs;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.common.utils.PathUtils;
import org.quartz.*;
import org.quartz.impl.calendar.CronCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.HashMap;
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
                        addJob(jdbc, id, jobId);
                        break;
                    case "D":
                        deleteJob(jdbc, id, jobId);
                        break;
                    case "M":
                    default:
                        throw new JobExecutionException("尚不支持[" + operate + "]操作");
                }
            }
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    private void addJob(JdbcHelper jdbc, int id, int jobId) throws SchedulerException, ClassNotFoundException, ParseException {
        log.debug("新增任务[{}]开始...", jobId);
        Map<String, Object> job = jdbc.queryFirst("select * from superz_quartz_job_new where id=?", jobId);
        if (null != job || job.size() > 0) {
            String group = (String) job.get("_group");
            String name = (String) job.get("_name");
            JobKey jobKey = new JobKey(name, group);
            if (!getScheduler().checkExists(jobKey)) {
                Class<? extends Job> clazz = (Class<? extends Job>) Class.forName((String) job.get("_class"));
                String crons = (String) job.get("_cron");
                String description = (String) job.get("_description");

                JobDetail jobDetail = JobBuilder.newJob(clazz)
                        .withIdentity(name, group)
                        .withDescription(description)
                        .build();

                // 获取参数配置
                Map<String, Object> jobDataMap = new HashMap<>();
                List<Map<String, Object>> jobData = jdbc.query("select * from superz_quartz_job_data where job_id=?", jobId);
                if (null != jobData && jobData.size() > 0) {
                    for (Map<String, Object> jobDetailItem : jobData) {
                        jobDataMap.put(String.valueOf(jobDetailItem.get("_key")), jobDetailItem.get("_value"));
                    }
                }
                jobDetail.getJobDataMap().putAll(jobDataMap);

                String[] cronArr = crons.split(";");
                String cron = cronArr[0];
                String calendarName = null;
                int len = cronArr.length;
                if (len > 1) {
                    calendarName = jobDetail.getKey().toString() + ".calendar";
                    Calendar calendar = null;
                    for (int i = 1; i < len; i++) {
                        calendar = new CronCalendar(calendar, cronArr[i]);
                    }
                    getScheduler().addCalendar(calendarName, calendar, false, false);
                }

                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(name, group)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                        .modifiedByCalendar(calendarName)
                        .withDescription(description)
                        .build();

                getScheduler().scheduleJob(jobDetail, trigger);
            } else {
                log.info("任务[{}]已存在，无需新增", jobId);
            }

            // 将任务的状态置为不可用
            jdbc.dmlExecute("update superz_quartz_job_new set is_enable=1 where id=?", jobId);
        }

        //将该条动态管理给置为无效
        jdbc.dmlExecute("update superz_quartz_dynamic_job set is_enable=0 where id=?", id);
        log.debug("新增任务[{}]结束！", jobId);
    }

    private void deleteJob(JdbcHelper jdbc, int id, int jobId) throws SchedulerException {
        log.debug("结束任务[{}]开始...", jobId);
        Map<String, Object> job = jdbc.queryFirst("select * from superz_quartz_job_new where id=?", jobId);
        if (null != job || job.size() > 0) {
            String group = (String) job.get("_group");
            String name = (String) job.get("_name");
            JobKey jobKey = new JobKey(name, group);
            if (getScheduler().checkExists(jobKey)) {
                getScheduler().deleteJob(jobKey);
            } else {
                log.debug("任务[{}]不存在，无需结束！", jobId);
            }

            // 将任务的状态置为不可用
            jdbc.dmlExecute("update superz_quartz_job_new set is_enable=0 where id=?", jobId);
        }

        //将该条动态管理给置为无效
        jdbc.dmlExecute("update superz_quartz_dynamic_job set is_enable=0 where id=?", id);
        log.debug("结束任务[{}]结束！", jobId);
    }
}
