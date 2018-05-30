package com.alexzfx.earlywarningmachine.quartz;

import com.alexzfx.earlywarningmachine.rabbit.MachineMessage;
import com.alexzfx.earlywarningmachine.rabbit.MessageSender;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Author : Alex
 * Date : 2018/4/14 16:02
 * Description :
 */
@Component
@Data
@Slf4j
public class MachineDataTask implements InterruptableJob {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap map = jobExecutionContext.getMergedJobDataMap();
        int machineId = map.getInt("machineId");
        String type = map.getString("cid");
        double data = getData();
        MessageSender sender = (MessageSender) map.get("sender");
        MachineMessage machineMessage = new MachineMessage(machineId, data);
        log.info(machineMessage.toString());
        sender.machineDataSender(machineMessage);
    }


    @Override
    public void interrupt() {
    }

    private double getData() {
        Random random = new Random();
        double data = 0;
        int magicNum = 40;
        int magicChange = 10;
        double magicScale = 0.07;
        double radians = Math.toRadians(random.nextDouble() * 360);
        double change = 5 * Math.sin(radians);
        data = magicNum + change;
        if (magicScale > random.nextDouble()) {
            data += random.nextInt(magicChange);
        }
        return data;
    }


}
