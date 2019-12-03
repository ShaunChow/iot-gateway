package com.shaun.SerialPortClient.listener;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;

public class TacsFixedListener implements SerialPortPacketListener {

    public int getListeningEvents() {
        // TODO Auto-generated method stub
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    public void serialEvent(SerialPortEvent event) {
        // TODO Auto-generated method stub
        byte[] newData = event.getReceivedData();
        System.out.println("Received data of size: " + newData.length);
        for (int i = 0; i < newData.length; ++i)
            System.out.print((char) newData[i]);
        System.out.println("\n");
    }

    public int getPacketSize() {
        // TODO Auto-generated method stub
        return 6;
    }

}