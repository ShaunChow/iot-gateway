package com.shaun.SerialPortClient.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import io.netty.channel.Channel;

@EnableAspectJAutoProxy
@Service
public class ChannelCacheService {

    @CachePut(value = "tcp_connection", key = "#key")
    public Channel cache(String key, Channel channel) {
        return channel;
    }

}