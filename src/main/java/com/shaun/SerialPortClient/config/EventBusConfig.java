package com.shaun.SerialPortClient.config;

import com.shaun.SerialPortClient.eventbus.TcpEventBus;
import com.shaun.SerialPortClient.eventbus.subscribe.TcpSubscribe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventBusConfig {

    @Autowired
    private TcpSubscribe tcpSubscribe;

    @Bean
    public TcpEventBus tcpEventBusRegister() {
        TcpEventBus.register(tcpSubscribe);
        return new TcpEventBus();
    }

}