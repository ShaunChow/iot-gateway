package com.shaun.SerialPortClient.eventbus.subscribe.bz;

import java.util.Map;

import com.google.common.eventbus.Subscribe;
import com.shaun.SerialPortClient.analysis.ModbusWaterQualityAnalysis;
import com.shaun.SerialPortClient.eventbus.message.TcpMessage;
import com.shaun.SerialPortClient.service.ChannelCacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WaterQualitySubscribe {

    public static String PROTOCAL_PREFIX = "WaterQuality";

    @Autowired
    private ChannelCacheService channelCacheService;

    @Subscribe
    public void on(TcpMessage message) {

        if (!message.getMessageType().startsWith(PROTOCAL_PREFIX))
            return;

        Map<String, Object> waterResult = ModbusWaterQualityAnalysis.getMap(message.getMessageContent());

        channelCacheService.cacheBusinessResult(message.getMessageType(), waterResult);

        System.out.println("WaterQualitySubscribe message->  messgeType：" + message.getMessageType()
                + "\n messageContent：" + waterResult);
    }
}