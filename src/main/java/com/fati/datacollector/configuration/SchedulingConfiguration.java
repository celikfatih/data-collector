package com.fati.datacollector.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * author @ fati
 * created @ 27.05.2021
 */

@Configuration
@EnableScheduling
public class SchedulingConfiguration implements SchedulingConfigurer {

    public static final int POOL_SIZE = 10;
    public static final String TASK_SCHEDULER_PREFIX = "task-scheduler";

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(POOL_SIZE);
        taskScheduler.setThreadNamePrefix(TASK_SCHEDULER_PREFIX);
        taskScheduler.initialize();

        taskRegistrar.setTaskScheduler(taskScheduler);
    }
}
