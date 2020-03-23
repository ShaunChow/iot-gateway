package com.shaun.SerialPortClient.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import io.netty.channel.Channel;
import net.sf.ehcache.Ehcache;

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

    @SuppressWarnings("unchecked")
    public List<Object> getCacheKeys() {
        Cache tcpConnections = cacheManager.getCache("tcp_connection");
        Object map = tcpConnections.getNativeCache();

        // SIMPLE
        if (map instanceof ConcurrentHashMap) {
            return new ArrayList<>(((Map<Object, Object>) map).keySet());
        }

        // EHCACHE
        if (map instanceof Ehcache) {
            return ((Ehcache) map).getKeys();
        }

        // CAFFEINE
        if (map instanceof com.github.benmanes.caffeine.cache.Cache) {
            return new ArrayList<>(
                (
                    (
                        (com.github.benmanes.caffeine.cache.Cache<Object,Object>)map
                    ).asMap()
                ).keySet());
        }
        return null;
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

    @SuppressWarnings("unchecked")
    public List<Object> getCacheResultKeys() {
        Cache tcpConnections = cacheManager.getCache("tcp_result");
        Object map = tcpConnections.getNativeCache();

        // SIMPLE
        if (map instanceof ConcurrentHashMap) {
            return new ArrayList<>(((Map<Object, Object>) map).keySet());
        }

        // EHCACHE
        if (map instanceof Ehcache) {
            return ((Ehcache) map).getKeys();
        }

        // CAFFEINE
        if (map instanceof com.github.benmanes.caffeine.cache.Cache) {
            return new ArrayList<>(
                (
                    (
                        (com.github.benmanes.caffeine.cache.Cache<Object,Object>)map
                    ).asMap()
                ).keySet());
        }
        return null;
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

    @SuppressWarnings("unchecked")
    public List<Object> getCacheBusinessResultKeys() {
        Cache tcpConnections = cacheManager.getCache("business_result");
        Object map = tcpConnections.getNativeCache();

        // SIMPLE
        if (map instanceof ConcurrentHashMap) {
            return new ArrayList<>(((Map<Object, Object>) map).keySet());
        }

        // EHCACHE
        if (map instanceof Ehcache) {
            return ((Ehcache) map).getKeys();
        }

        // CAFFEINE
        if (map instanceof com.github.benmanes.caffeine.cache.Cache) {
            return new ArrayList<>(
                (
                    (
                        (com.github.benmanes.caffeine.cache.Cache<Object,Object>)map
                    ).asMap()
                ).keySet());
        }
        return null;
    }

    @CacheEvict(value = "business_result", key = "#key")
    public void evictCacheBusinessResult(String key) {
    }

    public void evictAllCacheByIp(String tcpStatus) {

        String[] param = tcpStatus.split(":");
        Assert.isTrue(4 == param.length,
                "Cache Kye must be composed by 4 part, for example {protocal}:{ip}:{port}:{timestamp}!");
        String ip = param[1];
        String port = param[2];
        String timestamp = param[3];

        Arrays.stream(getCacheKeys().toArray()).forEach((e -> {
            String[] tempArray = e.toString().split(":");
            if (tempArray.length == 4 && tempArray[1].equals(ip) && tempArray[2].equals(port)
                    && tempArray[3].equals(timestamp)) {
                cacheManager.getCache("tcp_connection").evict(e.toString());
            }
        }));

        Arrays.stream(getCacheResultKeys().toArray()).forEach((e -> {
            String[] tempArray = e.toString().split(":");
            if (tempArray.length == 4 && tempArray[1].equals(ip) && tempArray[2].equals(port)
                    && tempArray[3].equals(timestamp)) {
                cacheManager.getCache("tcp_result").evict(e.toString());
            }
        }));

        Arrays.stream(getCacheBusinessResultKeys().toArray()).forEach((e -> {
            String[] tempArray = e.toString().split(":");
            if (tempArray.length == 4 && tempArray[1].equals(ip) && tempArray[2].equals(port)
                    && tempArray[3].equals(timestamp)) {
                cacheManager.getCache("business_result").evict(e.toString());
            }
        }));

    }

}