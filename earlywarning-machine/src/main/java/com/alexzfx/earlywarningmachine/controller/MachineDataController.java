package com.alexzfx.earlywarningmachine.controller;

import com.alexzfx.earlywarningmachine.entity.JobList;
import com.alexzfx.earlywarningmachine.quartz.MachineDataTask;
import com.alexzfx.earlywarningmachine.rabbit.MessageSender;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author : Alex
 * Date : 2018/4/14 13:06
 * Description :
 */
@RestController
@Slf4j
public class MachineDataController {

    private static final ConcurrentHashMap<Integer, JobKey> jobMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, JobKey> pausedJobMap = new ConcurrentHashMap<>();
    private final MessageSender sender;
    @Resource(name = "multitaskScheduler")
    private Scheduler scheduler;

    @Autowired
    public MachineDataController(MessageSender sender) {
        this.sender = sender;
    }

    @GetMapping("/jobInfo")
    public String getJobInfo() {
        JobList list = new JobList();
        list.getWorkerIds().addAll(jobMap.keySet());
        list.getPausedIds().addAll(pausedJobMap.keySet());
        return JSON.toJSONString(list);
    }

    @PostMapping("/resetTime")
    public String resetTime(@RequestParam("machineId") Integer machineId, @RequestParam("time") String cron) {
        if (!jobMap.containsKey(machineId)) {
            return "该仪器不在运行";
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(machineId.toString());
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(machineId.toString(), Scheduler.DEFAULT_GROUP).withSchedule(scheduleBuilder).build();
        try {
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "failed";
        }
        return "success";
    }

    @PostMapping(value = "/startMachineInfo")
    public String machineDataCreater(@RequestParam(name = "machineId") Integer machineId, @RequestParam(name = "cid", required = false) Integer categoryId, @RequestParam(value = "time", required = false) String cron) {
        if (jobMap.containsKey(machineId) || pausedJobMap.containsKey(machineId)) {
            return "打扰了，该仪器已经有数据了";
        }
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("machineId", machineId);
        dataMap.put("type", categoryId);
        dataMap.put("sender", sender);
        JobKey jobKey = new JobKey(machineId.toString());
        if (cron == null) {
            cron = "* 0/1 * * * ? *";
        }
        JobDetail jobDetail = JobBuilder.newJob(MachineDataTask.class).withIdentity(jobKey).setJobData(dataMap).build();
        CronScheduleBuilder scheduleBuilder;
        try {
            scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        } catch (RuntimeException e) {
            return "时间表达式错误";
        }
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(machineId.toString(), Scheduler.DEFAULT_GROUP).withSchedule(scheduleBuilder).build();
        try {
            scheduler.scheduleJob(jobDetail, cronTrigger);
            log.info(machineId + "号仪器开始任务，时间间隔为" + cron);
            jobMap.put(machineId, jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @PostMapping("/pauseByMachineId")
    public String pauseByMachineId(@RequestParam(name = "machineId") Integer machineId) {
        try {
            if (jobMap.containsKey(machineId)) {
                scheduler.pauseJob(jobMap.get(machineId));
                pausedJobMap.put(machineId, jobMap.get(machineId));
                jobMap.remove(machineId);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @PostMapping("/restartById")
    public String restartById(@RequestParam(name = "machineId") Integer machineId) {
        try {
            if (pausedJobMap.containsKey(machineId)) {
                scheduler.resumeJob(pausedJobMap.get(machineId));
                jobMap.put(machineId, pausedJobMap.get(machineId));
                pausedJobMap.remove(machineId);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "failed";
        }
        return "success";
    }

    @PostMapping("/stopOne")
    public String stopById(@RequestParam("machineId") Integer machineId) {
        try {
            if (jobMap.containsKey(machineId)) {
                scheduler.deleteJob(jobMap.get(machineId));
            } else if (pausedJobMap.containsKey(machineId)) {
                scheduler.deleteJob(pausedJobMap.get(machineId));
            } else {
                return "failed";
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "failed";
        }
        return "success";
    }

    @PostMapping("/pauseAll")
    public String pauseAll() {
        try {
            scheduler.pauseAll();
            pausedJobMap.putAll(jobMap);
            jobMap.clear();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @PostMapping("/restartAll")
    public String restartAll() {
        try {
            scheduler.resumeAll();
            jobMap.putAll(pausedJobMap);
            pausedJobMap.clear();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return "success";
    }


    @PostMapping("/stopAll")
    public String stopAll() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
                pausedJobMap.clear();
                jobMap.clear();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return "所有定时任务已关闭";
    }


}
