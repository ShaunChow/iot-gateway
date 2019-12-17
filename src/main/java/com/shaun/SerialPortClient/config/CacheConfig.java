package com.shaun.SerialPortClient.config;

import java.util.List;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

	@Bean
	public CacheManager cacheManager(List<Cache> caches) {
		SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
		simpleCacheManager.setCaches(caches);
		return simpleCacheManager;
	}

	@Bean("tcpConnection")
	public ConcurrentMapCacheFactoryBean tcpConnection() {
		ConcurrentMapCacheFactoryBean tcpConnection = new ConcurrentMapCacheFactoryBean();
		tcpConnection.setName("tcp_connection");
		return tcpConnection;
	}

	@Bean("tcpResult")
	public ConcurrentMapCacheFactoryBean tcpResult() {
		ConcurrentMapCacheFactoryBean tcpResult = new ConcurrentMapCacheFactoryBean();
		tcpResult.setName("tcp_result");
		return tcpResult;
	}
}