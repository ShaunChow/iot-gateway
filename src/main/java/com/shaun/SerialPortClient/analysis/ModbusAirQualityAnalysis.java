package com.shaun.SerialPortClient.analysis;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModbusAirQualityAnalysis {

    private static final Logger log = LoggerFactory.getLogger(ModbusAirQualityAnalysis.class);

    private static DecimalFormat onePointReserve = new DecimalFormat("0.0");

    private static DecimalFormat twoPointReserve = new DecimalFormat("0.00");

    private static DecimalFormat threePointReserve = new DecimalFormat("0.000");

    public static Map<String, Object> getMap(String hexStr) {

        // eg: hexStr =
        // 000003850474002200D20012000000FA0130006A0005000000000000025400270384000300DC02580262026C02760280044C00050038C350

        Map<String, Object> resultMap = new HashMap<String, Object>();

        // equipment_code
        log.debug("equipment_code [" + hexStr.substring(0, 8) + "]");
        resultMap.put("equipment_code", hexStr.substring(0, 8));

        // CO2
        String co2Hex = hexStr.substring(8, 12);
        Integer co2Int = Integer.parseInt(co2Hex, 16);
        log.debug("CO2 [" + co2Hex + "] -> [" + co2Int + "]");
        resultMap.put("CO2", co2Int);

        // PM25
        String pm25Hex = hexStr.substring(12, 16);
        Integer pm25Int = Integer.parseInt(pm25Hex, 16);
        log.debug("PM25 [" + pm25Hex + "] -> [" + pm25Int + "]");
        resultMap.put("PM25", pm25Int);

        // O2
        String o2Hex = hexStr.substring(16, 20);
        String o2Int = onePointReserve.format(Integer.parseInt(o2Hex, 16) / (float) 10);
        log.debug("O2 [" + o2Hex + "] -> [" + o2Int + "]");
        resultMap.put("O2", o2Int);

        // TVOC
        String tvocHex = hexStr.substring(20, 24);
        Integer tvocInt = Integer.parseInt(tvocHex, 16);
        log.debug("TVOC [" + tvocHex + "] -> [" + tvocInt + "]");
        resultMap.put("TVOC", tvocInt);

        // smoke
        String smokeHex = hexStr.substring(24, 28);
        Integer smokeInt = Integer.parseInt(smokeHex, 16);
        log.debug("smoke [" + smokeHex + "] -> [" + smokeInt + "]");
        resultMap.put("smoke", smokeInt);

        // temperature
        String temperatureHex = hexStr.substring(28, 32);
        Integer temperatureInt = Integer.parseInt(temperatureHex, 16);
        log.debug("temperature [" + temperatureHex + "] -> [" + temperatureInt + "]");
        resultMap.put("temperature", temperatureInt);

        // humidity
        String humidityHex = hexStr.substring(32, 36);
        Integer humidityInt = Integer.parseInt(humidityHex, 16);
        log.debug("humidity [" + humidityHex + "] -> [" + humidityInt + "]");
        resultMap.put("humidity", humidityInt);

        // SO2
        String so2Hex = hexStr.substring(36, 40);
        Integer so2Int = Integer.parseInt(so2Hex, 16);
        log.debug("SO2 [" + so2Hex + "] -> [" + so2Int + "]");
        resultMap.put("SO2", so2Int);

        // CH2O
        String ch2oHex = hexStr.substring(40, 44);
        Integer ch2oInt = Integer.parseInt(ch2oHex, 16);
        log.debug("CH2O [" + ch2oHex + "] -> [" + ch2oInt + "]");
        resultMap.put("CH2O", ch2oInt);

        // CO
        String coHex = hexStr.substring(44, 48);
        Integer coInt = Integer.parseInt(coHex, 16);
        log.debug("CO [" + coHex + "] -> [" + coInt + "]");
        resultMap.put("CO", coInt);

        // negative_oxygen_ion
        String negativeOxygenIonHex = hexStr.substring(48, 52);
        Integer negativeOxygenIonInt = Integer.parseInt(negativeOxygenIonHex, 16);
        log.debug("negative_oxygen_ion [" + negativeOxygenIonHex + "] -> [" + negativeOxygenIonInt + "]");
        resultMap.put("negative_oxygen_ion", negativeOxygenIonInt);

        // CH4
        String ch4Hex = hexStr.substring(52, 56);
        Integer ch4Int = Integer.parseInt(ch4Hex, 16);
        log.debug("CH4 [" + ch4Hex + "] -> [" + ch4Int + "]");
        resultMap.put("CH4", ch4Int);

        // noise
        String noiseHex = hexStr.substring(56, 60);
        Integer noiseInt = Integer.parseInt(noiseHex, 16);
        log.debug("noise [" + noiseHex + "] -> [" + noiseInt + "]");
        resultMap.put("noise", noiseInt);

        // PM10
        String pm10Hex = hexStr.substring(60, 64);
        Integer pm10Int = Integer.parseInt(pm10Hex, 16);
        log.debug("PM10 [" + pm10Hex + "] -> [" + pm10Int + "]");
        resultMap.put("PM10", pm10Int);

        // TSP
        String tspHex = hexStr.substring(64, 68);
        Integer tspInt = Integer.parseInt(tspHex, 16);
        log.debug("TSP [" + tspHex + "] -> [" + tspInt + "]");
        resultMap.put("TSP", tspInt);

        // wind_speed
        String windSpeedHex = hexStr.substring(68, 72);
        Integer windSpeedInt = Integer.parseInt(windSpeedHex, 16);
        log.debug("wind_speed [" + windSpeedHex + "] -> [" + windSpeedInt + "]");
        resultMap.put("wind_speed", windSpeedInt);

        // wind_direction
        String windDirectionHex = hexStr.substring(72, 76);
        Integer windDirectionInt = Integer.parseInt(windDirectionHex, 16);
        log.debug("wind_direction [" + windDirectionHex + "] -> [" + windDirectionInt + "]");
        resultMap.put("wind_direction", windDirectionInt);

        // NO
        String noHex = hexStr.substring(76, 80);
        Integer noInt = Integer.parseInt(noHex, 16);
        log.debug("NO [" + noHex + "] -> [" + noInt + "]");
        resultMap.put("NO", noInt);

        // NO2
        String no2Hex = hexStr.substring(80, 84);
        Integer no2Int = Integer.parseInt(no2Hex, 16);
        log.debug("NO2 [" + no2Hex + "] -> [" + no2Int + "]");
        resultMap.put("NO2", no2Int);

        // O3
        String o3Hex = hexStr.substring(84, 88);
        Integer o3Int = Integer.parseInt(o3Hex, 16);
        log.debug("O3 [" + o3Hex + "] -> [" + o3Int + "]");
        resultMap.put("O3", o3Int);

        // H2S
        String h2sHex = hexStr.substring(88, 92);
        Integer h2sInt = Integer.parseInt(h2sHex, 16);
        log.debug("H2S [" + h2sHex + "] -> [" + h2sInt + "]");
        resultMap.put("H2S", h2sInt);

        // NH3
        String nh3Hex = hexStr.substring(92, 96);
        Integer nh3Int = Integer.parseInt(nh3Hex, 16);
        log.debug("NH3 [" + nh3Hex + "] -> [" + nh3Int + "]");
        resultMap.put("NH3", nh3Int);

        // atmospheric_pressur
        String atmosphericPressurHex = hexStr.substring(96, 100);
        Integer atmosphericPressurInt = Integer.parseInt(atmosphericPressurHex, 16);
        log.debug("atmospheric_pressur [" + atmosphericPressurHex + "] -> [" + atmosphericPressurInt + "]");
        resultMap.put("atmospheric_pressur", atmosphericPressurInt);

        // negative_oxygen_ion_standard
        String negativeOxygenIonStandardHex = hexStr.substring(100, 102);
        Integer negativeOxygenIonStandardInt = Integer.parseInt(negativeOxygenIonStandardHex, 16);
        log.debug("negative_oxygen_ion_standard [" + negativeOxygenIonStandardHex + "] -> ["
                + negativeOxygenIonStandardInt + "]");
        resultMap.put("negative_oxygen_ion_standard", negativeOxygenIonStandardInt);

        // wind_speed_standard
        String windSpeedStandardHex = hexStr.substring(102, 104);
        Integer windSpeedStandardInt = Integer.parseInt(windSpeedStandardHex, 16);
        log.debug("wind_speed_standard [" + windSpeedStandardHex + "] -> [" + windSpeedStandardInt + "]");
        resultMap.put("wind_speed_standard", windSpeedStandardInt);

        // AQI
        String aqiHex = hexStr.substring(104, 108);
        Integer aqiInt = Integer.parseInt(aqiHex, 16);
        log.debug("AQI [" + aqiHex + "] -> [" + aqiInt + "]");
        resultMap.put("AQI", aqiInt);

        // illuminance
        String illuminanceHex = hexStr.substring(108, 112);
        Integer illuminanceInt = Integer.parseInt(illuminanceHex, 16);
        log.debug("illuminance [" + illuminanceHex + "] -> [" + illuminanceInt + "]");
        resultMap.put("illuminance", illuminanceInt);

        return resultMap;
    }

}