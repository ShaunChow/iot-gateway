package com.shaun.SerialPortClient.config;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("serialport")
public class SerialPortConfig implements Serializable {
    private static final long serialVersionUID = -1186314202499099206L;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}