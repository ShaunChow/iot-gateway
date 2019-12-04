package com.shaun.SerialPortClient.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class J2modService {

    private static final Logger log = LoggerFactory.getLogger(J2modService.class);

    public volatile String result = "";

    private DecimalFormat twoPointReserve = new DecimalFormat("0.00");
    private DecimalFormat threePointReserve = new DecimalFormat("0.000");

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
                    ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(0, 2);
                    request.setUnitID(1);
                    request.setHeadless(true);

                    log.info("Request  Hex >> " + request.getHexMessage());
                    ModbusResponse response = getModbusSerialResponse(serialConnection, request);
                    log.info("Response Hex >> " + response.getHexMessage());

                    String dataStr = response.getHexMessage().replace(" ", "").substring(6);
                    Map<String, String> resultMap = new HashMap<String, String>();
                    resultMap.put("value1",
                            threePointReserve.format(Integer.parseInt(dataStr.substring(0, 4), 16) / (float) 1000));
                    resultMap.put("value2",
                            twoPointReserve.format(Integer.parseInt(dataStr.substring(4), 16) / (float) 100));
                    result = new ObjectMapper().writeValueAsString(resultMap);

                    log.info("Response result >> " + result);

                } catch (InterruptedException | ModbusException | JsonProcessingException e) {
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
