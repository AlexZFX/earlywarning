package com.alexzfx.earlywarningmachine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Author : Alex
 * Date : 2018/4/14 13:53
 * Description : quartz定时任务配置
 */
@Configuration
public class QuartzConfig {

//    private static final String TASKNAME = "MachineDataTask";
//    private static final String TASKGROUP = "MachineTask";
//
//    @Bean
//    public MethodInvokingJobDetailFactoryBean detailFactoryBean(MachineDataTask task) {
//        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
//        //是否并发执行，设置为false，则若当前任务未执行完成，则不会继续执行下一任务。
//        jobDetail.setConcurrent(false);
//        jobDetail.setBeanName(TASKNAME);
//        jobDetail.setGroup(TASKGROUP);
//        jobDetail.setTargetObject(task);
//        return jobDetail;
//    }
//
//    @Bean("defaultTrigger")
//    public CronTriggerFactoryBean defaultTrigger(MethodInvokingJobDetailFactoryBean jobDetail) {
//        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
//        tigger.setJobDetail(jobDetail.getObject());
//        //暂时预设为每10s执行一次
//        tigger.setCronExpression("0/10 * * * * ? *");
//        return tigger;
//    }
//
//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean(Trigger trigger) {
//        SchedulerFactoryBean bean = new SchedulerFactoryBean();
//        bean.setStartupDelay(10);
//        bean.setTriggers(trigger);
//        return bean;
//    }


    //多任务是的Scheduler，动态设置Trigger。一个SchedulerFactoryBean可能有多个Trigger
    @Bean("multitaskScheduler")
    public SchedulerFactoryBean schedulerFactoryBean() {
        return new SchedulerFactoryBean();
    }
}
