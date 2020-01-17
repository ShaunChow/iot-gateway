package com.shaun.SerialPortClient.eventbus.subscribe;

import com.shaun.SerialPortClient.eventbus.message.TcpMessage;
import com.shaun.SerialPortClient.service.ChannelCacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TcpSubscribe implements TcpListening {

    @Autowired
    private ChannelCacheService channelCacheService;

    @Override
    public void on(TcpMessage message) {
        channelCacheService.cacheResult(message.getMessageType(), message.getMessageContent());
    }
}