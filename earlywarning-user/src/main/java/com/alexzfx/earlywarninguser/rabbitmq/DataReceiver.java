package com.alexzfx.earlywarninguser.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.alexzfx.earlywarninguser.config.RabbitConfig.MACHINE_DATA_QUEUE;

/**
 * Author : Alex
 * Date : 2018/4/11 18:51
 * Description : 用来接收 MQ 数据的类
 */
@Component
@RabbitListener(queues = MACHINE_DATA_QUEUE, containerFactory = "simpleRabbitListenerContainerFactory")
public class DataReceiver {

    @RabbitHandler
    public void machineProcess(MachineMessage message) {
        //TODO 数据处理办法
    }

}
