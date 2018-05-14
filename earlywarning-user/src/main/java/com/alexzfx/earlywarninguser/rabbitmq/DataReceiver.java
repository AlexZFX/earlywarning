package com.alexzfx.earlywarninguser.rabbitmq;

import com.alexzfx.earlywarninguser.entity.MachineData;
import com.alexzfx.earlywarninguser.service.MachineDataService;
import com.alexzfx.earlywarninguser.service.MailService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.alexzfx.earlywarninguser.config.RabbitConfig.MACHINE_DATA_QUEUE;

/**
 * Author : Alex
 * Date : 2018/4/11 18:51
 * Description : 用来接收 MQ 数据的类
 */
@Component
@RabbitListener(queues = MACHINE_DATA_QUEUE, containerFactory = "simpleRabbitListenerContainerFactory")
@Slf4j
public class DataReceiver {

//    private final Gson gson;

    private final MachineDataService machineDataService;

    private final MailService mailService;

    @Autowired
    public DataReceiver(MachineDataService machineDataService, MailService mailService) {
        this.machineDataService = machineDataService;
        this.mailService = mailService;
    }

    @RabbitHandler
    public void machineProcess(String json) {
        MachineData message = JSON.parseObject(json, MachineData.class);
        try {
            machineDataService.handleData(message);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

}
