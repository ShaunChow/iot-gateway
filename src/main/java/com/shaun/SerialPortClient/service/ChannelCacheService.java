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

    @CachePut(value = "tcp_result", key = "#key")
    public String cacheResult(String key, String content) {
        return content;
    }

    public String getCacheResult(String key) {
        return cacheManager.getCache("tcp_result").get(key, String.class);
    }

    @CacheEvict(value = "tcp_result", key = "#key")
    public void evictCacheResult(String key) {
    }

}