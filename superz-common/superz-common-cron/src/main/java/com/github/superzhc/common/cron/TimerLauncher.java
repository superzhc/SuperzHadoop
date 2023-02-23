package com.github.superzhc.common.cron;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author superz
 * @create 2023/2/23 16:34
 **/
public class TimerLauncher {
    private static final Logger LOG = LoggerFactory.getLogger(TimerLauncher.class);

    private static volatile Scheduler scheduler = null;

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

    public void register(Class<? extends Job> clazz, Map<String, Object> jobData) {

    }

    public void start(String[] args) {
        try {
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
