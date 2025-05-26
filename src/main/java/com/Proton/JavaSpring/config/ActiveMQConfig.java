package com.Proton.JavaSpring.config;

import jakarta.jms.Queue;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ActiveMQConfig {

    @Value("${queue.balance}")
    private String balanceQueue;

    @Bean
    public Queue balanceQueue() {
        return new ActiveMQQueue(balanceQueue);
    }

    @Bean
    public Queue queue() {
        return new ActiveMQQueue("payment.queue");
    }
}

