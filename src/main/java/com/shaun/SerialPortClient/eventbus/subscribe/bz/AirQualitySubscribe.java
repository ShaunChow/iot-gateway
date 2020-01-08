package com.shaun.SerialPortClient.eventbus.subscribe.bz;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.Subscribe;
import com.shaun.SerialPortClient.analysis.ModbusAirQualityAnalysis;
import com.shaun.SerialPortClient.entity.IotRecord;
import com.shaun.SerialPortClient.eventbus.message.TcpMessage;
import com.shaun.SerialPortClient.repository.IotRecordRepository;
import com.shaun.SerialPortClient.service.ChannelCacheService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AirQualitySubscribe {

    private static final Logger log = LoggerFactory.getLogger(AirQualitySubscribe.class);

    public static String PROTOCAL_PREFIX = "AirQuality";

    private LocalDateTime lastUpdateTime = LocalDateTime.MIN;

    @Autowired
    private ChannelCacheService channelCacheService;

    @Autowired
    private IotRecordRepository iotRecordRepossitory;

    @Subscribe
    public void on(TcpMessage message) throws JsonProcessingException {

        if (!message.getMessageType().startsWith(PROTOCAL_PREFIX)
                || lastUpdateTime.plusMinutes(10).isAfter(LocalDateTime.now()))
            return;

        lastUpdateTime = LocalDateTime.now();

        Map<String, Object> airResult = ModbusAirQualityAnalysis.getMap(message.getMessageContent());

        channelCacheService.cacheBusinessResult(message.getMessageType(), airResult);

        IotRecord newRecord = new IotRecord();
        newRecord.setCategory("AirQuality");
        newRecord.setSource(message.getMessageType().substring(message.getMessageType().indexOf(":") + 1));
        newRecord.setBody(new ObjectMapper().writeValueAsString(airResult));
        iotRecordRepossitory.save(newRecord);

        log.info("AirQualitySubscribe message->  messgeType：" + message.getMessageType() + "\n messageContent：\n"
                + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(airResult));
    }

}