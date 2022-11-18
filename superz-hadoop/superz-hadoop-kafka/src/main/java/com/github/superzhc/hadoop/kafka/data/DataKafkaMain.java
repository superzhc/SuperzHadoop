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


        JobDataMap jobDataMap2=new JobDataMap();
        jobDataMap2.put("isNeedInit",true);
        jobDataMap2.put("brokers","127.0.0.1:19092");
        jobDataMap2.put("topic","fund_eastmoney_real_net");
        jobDataMap2.put("codes","000478,160119,519671,001594,001595,160716,501050,004752,501009,012820,519915,001180,000071,012348,090010,164402,006327,164906");
        addJob(scheduler,"fund","eastmoney_real_net",FundRealNetJob.class,jobDataMap2,"0/5 * 9-14 ? * MON-FRI");

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
