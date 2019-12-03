package com.shaun.SerialPortClient.listener;


import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class TacsListener implements SerialPortDataListener {

    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
            return;
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] newData = new byte[event.getSerialPort().bytesAvailable()];
        int numRead = event.getSerialPort().readBytes(newData, newData.length);
        //redirect input to Tacs server
        System.out.println("Received data of size: " + numRead);
        System.out.println("Received data hex string: " + encodeHexString(newData));
        //for (int i = 0; i < newData.length; ++i)

//	         System.out.print((char)newData[i]);
        System.out.println("\n");
    }

    public String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(byteToHex(byteArray[i]));
        }
        return hexStringBuffer.toString();
    }

    public String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }
}
