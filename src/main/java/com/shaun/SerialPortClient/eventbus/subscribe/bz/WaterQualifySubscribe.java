package com.shaun.SerialPortClient.eventbus.subscribe.bz;

import java.util.Map;

import com.google.common.eventbus.Subscribe;
import com.shaun.SerialPortClient.analysis.ModbusWaterQualifyAnalysis;
import com.shaun.SerialPortClient.eventbus.message.TcpMessage;
import com.shaun.SerialPortClient.service.ChannelCacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WaterQualifySubscribe {

    @Autowired
    private ChannelCacheService channelCacheService;

    @Subscribe
    public void on(TcpMessage message) {

        Map<String, Object> waterResult = ModbusWaterQualifyAnalysis.getMap(message.getMessageContent());

        channelCacheService.cacheBusinessResult(message.getMessageType(), waterResult);

        System.out.println("WaterQualifySubscribe message->  messgeType：" + message.getMessageType()
                + "\n messageContent：" + waterResult);
    }
}