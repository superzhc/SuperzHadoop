package com.github.superzhc.common.cron;

import com.github.superzhc.common.cron.annotation.STask;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/2/23 16:34
 **/
public class TimerLauncher {
    private static final Logger LOG = LoggerFactory.getLogger(TimerLauncher.class);

    static class Task {
        private String group;

        private String name;

        private String description;

        private JobDetail jobDetail;
        private Trigger trigger;

//        public Task(JobDetail jobDetail, Trigger trigger) {
//            this.jobDetail = jobDetail;
//            this.trigger = trigger;
//        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public JobDetail getJobDetail() {
            return jobDetail;
        }

        public void setJobDetail(JobDetail jobDetail) {
            this.jobDetail = jobDetail;
        }

        public Trigger getTrigger() {
            return trigger;
        }

        public void setTrigger(Trigger trigger) {
            this.trigger = trigger;
        }
    }

    private static volatile Scheduler scheduler = null;

    /**
     * 任务
     */
    private static List<Task> tasks = Collections.synchronizedList(new ArrayList<Task>());

    public static Scheduler getScheduler() {
        if (null == scheduler) {
            synchronized (TimerLauncher.class) {
                if (null == scheduler) {
                    try {
                        scheduler = StdSchedulerFactory.getDefaultScheduler();
                    } catch (SchedulerException e) {
                        //ignore
                    }
                }
            }
        }
        return scheduler;
    }

    public static void register(Class<? extends Job> clazz) {
        if (!clazz.isAnnotationPresent(STask.class)) {
            throw new RuntimeException("Job register need config STask Annotation");
        }

        register(null, null, null, clazz, null, null);
    }

    public static void register(Class<? extends Job> clazz, Map<String, Object> jobData) {
        if (!clazz.isAnnotationPresent(STask.class)) {
            throw new RuntimeException("Job register need config STask Annotation");
        }

        register(null, null, null, clazz, jobData, null);
    }

    public static void register(String cron, Class<? extends Job> clazz) {
        if (!clazz.isAnnotationPresent(STask.class)) {
            throw new RuntimeException("Job register need config STask Annotation");
        }

        register(null, null, cron, clazz, null, null);
    }

    public static void register(String cron, Class<? extends Job> clazz, Map<String, Object> jobData) {
        if (!clazz.isAnnotationPresent(STask.class)) {
            throw new RuntimeException("Job register need config STask Annotation");
        }

        register(null, null, cron, clazz, jobData, null);
    }

    public static void register(String group, String name, Class<? extends Job> clazz) {
        if (!clazz.isAnnotationPresent(STask.class)) {
            throw new RuntimeException("Job register need config STask Annotation");
        }

        register(group, name, null, clazz, null, null);
    }

    public static void register(String group, String name, Class<? extends Job> clazz, Map<String, Object> jobData) {
        if (!clazz.isAnnotationPresent(STask.class)) {
            throw new RuntimeException("Job register need config STask Annotation");
        }

        register(group, name, null, clazz, jobData, null);
    }

    public static void register(String group, String name, String cron, Class<? extends Job> clazz) {
        register(group, name, cron, clazz, null, null);
    }

    public static void register(String group, String name, String cron, Class<? extends Job> clazz, Map<String, Object> jobData) {
        register(group, name, cron, clazz, jobData, null);
    }

    public static void register(String group, String name, String cron, Class<? extends Job> clazz, String description) {
        register(group, name, cron, clazz, null, description);
    }

    public static void register(String group, String name, String cron, Class<? extends Job> clazz, Map<String, Object> jobData, String description) {
        STask sTask = clazz.getDeclaredAnnotation(STask.class);
        if (null != sTask) {
            group = (null == group || group.trim().length() == 0) ? sTask.group() : group;
            name = (null == name || name.trim().length() == 0) ? sTask.name() : name;
            cron = (null == cron || cron.trim().length() == 0) ? sTask.cron() : cron;
            description = (null == description || description.trim().length() == 0) ? sTask.description() : description;
        }

        if (null == cron || cron.trim().length() == 0) {
            throw new RuntimeException("Cron cannot be empty");
        }


        Task task = new Task();
        task.setGroup(group);
        task.setName(name);
        task.setDescription(description);

        JobDetail jobDetail = JobBuilder.newJob(clazz)
                .withIdentity(group, name)
                .withDescription(description)
                .build();
        if (null != jobData && jobData.size() > 0) {
            jobDetail.getJobDataMap().putAll(jobData);
        }
        task.setJobDetail(jobDetail);

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(group, name)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .withDescription(description)
                .build();
        task.setTrigger(trigger);

        tasks.add(task);
    }

    public void start(String[] args) {
        try {
            for (Task task : tasks) {
                getScheduler().scheduleJob(task.getJobDetail(), task.getTrigger());
            }

            getScheduler().start();
        } catch (SchedulerException e) {
            try {
                getScheduler().shutdown();
            } catch (SchedulerException ex) {
                //ignore
            }
        }
    }
}
