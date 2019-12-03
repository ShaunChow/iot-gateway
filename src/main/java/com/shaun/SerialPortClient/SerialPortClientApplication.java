package com.shaun.SerialPortClient;

import com.fazecast.jSerialComm.SerialPort;
import com.shaun.SerialPortClient.listener.TacsListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SerialPortClientApplication {

    private static SerialPort tacsPort = null;

    public static void main(String[] args) {
        SpringApplication.run(SerialPortClientApplication.class, args);

        System.out.println("Hello World!");
        commWithPort("COM5");
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tacsPort == null) {
            System.out.println("No connection.");
        } else {

            System.out.println("Connected.");
//            tacsPort.removeDataListener();
//            tacsPort.closePort();
        }

        while (true) {
            ;
        }
    }

    public static void commWithPort(String portname) {
        SerialPort[] commports = SerialPort.getCommPorts();
        for (SerialPort port : commports) {
            System.out.println(port.getPortDescription());
            if (tacsPort == null && port.getDescriptivePortName().contains(portname)) {
                tacsPort = port;
            }
        }
        if (tacsPort != null) {
            tacsPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
            tacsPort.openPort();
            tacsPort.addDataListener(new TacsListener());
        }
    }
}
