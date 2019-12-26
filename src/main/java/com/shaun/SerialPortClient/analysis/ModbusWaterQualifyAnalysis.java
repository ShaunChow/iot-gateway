package com.shaun.SerialPortClient.analysis;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ModbusWaterQualifyAnalysis {

    private static DecimalFormat twoPointReserve = new DecimalFormat("0.00");

    private static DecimalFormat threePointReserve = new DecimalFormat("0.000");

    public static Map<String, Object> getMap(String hexStr) {

        // eg: hexStr = 01030422A50872678D
        hexStr = hexStr.substring(6);
        // hexStr = 0422A50872678D

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("value1", threePointReserve.format(Integer.parseInt(hexStr.substring(0, 4), 16) / (float) 1000));
        resultMap.put("value2", twoPointReserve.format(Integer.parseInt(hexStr.substring(4, 8), 16) / (float) 100));
        return resultMap;
    }

}