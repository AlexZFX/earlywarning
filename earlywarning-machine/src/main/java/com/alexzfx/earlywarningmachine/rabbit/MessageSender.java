package com.alexzfx.earlywarningmachine.rabbit;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.alexzfx.earlywarningmachine.config.RabbitConfig.MACHINE_DATA_QUEUE;

/**
 * Author : Alex
 * Date : 2018/4/12 14:57
 * Description :
 */

@Component
public class MessageSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void machineDataSender(MachineMessage message) {
        String msg = JSON.toJSONString(message);
        rabbitTemplate.convertAndSend(MACHINE_DATA_QUEUE, msg);
    }
}
