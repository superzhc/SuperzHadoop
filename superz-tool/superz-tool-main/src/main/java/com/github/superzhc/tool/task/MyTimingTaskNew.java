package com.github.superzhc.tool.task;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.common.utils.PathUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/11/18 15:32
 **/
public class MyTimingTaskNew {
    private static final Logger log = LoggerFactory.getLogger(MyTimingTaskNew.class);

    public static final String SQLITE_DB_URL_TEMPLATE = "jdbc:sqlite:%s/%s";
    public static final String TOOL_SQLITE_DB_PATH = "superz-tool/superz-tool-main/db/tool.db";

    private static volatile Scheduler scheduler = null;

    public static Scheduler getScheduler() {
        if (null == scheduler) {
            synchronized (MyTimingTaskNew.class) {
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
            List<Map<String, Object>> jobs = jdbc.query("select * from superz_quartz_job_new where is_enable=1");
            if (null != jobs && jobs.size() > 0) {
                for (Map<String, Object> job : jobs) {
                    int id = (int) job.get("id");
                    String group = (String) job.get("_group");
                    String name = (String) job.get("_name");
                    Class<? extends Job> clazz = (Class<? extends Job>) Class.forName((String) job.get("_class"));
                    String cron = (String) job.get("_cron");
                    String description = (String) job.get("_description");

                    JobDetail jobDetail = JobBuilder.newJob(clazz)
                            .withIdentity(name, group)
                            .withDescription(description)
                            .build();

                    // 获取参数配置
                    Map<String, Object> jobDataMap = new HashMap<>();
                    List<Map<String, Object>> jobData = jdbc.query("select * from superz_quartz_job_data where job_id=?", id);
                    if (null != jobData && jobData.size() > 0) {
                        for (Map<String, Object> jobDetailItem : jobData) {
                            jobDataMap.put(String.valueOf(jobDetailItem.get("_key")), jobDetailItem.get("_value"));
                        }
                    }
                    jobDetail.getJobDataMap().putAll(jobDataMap);

                    Trigger trigger = TriggerBuilder.newTrigger()
                            .withIdentity(name, group)
                            .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                            .withDescription(description)
                            .build();

                    getScheduler().scheduleJob(jobDetail, trigger);
                }
            }
        }
        getScheduler().start();
    }
}
