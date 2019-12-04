package com.shaun.SerialPortClient.service;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ModbusResponse;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

import java.io.IOException;

@Service
public class J2modService {

    private static final Logger log = LoggerFactory.getLogger(J2modService.class);

    @Autowired
    private SerialParameters serialParameters;

    private static SerialConnection serialConnection;

    public void start() {
        log.info("serial connecting...");

        serialConnection = new SerialConnection(serialParameters);
        serialConnection.setTimeout(500);
        try {
            serialConnection.open();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        log.info("serial connect success!");

        cardHeartBeat();
    }

    public void cardHeartBeat() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000);
                    // Query according to Meter Manual
                    ReadMultipleRegistersRequest writeMultipleRegistersRequest = new ReadMultipleRegistersRequest(0, 2);
                    writeMultipleRegistersRequest.setUnitID(1);
                    writeMultipleRegistersRequest.setHeadless(true);

                    log.info("Request  Hex >> " + writeMultipleRegistersRequest.getHexMessage());

                    ModbusResponse writeMultipleRegistersResponse = getModbusSerialResponse(serialConnection,
                            writeMultipleRegistersRequest);

                    log.info("Response Hex >> " + writeMultipleRegistersResponse.getHexMessage());

                } catch (InterruptedException | ModbusException e) {
                    log.error(e.getMessage());
                }
            }
        }).start();
    }

    @PreDestroy
    public void destroy() {
        log.info("destroy serial connecting...");
        serialConnection.close();
        log.info("destroy serial success!");
    }

    private ModbusResponse getModbusSerialResponse(SerialConnection serialConnection, ModbusRequest modbusRequest)
            throws ModbusException {
        ModbusSerialTransaction modbusSerialTransaction = new ModbusSerialTransaction(serialConnection);
        modbusSerialTransaction.setRequest(modbusRequest);
        modbusSerialTransaction.setTransDelayMS(10);
        modbusSerialTransaction.execute();
        return modbusSerialTransaction.getResponse();
    }
}
