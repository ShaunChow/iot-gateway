package com.shaun.SerialPortClient.eventbus.subscribe;

import com.google.common.eventbus.Subscribe;
import com.shaun.SerialPortClient.eventbus.message.TcpMessage;
import com.shaun.SerialPortClient.service.ChannelCacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TcpSubscribe {

    @Autowired
    private ChannelCacheService channelCacheService;

    @Subscribe
    public void on(TcpMessage message) {
        System.out.println("TcpSubscribe message->  messgeType：" + message.getMessageType() + "\n messageContent："
                + message.getMessageContent());

        channelCacheService.cacheResult(message.getMessageType(), message.getMessageContent());
    }
}