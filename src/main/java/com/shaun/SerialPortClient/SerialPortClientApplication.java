package com.shaun.SerialPortClient;

import com.shaun.SerialPortClient.config.SerialPortConfig;
import com.shaun.SerialPortClient.service.J2modService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({ SerialPortConfig.class })
public class SerialPortClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(SerialPortClientApplication.class, args);
    }

    @Autowired
    private J2modService j2modService;

    @Bean
    public void startModbusListening() {
        j2modService.start();
    }
}