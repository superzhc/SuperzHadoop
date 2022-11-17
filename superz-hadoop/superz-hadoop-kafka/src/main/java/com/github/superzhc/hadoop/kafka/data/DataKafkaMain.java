package com.github.superzhc.hadoop.kafka.data;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.utils.Key;

/**
 * @author superz
 * @create 2022/11/17 13:46
 **/
public class DataKafkaMain {
    public static void main(String[] args) throws Exception {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("brokers", "127.0.0.1:19092");
        // jobDataMap.put("partitions",5);

        String type = "all";
        jobDataMap.put("type", type);
        addJob(scheduler, "guangdiu", type, GuangDiuJob.class, jobDataMap, "0 0/5 * * * ? *");

        type = "food";
        jobDataMap.put("type", type);
        addJob(scheduler, "guangdiu", type, GuangDiuJob.class, jobDataMap, "5 0/5 * * * ? *");

        type = "daily";
        jobDataMap.put("type", type);
        addJob(scheduler, "guangdiu", type, GuangDiuJob.class, jobDataMap, "10 0/5 * * * ? *");

        type = "electrical";
        jobDataMap.put("type", type);
        addJob(scheduler, "guangdiu", type, GuangDiuJob.class, jobDataMap, "15 0/5 * * * ? *");

        type = "medical";
        jobDataMap.put("type", type);
        addJob(scheduler, "guangdiu", type, GuangDiuJob.class, jobDataMap, "20 0/5 * * * ? *");

        type = "mensshoes";
        jobDataMap.put("type", type);
        addJob(scheduler, "guangdiu", type, GuangDiuJob.class, jobDataMap, "25 0/5 * * * ? *");

        type = "menswear";
        jobDataMap.put("type", type);
        addJob(scheduler, "guangdiu", type, GuangDiuJob.class, jobDataMap, "30 0/5 * * * ? *");

        type = "sport";
        jobDataMap.put("type", type);
        addJob(scheduler, "guangdiu", type, GuangDiuJob.class, jobDataMap, "35 0/5 * * * ? *");

        type = "furniture";
        jobDataMap.put("type", type);
        addJob(scheduler, "guangdiu", type, GuangDiuJob.class, jobDataMap, "40 0/5 * * * ? *");


        scheduler.start();
        // 在调用 scheduler.shutdown() 之前，scheduler 不会终止，还有活跃的线程在执行
        // scheduler.shutdown();
    }

    private static void addJob(Scheduler scheduler, String group, String name, Class<? extends Job> clazz, JobDataMap jobDataMap, String cron) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(clazz)
                .withIdentity(null == name ? Key.createUniqueName(group) : name, group)
                .usingJobData(null == jobDataMap ? new JobDataMap() : jobDataMap)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(null == name ? Key.createUniqueName(group) : name, group)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}
