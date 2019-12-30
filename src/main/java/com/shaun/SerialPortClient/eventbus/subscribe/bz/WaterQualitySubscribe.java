package com.shaun.SerialPortClient.eventbus.subscribe.bz;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.Subscribe;
import com.shaun.SerialPortClient.analysis.ModbusWaterQualityAnalysis;
import com.shaun.SerialPortClient.eventbus.message.TcpMessage;
import com.shaun.SerialPortClient.service.ChannelCacheService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WaterQualitySubscribe {

    private static final Logger log = LoggerFactory.getLogger(WaterQualitySubscribe.class);

    public static String PROTOCAL_PREFIX = "WaterQuality";

    @Autowired
    private ChannelCacheService channelCacheService;

    @Subscribe
    public void on(TcpMessage message) throws JsonProcessingException {

        if (!message.getMessageType().startsWith(PROTOCAL_PREFIX))
            return;

        Map<String, Object> waterResult = ModbusWaterQualityAnalysis.getMap(message.getMessageContent());

        channelCacheService.cacheBusinessResult(message.getMessageType(), waterResult);

        log.info("WaterQualitySubscribe message->  messgeType：" + message.getMessageType() + "\n messageContent：\n"
                + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(waterResult));
    }
}