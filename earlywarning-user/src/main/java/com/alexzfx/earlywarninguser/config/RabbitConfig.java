package com.alexzfx.earlywarninguser.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author : Alex
 * Date : 2018/4/12 8:50
 * Description :
 */
@Configuration
public class RabbitConfig {

    private static final int DEFAULT_CONCURRENT = 10;

    private static final int DEFAULT_PREFETCH_COUNT = 50;

    public static final String MACHINE_DATA_QUEUE = "machineData";

    @Bean
    public Queue queue() {
        return new Queue(MACHINE_DATA_QUEUE);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(DEFAULT_CONCURRENT);
        factory.setPrefetchCount(DEFAULT_PREFETCH_COUNT);
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}
