package com.shaun.SerialPortClient.service;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.lang.Nullable;
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

    public Set<Object> getCacheKeys() {
        ConcurrentMapCache tcpConnections = (ConcurrentMapCache) cacheManager.getCache("tcp_connection");
        ConcurrentMap<Object, Object> map = tcpConnections.getNativeCache();
        return map.keySet();
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

    public Set<Object> getCacheResultKeys() {
        ConcurrentMapCache tcpConnections = (ConcurrentMapCache) cacheManager.getCache("tcp_result");
        ConcurrentMap<Object, Object> map = tcpConnections.getNativeCache();
        return map.keySet();
    }

    @CacheEvict(value = "tcp_result", key = "#key")
    public void evictCacheResult(String key) {
    }

    @CachePut(value = "business_result", key = "#key")
    public Object cacheBusinessResult(String key, Object content) {
        return content;
    }

    public <T> T getCacheBusinessResult(String key, @Nullable Class<T> type) {
        return cacheManager.getCache("business_result").get(key, type);
    }

    public Set<Object> getCacheBusinessResultKeys() {
        ConcurrentMapCache tcpConnections = (ConcurrentMapCache) cacheManager.getCache("business_result");
        ConcurrentMap<Object, Object> map = tcpConnections.getNativeCache();
        return map.keySet();
    }

    @CacheEvict(value = "business_result", key = "#key")
    public void evictCacheBusinessResult(String key) {
    }

    public void evictAllCacheByIp(String ip, String port, String currentThreadName) {

        Arrays.stream(getCacheKeys().toArray()).forEach((e -> {
            String[] tempArray = e.toString().split(":");
            if (tempArray.length == 4 && tempArray[1].equals(ip) && tempArray[2].equals(port)
                    && tempArray[3].equals(currentThreadName)) {
                cacheManager.getCache("tcp_connection").evict(e.toString());
            }
        }));

        Arrays.stream(getCacheResultKeys().toArray()).forEach((e -> {
            String[] tempArray = e.toString().split(":");
            if (tempArray.length == 4 && tempArray[1].equals(ip) && tempArray[2].equals(port)
                    && tempArray[3].equals(currentThreadName)) {
                cacheManager.getCache("tcp_result").evict(e.toString());
            }
        }));

        Arrays.stream(getCacheBusinessResultKeys().toArray()).forEach((e -> {
            String[] tempArray = e.toString().split(":");
            if (tempArray.length == 4 && tempArray[1].equals(ip) && tempArray[2].equals(port)
                    && tempArray[3].equals(currentThreadName)) {
                cacheManager.getCache("business_result").evict(e.toString());
            }
        }));

    }

}