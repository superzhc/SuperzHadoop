package com.github.superzhc.tool.task;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.common.utils.PathUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.CronCalendar;
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

    public static boolean existsJob(String group, String name) {
        return existsJob(new JobKey(group, name));
    }

    public static boolean existsJob(JobKey jobKey) {
        try {
            return getScheduler().checkExists(jobKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void addJob(Map<String, Object> job, List<Map<String, Object>> commonData, List<Map<String, Object>> jobData) {
        try {
            String group = (String) job.get("_group");
            String name = (String) job.get("_name");
            Class<? extends Job> clazz = (Class<? extends Job>) Class.forName((String) job.get("_class"));
            String crons = (String) job.get("_cron");
            String description = (String) job.get("_description");
            log.info("新增任务[group:{},name:{},class:{},cron:{}]", group, name, clazz, crons);

            JobDetail jobDetail = JobBuilder.newJob(clazz)
                    .withIdentity(name, group)
                    .withDescription(description)
                    .build();

            /* 配置参数 */
            // 公共参数，主要包括系统的一些连接信息，系统标识等
            Map<String, Object> jobDataMap = new HashMap<>();
            if (null != commonData && commonData.size() > 0) {
                for (Map<String, Object> commonDataItem : commonData) {
                    jobDataMap.put(String.valueOf(commonDataItem.get("_key")), commonDataItem.get("_value"));
                }
            }
            // 自定义任务参数，各任务自定义参数，优先级高于公共参数
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteJob(String group, String name){
        deleteJob(new JobKey(name, group));
    }

    public static void deleteJob(JobKey jobKey) {
        try {
            if (existsJob(jobKey)) {
                getScheduler().deleteJob(jobKey);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        try (JdbcHelper jdbc = new JdbcHelper(String.format(SQLITE_DB_URL_TEMPLATE, PathUtils.project(), TOOL_SQLITE_DB_PATH))) {
            // 获取系统公用参数
            List<Map<String, Object>> commonData = jdbc.query("select * from superz_quartz_common_data");

            List<Map<String, Object>> jobs = jdbc.query("select * from superz_quartz_job_new where is_enable=1");
            if (null != jobs && jobs.size() > 0) {
                for (Map<String, Object> job : jobs) {
                    // 获取任务唯一标识
                    int id = (int) job.get("id");
                    // 获取任务自定义参数
                    List<Map<String, Object>> jobData = jdbc.query("select * from superz_quartz_job_data where job_id=?", id);
                    addJob(job, commonData, jobData);
                }
            }
        }
        getScheduler().start();
    }
}
