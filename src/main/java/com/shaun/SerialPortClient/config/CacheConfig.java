package com.shaun.SerialPortClient.config;

import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(
                Arrays.asList(new ConcurrentMapCache("tcp_connection"), new ConcurrentMapCache("tcp_result")));
        return simpleCacheManager;
    }
}