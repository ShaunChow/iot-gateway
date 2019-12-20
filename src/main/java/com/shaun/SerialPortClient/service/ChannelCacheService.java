package com.shaun.SerialPortClient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import io.netty.channel.Channel;

@Service
public class ChannelCacheService {

    @Autowired
    private CacheManager cacheManager;

    @CachePut(value = "tcp_connection", key = "#key")
    public Channel cache(String key, Channel channel) {
        return channel;
    }

    public Channel getCache(String key) {
        return cacheManager.getCache("tcp_connection").get(key, Channel.class);
    }

    @CacheEvict(value = "tcp_connection", key = "#key")
    public void evictCache(String key) {
    }

}