package com.shaun.SerialPortClient.config;

import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.util.SerialParameters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class J2modConfig {

    private SerialPortConfig serialPortConfig;

    public J2modConfig(SerialPortConfig serialPortConfig) {
        this.serialPortConfig = serialPortConfig;
    }

    @Bean
    public SerialParameters getSerialParameters() {
        SerialParameters serialConfig = new SerialParameters();
        serialConfig.setPortName(serialPortConfig.getName());
        serialConfig.setEncoding(Modbus.SERIAL_ENCODING_RTU);
        return serialConfig;
    }
}