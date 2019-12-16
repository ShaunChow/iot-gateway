package com.shaun.SerialPortClient;

import com.shaun.SerialPortClient.config.properties.SerialPortProperties;
import com.shaun.SerialPortClient.service.J2modService;
import com.shaun.SerialPortClient.service.server.BootTcpServer;
import com.shaun.SerialPortClient.service.server.BootUdpServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import reactor.netty.Connection;
import reactor.netty.DisposableServer;

@SpringBootApplication
public class SerialPortClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(SerialPortClientApplication.class, args);
    }

    @Bean
    DisposableServer tcpServerRunner(BootTcpServer bootTcpServer) {
        return bootTcpServer.runTcpServer();
    }

    // @Bean
    // Connection udpServerRunner(BootUdpServer bootUdpServer) {
    // return bootUdpServer.runUdpServer();
    // }

    // @Bean
    // boolean startModbusListening(J2modService j2modService) {

    // return j2modService.start();

    // // ModbusTCPMaster master = new ModbusTCPMaster("10.252.31.144", 26, true);
    // // try {
    // // master.connect();
    // // while (true) {
    // // Register[] resul = master.readMultipleRegisters(1, 0, 2);
    // // }
    // // } catch (Exception e) {
    // // // TODO Auto-generated catch block
    // // e.printStackTrace();
    // // }s
    // }

}