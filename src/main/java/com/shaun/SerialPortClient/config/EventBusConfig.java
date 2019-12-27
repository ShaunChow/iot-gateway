package com.shaun.SerialPortClient.config;

import com.shaun.SerialPortClient.eventbus.TcpEventBus;
import com.shaun.SerialPortClient.eventbus.subscribe.TcpSubscribe;
import com.shaun.SerialPortClient.eventbus.subscribe.bz.AirQualitySubscribe;
import com.shaun.SerialPortClient.eventbus.subscribe.bz.WaterQualitySubscribe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventBusConfig {

    @Autowired
    private TcpSubscribe tcpSubscribe;

    @Autowired
    private WaterQualitySubscribe waterQualitySubscribe;

    @Autowired
    private AirQualitySubscribe airQualitySubscribe;

    @Bean
    public TcpEventBus tcpEventBusRegister() {
        TcpEventBus.register(tcpSubscribe);
        TcpEventBus.register(waterQualitySubscribe);
        TcpEventBus.register(airQualitySubscribe);
        return new TcpEventBus();
    }

}