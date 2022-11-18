package com.github.superzhc.tool.task;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.common.utils.PathUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.utils.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/11/18 11:27
 **/
public class MyTimingTask {
    private static final Logger log = LoggerFactory.getLogger(MyTimingTask.class);

    public static final String SQLITE_DB_URL_TEMPLATE = "jdbc:sqlite:%s/%s";
    public static final String TOOL_SQLITE_DB_PATH = "superz-tool/superz-tool-main/db/tool.db";

    private static volatile Scheduler scheduler = null;

    public static Scheduler getScheduler() {
        if (null == scheduler) {
            synchronized (MyTimingTask.class) {
                if (null == scheduler) {
                    try {
                        scheduler = StdSchedulerFactory.getDefaultScheduler();
                    } catch (Exception e) {
                        //ignore
                    }
                }
            }
        }
        return scheduler;
    }

    public static void main(String[] args) throws Exception {
        try (JdbcHelper jdbc = new JdbcHelper(String.format(SQLITE_DB_URL_TEMPLATE, PathUtils.project(), TOOL_SQLITE_DB_PATH))) {
            List<Map<String, Object>> jobs = jdbc.query("select * from superz_quartz_job where is_enable=1");
            if (null != jobs && jobs.size() > 0) {
                for (Map<String, Object> job : jobs) {
                    // JobDetail
                    int jobDetailId = (int) job.get("job_detail_id");
                    Map<String, Object> jobDetailMap = jdbc.queryFirst("select * from superz_quartz_job_detail where id=?", jobDetailId);
                    String clazz = (String) jobDetailMap.get("class");
                    String jobGroup = (String) jobDetailMap.get("_group");
                    String jobName = (String) jobDetailMap.get("_name");
                    String jobDescription = (String) jobDetailMap.get("_description");
                    JobBuilder jobBuilder = JobBuilder.newJob()
                            .ofType((Class<? extends Job>) Class.forName(clazz))
                            // 不支持jobname为null，需要管理任务
                            .withIdentity(jobName/*null == jobName ? Key.createUniqueName(jobGroup) : jobName*/, jobGroup)
                            .withDescription(jobDescription);

                    // jobDataMap
                    JobDataMap jobDataMap = new JobDataMap();
                    List<Map<String, Object>> jobDetailData = jdbc.query("select * from superz_quartz_job_detail_data where job_detail_id=?", jobDetailId);
                    if (null != jobDetailData && jobDetailData.size() > 0) {
                        for (Map<String, Object> jobDetailItem : jobDetailData) {
                            jobDataMap.put(String.valueOf(jobDetailItem.get("_key")), jobDetailItem.get("_value"));
                        }
                    }
                    jobBuilder.usingJobData(jobDataMap);

                    JobDetail jobDetail = jobBuilder.build();

                    // Trigger
                    int triggerId = (int) job.get("trigger_id");
                    Map<String, Object> triggerMap = jdbc.queryFirst("select * from superz_quartz_trigger where id=?", triggerId);
                    String triggerGroup = (String) triggerMap.get("_group");
                    String triggerName = (String) triggerMap.get("_name");
                    String cron = (String) triggerMap.get("cron");
                    String triggerDescription = (String) triggerMap.get("_description");
                    Trigger trigger = TriggerBuilder.newTrigger()
                            // 不支持triggername为null，需要管理
                            .withIdentity(triggerName/*null == triggerName ? Key.createUniqueName(triggerGroup) : triggerName*/, triggerGroup)
                            .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                            .withDescription(triggerDescription)
                            .build();

                    getScheduler().scheduleJob(jobDetail, trigger);
                }
            }
        }

        getScheduler().start();
    }
}
