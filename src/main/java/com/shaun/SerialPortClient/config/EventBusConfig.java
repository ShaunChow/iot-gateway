package com.shaun.SerialPortClient.config;

import com.google.common.eventbus.EventBus;
import com.shaun.SerialPortClient.eventbus.TcpEventBus;
import com.shaun.SerialPortClient.eventbus.subscribe.TcpListening;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventBusConfig {
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public EventBus tcpEventBusRegister() {
        applicationContext.getBeansOfType(TcpListening.class).values().stream().forEach(TcpEventBus::register);
        return TcpEventBus.SINGLETON;
    }
}